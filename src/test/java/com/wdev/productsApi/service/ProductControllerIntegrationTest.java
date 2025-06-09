package com.wdev.productsApi.service;

import com.jayway.jsonpath.JsonPath;
import com.wdev.productsApi.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    public void cleanUp() {
        productRepository.deleteAll();
    }

    private static final String TEST_PRODUCTS_JSON = "[{\"name\": \"teclado gamer\",\"price\": 190}," +
            " {\"name\": \"mousepad\",\"price\": 15}," +
            " {\"name\": \"monitor\",\"price\": 150}," +
            " {\"name\": \"headset\",\"price\": 70}]";

    @Test
    @DisplayName("Should create and delete a product")
    public void createProduct() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TEST_PRODUCTS_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idProduct").exists())
                .andExpect(jsonPath("$[0].name").value("teclado gamer"))
                .andExpect(jsonPath("$[0].price").value(190))
                .andReturn();

        String data = result.getResponse().getContentAsString();
        String idProduct = JsonPath.read(data, "$[0].idProduct");

        mockMvc.perform(delete("/api/products/{id}", idProduct)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"successfully deleted\"}"));
    }

    @Test
    @DisplayName("Should return an error when trying to create a product with invalid data")
    public void createProductError() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"name\": \"chapeu\"}]"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"message\": \"Oops! An unexpected error occurred.\" }"));
    }

    @Test
    @DisplayName("Should return all products")
    public void returnAllProducts() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TEST_PRODUCTS_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idProduct").exists())
                .andExpect(jsonPath("$[0].name").value("teclado gamer"))
                .andExpect(jsonPath("$[0].price").value(190))
                .andExpect(jsonPath("$[1].idProduct").exists())
                .andExpect(jsonPath("$[1].name").value("mousepad"))
                .andExpect(jsonPath("$[1].price").value(15))
                .andExpect(jsonPath("$[2].idProduct").exists())
                .andExpect(jsonPath("$[2].name").value("monitor"))
                .andExpect(jsonPath("$[2].price").value(150))
                .andExpect(jsonPath("$[3].idProduct").exists())
                .andExpect(jsonPath("$[3].name").value("headset"))
                .andExpect(jsonPath("$[3].price").value(70))
                .andReturn();

        mockMvc.perform(get("/api/products")
                        .param("page", "1")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @DisplayName("Should return an error when trying to return a product page empty")
    public void returnProductPageEmpty() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("page", "999")
                        .param("size", "3"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"Product not found\"}"));
    }

    @Test
    @DisplayName("Should return a message when delete product")
    public void deleteProduct() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TEST_PRODUCTS_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].idProduct").exists())
                .andExpect(jsonPath("$[0].name").value("teclado gamer"))
                .andExpect(jsonPath("$[0].price").value(190))
                .andReturn();

        String data = mvcResult.getResponse().getContentAsString();
        String id = JsonPath.read(data, "$[0].idProduct");

        mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"successfully deleted\"}"));
    }
}
