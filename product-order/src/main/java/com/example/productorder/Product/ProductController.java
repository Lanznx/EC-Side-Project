package com.example.productorder.Product;

import com.example.productorder.Product.entity.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

class CreateProductRequest {
    private UUID productId;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;

    public UUID getProductId() {
        return productId;
    }

    public CreateProductRequest setProductId(UUID productId) {
        this.productId = productId;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateProductRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateProductRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public CreateProductRequest setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public CreateProductRequest setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
        return this;
    }
}

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable UUID id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody CreateProductRequest request) {
        return productService.saveProduct(request);
    }

}