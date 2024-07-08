package com.project.pescueshop.service.data;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.project.pescueshop.model.elastic.ElasticClient;
import com.project.pescueshop.model.elastic.document.InvoiceData;
import com.project.pescueshop.model.elastic.document.RatingData;
import com.project.pescueshop.model.entity.ViewAuditLog;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.ViewAuditLogDAO;
import com.project.pescueshop.util.constant.EnumElasticIndex;
import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataService {
    private final ViewAuditLogDAO viewAuditLogDAO;

    public List<ViewAuditLog> getViewsAudiLogData(String objectId){
        return viewAuditLogDAO.getByObjectId(objectId);
    }

    public List<InvoiceData> getInvoiceData() throws FriendlyException {
        try {
            SearchResponse<InvoiceData> resp = ElasticClient.get().search(s -> s
                    .index(EnumElasticIndex.INVOICE_DATA.getName())
                    .size(9999)
                    , InvoiceData.class);

            return fromHitsToData(resp.hits().hits());
        } catch (IOException e) {
            log.error("Error while getting invoice data from elastic", e);
            throw new FriendlyException(EnumResponseCode.SYSTEM_ERROR);
        }
    }

    public List<RatingData> getRatingData() throws FriendlyException {
        try {
            SearchResponse<RatingData> resp = ElasticClient.get().search(s -> s
                            .index(EnumElasticIndex.RATING_DATA.getName())
                            .size(9999)
                    , RatingData.class);

            return fromHitsToData(resp.hits().hits());
        } catch (IOException e) {
            log.error("Error while getting invoice data from elastic", e);
            throw new FriendlyException(EnumResponseCode.SYSTEM_ERROR);
        }
    }

    private <T> List<T> fromHitsToData(List<Hit<T>> hits) {
        List<T> res = new ArrayList<>();
        for (Hit<T> hit: hits) {
            res.add(hit.source());
        }

        return res;
    }
}
