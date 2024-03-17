package com.project.pescueshop.service;

import com.project.pescueshop.model.entity.Brand;
import com.project.pescueshop.repository.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService extends BaseService {
    private final BrandRepository brandRepository;
    private final FileUploadService fileUploadService;

    public Brand findById(String id){
        return brandRepository.findById(id).orElse(null);
    }
    @Transactional(rollbackOn = Exception.class)
    public Brand addBrand(Brand brand, MultipartFile image){
        brandRepository.save(brand);

        String brandLogo = uploadBrandImages(brand.getBrandId(), image);
        brand.setBrandLogo(brandLogo);

        return brandRepository.save(brand);
    }

    public List<Brand> findAllBrand() {
        return brandRepository.findAll();
    }

    public String uploadBrandImages(String brandId, final MultipartFile image){
        return fileUploadService.uploadFile(image, "brand/", brandId + "_" + System.currentTimeMillis());
    }
}
