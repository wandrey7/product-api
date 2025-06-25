package com.wdev.productsApi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wdev.productsApi.DTO.ProductDTO;
import com.wdev.productsApi.DTO.ProductResponseDTO;
import com.wdev.productsApi.Mapper.ProductMapper;
import com.wdev.productsApi.exceptions.ProductExceptions;
import com.wdev.productsApi.model.ProductModel;
import com.wdev.productsApi.service.ProductService;

import jakarta.validation.Valid;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    ProductMapper productMapper;

    @GetMapping("/api/products")
    public ResponseEntity<Object> getAllProducts(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            Pageable pageable = PageRequest.of(page - 1, size); // baseado em 0, ajuste indice
            Page<ProductModel> productPage = productService.findAllProducts(pageable);
            if (productPage.isEmpty()) throw new ProductExceptions.ProductEmptyException();
            List<ProductResponseDTO> responseDTOs = productMapper.toResponseDtoList(productPage.getContent());
            return ResponseEntity.status(HttpStatus.OK).body(responseDTOs);
    }

    @GetMapping("/api/products/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable("id") UUID productId) {
            ProductModel productModel = productService.findProductById(productId);
            ProductResponseDTO dto = productMapper.toResponseDTO(productModel);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("/api/products")
    public ResponseEntity<Object> saveProduct(@RequestBody @Valid List<ProductDTO> productDTOs) {
            List<ProductModel> productModels = productMapper.toEntityList(productDTOs);
            List<ProductModel> savedProducts = productService.createProducts(productModels);
            List<ProductResponseDTO> savedDTOs = productMapper.toResponseDtoList(savedProducts);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDTOs);
    }


    @PutMapping("/api/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") UUID productId,
                                                @RequestBody @Valid ProductDTO productDTO) {
            ProductModel updateModel = productService.update(productId, productDTO);
            ProductResponseDTO responseDTO = productMapper.toResponseDTO(updateModel);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") UUID productId) {
            ProductModel productModel = productService.findProductById(productId);
            productService.deleteProduct(productModel.getIdProduct());
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "successfully deleted"));
    }

    @GetMapping("/api")
    public Map<String, Object> getApiInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello from the API!");

        Map<String, String> routes = new HashMap<>();
        routes.put("GET /api/products", "Lista todos os produtos cadastrados.");
        routes.put("GET /api/products/{id}", "Retorna os detalhes de um produto específico pelo ID.");
        routes.put("POST /api/products", "Cadastra um novo usuário.");
        routes.put("PUT /api/products/{id}", "Atualiza os dados de um produto específico pelo ID.");
        routes.put("DELETE /api/products/{id}", "Remove um produto específico pelo ID.");

        response.put("availableRoutes", routes);
        response.put("github", "https://github.com/wandrey7/product-api");

        return response;
    }
}


