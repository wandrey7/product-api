package com.wdev.productsApi.service;

import com.jayway.jsonpath.JsonPath;
import com.wdev.productsApi.repository.ProductRepository;
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
public class ProductServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("Shold return a create and delete product")
    public void createProduct() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"name\": \"tecladogamer\",\"value\": 190}]"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idProduct").exists())
                .andExpect(jsonPath("$[0].name").value("tecladogamer"))
                .andExpect(jsonPath("$[0].value").value(190))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String idProduct = JsonPath.read(content, "$[0].idProduct");

        mockMvc.perform(delete("/api/products/{id}", idProduct)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"successfully deleted\"}"));
    }

    @Test
    @DisplayName("Shold return error a create product")
    public void createProductError() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"name\": \"chapeu\"}]"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"message\": \"An unexpected error occurred\" }"));
    }

    @Test
    @DisplayName("Shold return all products")
    public void returnAllProducts() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"name\": \"tecladogamer\",\"value\": 190}, {\"name\": \"mousepad\",\"value\": 15}, {\"name\": \"monitor\",\"value\": 150}, {\"name\": \"headset\",\"value\": 70}]")
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idProduct").exists())
                .andExpect(jsonPath("$[0].name").value("tecladogamer"))
                .andExpect(jsonPath("$[0].value").value(190))
                .andReturn();

        String contents = result.getResponse().getContentAsString();

        mockMvc.perform(get("/api/products")
                        .param("page", "1")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));

        for (int i = 0; i < 4; i++) {
            String id = JsonPath.read(contents, "$[" + i + "].idProduct");
            mockMvc.perform(delete("/api/products/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"message\": \"successfully deleted\"}"));
        }
    }

    @Test
    @DisplayName("Return error a product page empty")
    public void returnProductPageEmpty() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("page", "999")
                        .param("size", "3"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"Product not found\"}"));
    }

    @Test
    @DisplayName("Shold delete product")
    public void deleteProduct() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"name\": \"MouseM600\", \"value\": 19}]"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].idProduct").exists())
                .andExpect(jsonPath("$[0].name").value("MouseM600"))
                .andExpect(jsonPath("$[0].value").value(19))
                .andReturn();

        String data = mvcResult.getResponse().getContentAsString();
        String id = JsonPath.read(data, "$[0].idProduct");

        mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"successfully deleted\"}"));
    }
}
