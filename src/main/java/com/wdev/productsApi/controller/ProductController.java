package com.wdev.productsApi.controller;

import com.wdev.productsApi.DTO.ProductDTO;
import com.wdev.productsApi.exceptions.ProductExceptions;
import com.wdev.productsApi.model.ProductModel;
import com.wdev.productsApi.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/api/products")
    public ResponseEntity<Object> getAllProducts(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            Pageable pageable = PageRequest.of(page - 1, size); // baseado em 0, ajuste indice
            Page<ProductModel> productPage = productService.findAllProducts(pageable);
            if (productPage.isEmpty()) throw new ProductExceptions.ProductEmptyException();
            return ResponseEntity.status(HttpStatus.OK).body(productPage.getContent());
        } catch (ProductExceptions.ProductEmptyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred when searching for all products"));
        }
    }

    @GetMapping("/api/products/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable("id") UUID productId) {
        try {
            Optional<ProductModel> productData = productService.findProductById(productId);
            return ResponseEntity.status(HttpStatus.OK).body(productData);
        } catch (ProductExceptions.ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred when searching for product by id"));
        }
    }

    @PostMapping("/api/products")
    public ResponseEntity<Object> saveProduct(@RequestBody @Valid List<ProductDTO> productDTOs) {
        try {
            List<ProductModel> productModels = productDTOs.stream()
                    .map(productDTO -> {
                        ProductModel productModel = new ProductModel();
                        BeanUtils.copyProperties(productDTO, productModel);
                        return productModel;
                    })
                    .collect(Collectors.toList());
            List<ProductModel> savedProducts = productService.createProducts(productModels);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProducts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred when creating new products"));
        }
    }


    @PutMapping("/api/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") UUID productId,
                                                @RequestBody @Valid ProductDTO productDTO) {
        try {
            Optional<ProductModel> product = productService.findProductById(productId);
            ProductModel productModel = product.get();
            BeanUtils.copyProperties(productDTO, productModel);
            return ResponseEntity.status(HttpStatus.OK).body(productService.createProducts(Collections.singletonList(productModel)));
        } catch (ProductExceptions.ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred when update product"));
        }
    }

    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") UUID productId) {
        try {
            Optional<ProductModel> product = productService.findProductById(productId);
            productService.deleteProduct(productId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "successfully deleted"));
        } catch (ProductExceptions.ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while retrieving the product"));
        }
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
        response.put("github", "https://github.com/wandrey7/productsAPI");

        return response;
    }
}


