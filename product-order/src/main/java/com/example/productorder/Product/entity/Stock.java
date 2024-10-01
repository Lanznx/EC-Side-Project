package com.example.productorder.Product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
public class Stock {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID stockId;

    @ManyToOne
    private Product product;

    private Integer quantity;

    public UUID getStockId() {
        return stockId;
    }

    public Stock setStockId(UUID stockId) {
        this.stockId = stockId;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public Stock setProduct(Product product) {
        this.product = product;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Stock setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

}