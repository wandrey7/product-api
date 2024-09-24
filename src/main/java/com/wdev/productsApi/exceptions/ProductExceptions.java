package com.wdev.productsApi.exceptions;


public class ProductExceptions {
    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException() {
            super("This ID is not associated with any product");
        }

    }

    public static class ProductEmptyException extends RuntimeException {
        public ProductEmptyException() {
            super("Product not found");
        }
    }
}




