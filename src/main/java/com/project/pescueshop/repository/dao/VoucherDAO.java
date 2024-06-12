package com.project.pescueshop.repository.dao;

import com.project.pescueshop.model.entity.Voucher;
import com.project.pescueshop.repository.jpa.VoucherRepository;
import com.project.pescueshop.util.constant.EnumStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VoucherDAO extends BaseDAO{
    private final VoucherRepository voucherRepository;

    public List<Voucher> findAllVoucher(){
        return voucherRepository.findAll();
    }

    public Voucher findVoucherById(String voucherId){
        return voucherRepository.findById(voucherId).orElse(null);
    }

    public void saveAndFlushVoucher(Voucher voucher){
        voucherRepository.saveAndFlush(voucher);
    }

    public void deleteVoucher(Voucher voucher){
        voucher.setStatus(EnumStatus.DELETED.getValue());
        saveAndFlushVoucher(voucher);
    }

    public List<Voucher> findAllAvailableVoucher(String userId){
        return voucherRepository.findAllAvailableVoucher(userId);
    }
}
