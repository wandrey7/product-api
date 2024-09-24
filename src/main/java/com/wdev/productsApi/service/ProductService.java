package com.wdev.productsApi.service;

import com.wdev.productsApi.exceptions.ProductExceptions;
import com.wdev.productsApi.model.ProductModel;
import com.wdev.productsApi.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductModel> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<ProductModel> findProductById(UUID id) throws Exception {
        Optional<ProductModel> fBId = productRepository.findById(id);
        if (fBId.isEmpty()) throw new ProductExceptions.ProductNotFoundException();
        return fBId;
    }

    public List<ProductModel> createProducts(List<ProductModel> productModels) {
        return productRepository.saveAll(productModels);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
