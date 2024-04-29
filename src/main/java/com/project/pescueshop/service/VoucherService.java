package com.project.pescueshop.service;

import com.project.pescueshop.model.dto.VoucherInputDTO;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.entity.Voucher;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.VoucherDAO;
import com.project.pescueshop.util.Util;
import com.project.pescueshop.util.constant.EnumResponseCode;
import com.project.pescueshop.util.constant.EnumStatus;
import com.project.pescueshop.util.constant.EnumVoucherType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherDAO voucherDAO;

    public Voucher findById(String voucherId){
        return voucherDAO.findVoucherById(voucherId);
    }

    public List<Voucher> findAllVoucher(){
        return voucherDAO.findAllVoucher();
    }

    public Voucher createNewVoucher(VoucherInputDTO inputDTO){
        EnumVoucherType voucherType = EnumVoucherType.getByValue(inputDTO.getType());

        Voucher voucher = Voucher.builder()
                .createdDate(Util.getCurrentDate())
                .maxValue(inputDTO.getMaxValue())
                .minInvoiceValue(inputDTO.getMinInvoiceValue())
                .price(inputDTO.getPrice())
                .status(EnumStatus.ACTIVE.getValue())
                .type(voucherType.getValue())
                .value(inputDTO.getValue())
                .build();

        voucherDAO.saveAndFlushVoucher(voucher);

        return voucher;
    }

    public void deleteVoucher(String voucherId) throws FriendlyException {
        Voucher voucher = findById(voucherId);

        if (voucher == null){
            throw new FriendlyException(EnumResponseCode.VOUCHER_NOT_FOUND);
        }

        voucherDAO.deleteVoucher(voucher);
    }

    public Voucher updateVoucher(String voucherId, VoucherInputDTO dto) throws FriendlyException {
        Voucher voucher = findById(voucherId);

        if (voucher == null){
            throw new FriendlyException(EnumResponseCode.VOUCHER_NOT_FOUND);
        }

        EnumVoucherType enumVoucherType = EnumVoucherType.getByValue(dto.getType());

        voucher.setMaxValue(dto.getMaxValue());
        voucher.setType(enumVoucherType.getValue());
        voucher.setPrice(dto.getPrice());
        voucher.setMinInvoiceValue(dto.getMinInvoiceValue());
        voucher.setValue(dto.getValue());

        voucherDAO.saveAndFlushVoucher(voucher);
        return voucher;
    }

    public List<Voucher> findAllAvailableVoucher(User user){
        return voucherDAO.findAllAvailableVoucher(user.getUserId());
    }
}
