package com.example.productorder.Product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
public class Product {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID productId;

  private String name;
  private Double price;
  private String description;

  public UUID getProductId() {
    return productId;
  }

  public Product setProductId(UUID productId) {
    this.productId = productId;
    return this;
  }

  public String getName() {
    return name;
  }

  public Product setName(String name) {
    this.name = name;
    return this;
  }

  public Double getPrice() {
    return price;
  }

  public Product setPrice(Double price) {
    this.price = price;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Product setDescription(String description) {
    this.description = description;
    return this;
  }
}