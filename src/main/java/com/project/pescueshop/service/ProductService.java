package com.project.pescueshop.service;

import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.project.pescueshop.model.dto.ProductDTO;
import com.project.pescueshop.model.dto.ProductDashboardResult;
import com.project.pescueshop.model.dto.ProductListDTO;
import com.project.pescueshop.model.elastic.ElasticClient;
import com.project.pescueshop.model.elastic.document.ProductData;
import com.project.pescueshop.model.elastic.document.ViewAuditLogData;
import com.project.pescueshop.model.entity.*;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.ProductDAO;
import com.project.pescueshop.repository.dao.VarietyAttributeDAO;
import com.project.pescueshop.util.constant.EnumElasticIndex;
import com.project.pescueshop.util.constant.EnumPetType;
import com.project.pescueshop.util.constant.EnumResponseCode;
import com.project.pescueshop.util.constant.EnumStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService extends BaseService {
    private final VarietyService varietyService;
    private final FileUploadService fileUploadService;
    private final VarietyAttributeDAO varietyAttributeDAO;
    private final ProductDAO productDAO;
    private final ThreadService threadService;

    public ProductDTO transformProductToDTO(Product product){
        return ProductDTO.builder()
                .avgRating(product.getAvgRating())
                .brand(product.getBrand())
                .description(product.getDescription())
                .detail(product.getDetail())
                .images(product.getImages())
                .length(product.getLength())
                .height(product.getHeight())
                .name(product.getName())
                .petType(product.getPetType())
                .price(product.getPrice())
                .productId(product.getProductId())
                .status(product.getStatus())
                .subCategory(product.getSubCategory())
                .weight(product.getWeight())
                .merchantId(product.getMerchantId())
                .build();
    }

    public Product findById(String id){
        return productDAO.findProductById(id);
    }

    public ProductDTO getProductDetail(String productId, String viewerId){
        Product product = findById(productId);
        ProductDTO dto = transformProductToDTO(product);

        threadService.retrieveExternalInfoForProductDTO(dto, viewerId);

        return dto;
    }

    public ProductDTO addProduct(ProductDTO productDTO, MultipartFile[] images, boolean isSameMeasurement) {
        List<MultipartFile> imagesList = Arrays.stream(images).toList();
        EnumPetType petType = EnumPetType.getById(productDTO.getPetTypeId());
        productDTO.setPetType(petType.getValue());
        productDTO.setStatus(EnumStatus.ACTIVE.getValue());

        Product product = new Product(productDTO);
        productDAO.saveAndFlushProduct(product);

        List<String> imagesUrl = uploadProductImages(product.getProductId(), imagesList);
        product.setImages(imagesUrl);
        productDAO.saveAndFlushProduct(product);

        CompletableFuture.runAsync(() -> {
            try {
                addDefaultVariety(product, productDTO.getVarietyAttributeList(), isSameMeasurement);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            productDAO.pushOrUpdateProductToElasticSearch(product);
        });

        return transformProductToDTO(product);
    }

    private void addDefaultVariety(Product product, List<VarietyAttribute> varietyAttributeList, boolean isSameMeasurement) throws InterruptedException {
        if (product == null)
            return;

        if (CollectionUtils.isEmpty(varietyAttributeList)) {
            Variety variety = new Variety();
            variety.setProductId(product.getProductId());
            variety.setName(product.getName());
            variety.setPrice(product.getPrice());
            variety.setStatus(product.getStatus());
            variety.setMerchantId(product.getMerchantId());
            variety.setStockAmount(0);

            if (isSameMeasurement) {
                variety.setWidth(product.getWidth());
                variety.setHeight(product.getHeight());
                variety.setLength(product.getLength());
                variety.setWeight(product.getWeight());
            }

            varietyService.addOrUpdateVariety(variety);
        }
        else {
            Map<String, List<VarietyAttribute>> varietiesAttributeMap = varietyAttributeList.stream()
                    .collect(Collectors.groupingBy(VarietyAttribute::getType));

            List<VarietyAttribute> sizeAttribute = varietiesAttributeMap.getOrDefault("SIZE", new ArrayList<>());
            List<VarietyAttribute> colorAttribute = varietiesAttributeMap.getOrDefault("COLOR", new ArrayList<>());

            varietyService.addVarietyByListAttribute(product, sizeAttribute, colorAttribute, isSameMeasurement);
        }

    }

    public List<String> uploadProductImages(String productId, final List<MultipartFile> images){
        List<String> imagesUrl = new ArrayList<>();
        images.forEach(image -> imagesUrl.add(fileUploadService.uploadFile(image, "product/", productId)));

        return imagesUrl;
    }

    public List<VarietyAttribute> getAllExistedAttributeByProductId(String productId, String type){
        return varietyAttributeDAO.getAllExistedAttributeByProductId(productId, type);
    }

    public void addVarietyAttribute(VarietyAttribute newAttribute, String productId) throws FriendlyException {
        Product product = findById(productId);
        if (product == null)
        {
            throw new FriendlyException(EnumResponseCode.PRODUCT_NOT_FOUND);
        }

        List<Variety> varieties = varietyService.findByProductId(productId);

        boolean isExisted = varieties.stream()
                .anyMatch(variety -> variety.getVarietyAttributes().stream()
                        .anyMatch(varietyAttribute -> Objects.equals(varietyAttribute.getAttributeId(), newAttribute.getAttributeId())));

        if (isExisted){
            throw new FriendlyException(EnumResponseCode.ATTRIBUTE_EXISTED);
        }

        List<VarietyAttribute> varietyAttributeList = getAllExistedAttributeByProductId(productId, newAttribute.getType());
        if (CollectionUtils.isEmpty(varietyAttributeList))
        {
            Variety variety = new Variety();
            variety.addAttribute(newAttribute);
            variety.setProductId(productId);
            variety.setStatus(EnumStatus.ACTIVE.getValue());
            variety.setStockAmount(0);
            variety.setPrice(product.getPrice());

            variety = varietyService.addOrUpdateVariety(variety);
            varieties.add(variety);
        }
        else {
            threadService.addVarietyByAttribute(product, varietyAttributeList, newAttribute, false);
        }
    }

    public ProductDTO updateProduct(ProductDTO dto) throws FriendlyException {
        Product product = findById(dto.getProductId());
        if (product == null){
            throw new FriendlyException(EnumResponseCode.PRODUCT_NOT_FOUND);
        }

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setBrand(dto.getBrand());
        product.setSubCategory(dto.getSubCategory());
        product.setDescription(dto.getDescription());
        product.setDetail(dto.getDetail());
        product.setPetType(dto.getPetType());
        productDAO.saveAndFlushProduct(product);

        productDAO.pushOrUpdateProductToElasticSearch(product);

        return transformProductToDTO(product);
    }

    public List<String> updateProductImage(String productId, List<MultipartFile> newImages, List<String> deletedImages) throws FriendlyException {
        Product product = findById(productId);
        if (product == null){
            throw new FriendlyException(EnumResponseCode.PRODUCT_NOT_FOUND);
        }

        if (newImages != null && !newImages.isEmpty()){
            List<String> newImagesUrl = uploadProductImages(productId, newImages);
            addNewProductImages(productId, newImagesUrl);
        }

        if (deletedImages != null && !deletedImages.isEmpty()){
            removeProductImages(deletedImages);
        }

        CompletableFuture.runAsync(() -> {
            Product newProduct = findById(productId);
            productDAO.pushOrUpdateProductToElasticSearch(newProduct);
        });

        return product.getImages();
    }

    private void addNewProductImages(String productId, List<String> productImages){
        productDAO.addNewProductImages(productId, productImages);
    }

    private void removeProductImages(List<String> productImages){
        productDAO.removeProductImages(productImages);
    }

    public void deleteAttribute(String productId, String attributeId){
       productDAO.deleteAttribute(productId, attributeId);
    }

    public List<Product> getNRandomProducts(Integer n){
        return productDAO.getRandomNProduct(n);
    }

    public List<ProductDashboardResult> getMostViewsProducts(Integer n, Long daysAmount, String merchantId) {
        return productDAO.getMostViewsProducts(n, daysAmount, merchantId);
    }

    public List<ProductDashboardResult> getMostBuyProducts(Integer n, Long daysAmount, String merchantId) {
        return productDAO.getMostBuyProducts(n, daysAmount, merchantId);
    }

    public List<Product> getProductByBrandId(String brandId) {
        return productDAO.getProductByBrandId(brandId);
    }

    public List<Product> getProductByCategoryId(String categoryId) {
        return productDAO.getProductByCategoryId(categoryId);
    }

    public List<ProductListDTO> getListProduct(String categoryId, String subCategoryId, String brandId, String merchantId, Long minPrice, Long maxPrice, Integer page, Integer size){
        return productDAO.getListProduct(categoryId, subCategoryId, brandId, merchantId, minPrice, maxPrice, page, size);
    }
}
