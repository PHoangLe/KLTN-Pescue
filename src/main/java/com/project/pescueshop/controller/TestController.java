package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.ShippingFeeRequest;
import com.project.pescueshop.model.dto.ShippingFeeResponse;
import com.project.pescueshop.model.dto.ShippingItem;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.PaymentDAO;
import com.project.pescueshop.service.*;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@CrossOrigin
@Api
public class TestController {
}
