package com.example.productorder.Product;

import com.example.productorder.Product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Transactional
    public boolean updateStock(Long productId, int quantity) {
        Product product = productRepository.findByIdForUpdate(productId);
        if (product.getStock() < quantity) {
            return false;
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        return true;
    }
}