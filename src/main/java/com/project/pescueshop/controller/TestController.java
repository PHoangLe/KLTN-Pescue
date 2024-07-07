package com.project.pescueshop.controller;

import co.elastic.clients.elasticsearch._types.aggregations.HistogramBucket;
import com.project.pescueshop.model.dto.*;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.elastic.document.InvoiceData;
import com.project.pescueshop.model.elastic.document.RatingData;
import com.project.pescueshop.model.entity.Invoice;
import com.project.pescueshop.model.entity.live.LiveInvoice;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.PaymentDAO;
import com.project.pescueshop.service.*;
import com.project.pescueshop.service.data.DataService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@CrossOrigin
@Api
public class TestController {
}
