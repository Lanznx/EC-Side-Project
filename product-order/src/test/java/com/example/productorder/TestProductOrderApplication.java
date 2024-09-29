package com.example.productorder;

import org.springframework.boot.SpringApplication;

public class TestProductOrderApplication {

  public static void main(String[] args) {
    SpringApplication.from(ProductOrderApplication::main).with(TestcontainersConfiguration.class)
        .run(args);
  }

}
