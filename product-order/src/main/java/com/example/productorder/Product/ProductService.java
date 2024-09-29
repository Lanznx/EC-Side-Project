package com.example.productorder.Product;

import com.example.productorder.Product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product getProductById(UUID productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public boolean updateStock(UUID productId, int quantity) {
        Product product = productRepository.findByIdForUpdate(productId);
        if (product.getStock() < quantity) {
            return false;
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        return true;
    }
}