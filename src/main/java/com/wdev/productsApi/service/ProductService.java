package com.wdev.productsApi.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wdev.productsApi.DTO.ProductDTO;
import com.wdev.productsApi.Mapper.ProductMapper;
import com.wdev.productsApi.exceptions.ProductExceptions;
import com.wdev.productsApi.model.ProductModel;
import com.wdev.productsApi.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public Page<ProductModel> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public ProductModel findProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(ProductExceptions.ProductNotFoundException::new);
    }

    public List<ProductModel> createProducts(List<ProductModel> productModels) {
        return productRepository.saveAll(productModels);
    }

    @Transactional
    public ProductModel update(UUID id, ProductDTO productDTO)  {
        ProductModel existingProduct = this.findProductById(id);

        ProductModel updatedProduct = productMapper.toEntity(productDTO);
      
        updatedProduct.setIdProduct(existingProduct.getIdProduct());

        return productRepository.save(updatedProduct);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
