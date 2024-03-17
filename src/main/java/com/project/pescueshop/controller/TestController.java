package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.ProductDTO;
import com.project.pescueshop.model.entity.Invoice;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.PaymentDAO;
import com.project.pescueshop.service.*;
import com.project.pescueshop.util.Util;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@CrossOrigin
@Api
public class TestController {

    private final PaymentService paymentService;
    private final PaymentDAO paymentDAO;
    private final ThreadService threadService;

    @GetMapping("")
    public Object findAllProduct() throws FriendlyException, UnsupportedEncodingException {
//        ProductDTO url = productService.addVarietyAttribute(null, null);
        String key = Util.getRandomKey();
        Invoice invoice = paymentDAO.findInvoiceById("INVO_1703597043275_RR31F");
        threadService.sendReceiptEmail(invoice);
        return ResponseEntity.ok("");
    }
}
