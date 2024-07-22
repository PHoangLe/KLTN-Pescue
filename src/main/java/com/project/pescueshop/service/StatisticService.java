package com.project.pescueshop.service;

import co.elastic.clients.elasticsearch._types.aggregations.HistogramBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.project.pescueshop.model.dto.ReportResultDTO;
import com.project.pescueshop.model.elastic.ElasticClient;
import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.MerchantDAO;
import com.project.pescueshop.util.constant.EnumElasticIndex;
import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticService {
    private final Double MONTH_IN_MILLIS = 2629800000.0;
    private final Double DAY_IN_MILLIS = 86400000.0;

    private final MerchantDAO merchantDAO;

    public List<ReportResultDTO> revenueStatisticForAdmin(long startTime, long endTime, String interval) throws IOException {
        Double i = interval.equals("day") ? DAY_IN_MILLIS : MONTH_IN_MILLIS;
        return revenueStatistic(startTime, endTime, null, i);
    }

    public List<ReportResultDTO> revenueStatusForMerchant(long startTime, long endTime, String interval) throws IOException, FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUserIfExist();
        if (user == null) {
            throw new FriendlyException(EnumResponseCode.MERCHANT_NOT_FOUND);
        }

        Merchant merchant = merchantDAO.getMerchantByUserId(user.getUserId());
        if (merchant == null) {
            throw new FriendlyException(EnumResponseCode.MERCHANT_NOT_FOUND);
        }

        Double i = interval.equals("day") ? DAY_IN_MILLIS : MONTH_IN_MILLIS;
        return revenueStatistic(startTime, endTime, merchant.getMerchantId(), i);
    }

    private List<ReportResultDTO> revenueStatistic(long startTime, long endTime, String merchantId, Double interval) throws IOException {
        final Query boolQuery;
        if (merchantId != null) {
            List<Query> must = new ArrayList<>();
            must.add(MatchQuery.of(q -> q
                    .field("merchantId")
                    .query(merchantId)
            )._toQuery());

            must.add(RangeQuery.of(q -> q
                    .field("timestamp")
                    .gte(JsonData.of(startTime))
                    .lte(JsonData.of(endTime))
            )._toQuery());

            boolQuery = BoolQuery.of(q -> q
                    .must(must)
            )._toQuery();
        } else {
            boolQuery = RangeQuery.of(q -> q
                        .field("timestamp")
                        .gte(JsonData.of(startTime))
                        .lte(JsonData.of(endTime)))._toQuery();
        }

        SearchResponse<Void> response = ElasticClient.get().search(b -> b
                        .index(EnumElasticIndex.INVOICE_DATA.getName())
                        .query(boolQuery)
                        .size(0)
                        .aggregations("price-histogram", a -> a
                                .histogram(h -> h
                                        .field("timestamp")
                                        .interval(interval)
                                )
                                .aggregations("total-amount", a2 -> a2
                                        .sum(s -> s
                                                .field("totalAmount")
                                        )
                                )
                        ),
                Void.class
        );

        List<HistogramBucket> buckets = response.aggregations()
                .get("price-histogram")
                .histogram()
                .buckets().array();

        List<ReportResultDTO> result = new ArrayList<>();
        for (HistogramBucket bucket: buckets) {
            Double timestampDouble = bucket.key();
            long timestamp = timestampDouble.longValue();

            Double totalAmountDouble = bucket.aggregations().get("total-amount").sum().value();
            long totalAmount = totalAmountDouble.longValue();

            result.add(ReportResultDTO.builder()
                    .reportTime(new Date(timestamp))
                    .totalSell(totalAmount)
                    .build()
            );
        }

        log.info("Revenue statistic: {}", result);
        return result;
    }
}
