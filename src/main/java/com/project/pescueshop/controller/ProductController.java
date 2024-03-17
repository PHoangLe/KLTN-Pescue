package com.project.pescueshop.controller;

import com.project.pescueshop.model.dto.*;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.*;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.BrandService;
import com.project.pescueshop.service.CategoryService;
import com.project.pescueshop.service.ProductService;
import com.project.pescueshop.service.VarietyService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@CrossOrigin
@Api
public class ProductController {
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductService productService;
    private final VarietyService varietyService;

    //<editor-fold desc="Category">
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/category")
    public ResponseEntity<ResponseDTO<Category>> addCategory(@RequestBody Category category) throws FriendlyException {
        category = categoryService.addCategory(category);

        ResponseDTO<Category> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, category);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/category")
    public ResponseEntity<ResponseDTO<List<Category>>> findAllCategory() throws FriendlyException {
        List<Category> categoryList = categoryService.findAllCategory();

        ResponseDTO<List<Category>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, categoryList, "categoryList");

        return ResponseEntity.ok(result);
    }
    //</editor-fold>

    //<editor-fold desc="SubCategory">
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/sub-category")
    public ResponseEntity<ResponseDTO<SubCategory>> addSubCategory(@RequestBody SubCategory subCategory) throws FriendlyException {
        subCategory = categoryService.addSubCategory(subCategory);

        ResponseDTO<SubCategory> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, subCategory);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/sub-category")
    public ResponseEntity<ResponseDTO<List<SubCategory>>> findAllSubCategory() throws FriendlyException {
        List<SubCategory> categoryList = categoryService.findAllSubCategory();

        ResponseDTO<List<SubCategory>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, categoryList, "subCategoryList");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/sub-category/{categoryId}")
    public ResponseEntity<ResponseDTO<List<SubCategory>>> findAllSubCategoryByCategoryId(@PathVariable String categoryId){
        List<SubCategory> subCategoryList = categoryService.findAllSubCategoryByCategoryId(categoryId);

        ResponseDTO<List<SubCategory>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, subCategoryList, "subCategoryList");

        return ResponseEntity.ok(result);
    }
    //</editor-fold>

    //<editor-fold desc="Brand">
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/brand")
    public ResponseEntity<ResponseDTO<Brand>> addBrand(@RequestPart Brand brand, @RequestPart("image") MultipartFile image) throws FriendlyException {
        brand = brandService.addBrand(brand, image);

        ResponseDTO<Brand> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, brand);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/brand")
    public ResponseEntity<ResponseDTO<List<Brand>>> findAllBrand() throws FriendlyException {
        List<Brand> categoryList = brandService.findAllBrand();

        ResponseDTO<List<Brand>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, categoryList, "brandList");

        return ResponseEntity.ok(result);
    }
    //</editor-fold>

    //<editor-fold desc="Variety">
    @GetMapping("/variety/attributes")
    public ResponseEntity<ResponseDTO<Map<String, List<VarietyAttribute>>>> findAllAttribute() throws FriendlyException {
        Map<String, List<VarietyAttribute>> varietyAttribute = varietyService.findAllVarietyAttribute();

        ResponseDTO<Map<String, List<VarietyAttribute>>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, varietyAttribute, "productList");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/variety")
    public ResponseEntity<ResponseDTO<VarietyDTO>> addVariety(@RequestBody VarietyDTO dto) throws FriendlyException {
        VarietyDTO varietyDTO = varietyService.addOrUpdateVariety(dto);
        ResponseDTO<VarietyDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, varietyDTO, "product");
        return ResponseEntity.ok(result);
    }
    //</editor-fold>

    //<editor-fold desc="Product">
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    public ResponseEntity<ResponseDTO<ProductDTO>> addProduct(@RequestPart ProductDTO productDTO, @RequestPart("images") MultipartFile[] images) throws InterruptedException {
        productDTO = productService.addProduct(productDTO, images);

        ResponseDTO<ProductDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, productDTO);
        System.out.println(System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }

    @PutMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<ProductDTO>> updateProduct(@RequestBody ProductDTO productDTO) throws FriendlyException {
        productDTO = productService.updateProduct(productDTO);

        ResponseDTO<ProductDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, productDTO);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/product-images/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<List<String>>> updateProductImage(@RequestPart("newImages") List<MultipartFile> newImages, @RequestPart List<String> deletedImages, @PathVariable String productId) throws FriendlyException {
        List<String> imagesUrl = productService.updateProductImage(productId, newImages, deletedImages);
        ResponseDTO<List<String>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, imagesUrl, "images");

        return ResponseEntity.ok(result);
    }

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<Product>>> findAllProduct() {
        List<Product> productList = productService.findAllProduct();

        ResponseDTO<List<Product>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, productList, "productList");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/random")
    public ResponseEntity<ResponseDTO<List<Product>>> getNProducts(@RequestParam Integer quantity) {
        List<Product> productList = productService.getNRandomProducts(quantity);

        ResponseDTO<List<Product>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, productList, "productList");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/most-views")
    public ResponseEntity<ResponseDTO<List<ProductDashboardResult>>> getMostViewsProduct(@RequestParam(required = false) Integer quantity, @RequestParam(required = false) Long daysAmount) {
        List<ProductDashboardResult> productList = productService.getMostViewsProducts(quantity, daysAmount);

        ResponseDTO<List<ProductDashboardResult>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, productList, "productList");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/most-buy")
    public ResponseEntity<ResponseDTO<List<ProductDashboardResult>>> getMostBuyProduct(@RequestParam(required = false) Integer quantity, @RequestParam(required = false) Long daysAmount) {
        List<ProductDashboardResult> productList = productService.getMostBuyProducts(quantity, daysAmount);

        ResponseDTO<List<ProductDashboardResult>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, productList, "productList");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ResponseDTO<List<Product>>> getByCategoryId(@PathVariable String categoryId) {
        List<Product> productList = productService.getProductByCategoryId(categoryId);

        ResponseDTO<List<Product>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, productList, "productList");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/brands/{brandId}")
    public ResponseEntity<ResponseDTO<List<Product>>> getByBrandId(@PathVariable String brandId) {
        List<Product> productList = productService.getProductByBrandId(brandId);

        ResponseDTO<List<Product>> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, productList, "productList");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ResponseDTO<ProductDTO>> getProductDetail(@PathVariable String productId) {
        ProductDTO dto = productService.getProductDetail(productId);

        ResponseDTO<ProductDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS, dto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/add-attribute/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<ProductDTO>> addAttribute(@RequestBody VarietyAttribute attribute, @PathVariable String productId) throws FriendlyException, InterruptedException {
        productService.addVarietyAttribute(attribute, productId);
        ResponseDTO<ProductDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete-attribute/{productId}/{attributeId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<ProductDTO>> deleteAttribute(@PathVariable String attributeId, @PathVariable String productId) {
        productService.deleteAttribute(attributeId, productId);
        ResponseDTO<ProductDTO> result = new ResponseDTO<>(EnumResponseCode.SUCCESS);

        return ResponseEntity.ok(result);
    }

    //</editor-fold>
}
