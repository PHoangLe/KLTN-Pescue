package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.CartCheckOutInfoDTO;
import com.project.pescueshop.model.dto.PaymentInfoDTO;
import com.project.pescueshop.model.entity.Invoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MultiInvoiceService extends BaseService{
    private final InvoiceService invoiceService;

    public void createMultiInvoice(CartCheckOutInfoDTO cartCheckOutInfoDTO) {
//        log.info("Creating multi invoice for cart {}", cartCheckOutInfoDTO.getCartId());
//        PaymentInfoDTO paymentInfoDTO = cartCheckOutInfoDTO.getPaymentInfoDTO();
//        List<Invoice> invoices = new ArrayList<>();
//        for (int i = 0; i < paymentInfoDTO.getPaymentAmount(); i++) {
//            Invoice invoice = new Invoice();
//            invoice.setCartId(cartCheckOutInfoDTO.getCartId());
//            invoice.setPaymentAmount(paymentInfoDTO.getPaymentAmount());
//            invoice.setPaymentMethod(paymentInfoDTO.getPaymentMethod());
//            invoice.setPaymentStatus(paymentInfoDTO.getPaymentStatus());
//            invoice.setPaymentTime(paymentInfoDTO.getPaymentTime());
//            invoice.setPaymentType(paymentInfoDTO.getPaymentType());
//            invoice.setPaymentVendor(paymentInfoDTO.getPaymentVendor());
//            invoice.setShippingFee(paymentInfoDTO.getShippingFee());
//            invoice.setShippingMethod(paymentInfoDTO.getShippingMethod());
//            invoice.setShippingStatus(paymentInfoDTO.getShippingStatus());
//            invoice.setShippingTime(paymentInfoDTO.getShippingTime());
//            invoice.setShippingVendor(paymentInfoDTO.getShippingVendor());
//            invoice.setTotalAmount(paymentInfoDTO.getTotalAmount());
//            invoice.setUserId(paymentInfoDTO.getUserId());
//            invoices.add(invoice);
//        }
    }
}
