package com.example.productorder.Product;

import com.example.productorder.Product.entity.Product;
import com.example.productorder.Product.entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    public Product getProductById(UUID productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public Product saveProduct(CreateProductRequest request) {
        Product product = new Product()
            .setProductId(request.getProductId())
            .setName(request.getName())
            .setDescription(request.getDescription())
            .setPrice(request.getPrice());


        Stock stock = new Stock()
            .setProduct(product)
            .setQuantity(request.getStockQuantity());
        productRepository.save(product);
        stockRepository.save(stock);
        return product;
    }

    @Transactional
    public boolean updateStock(UUID productId, int quantity) {
        Stock stock = stockRepository.findByProductId(productId);
        if (stock.getQuantity() < quantity) {
            return false;
        }
        stock.setQuantity(stock.getQuantity() - quantity);
        stockRepository.save(stock);
        return true;
    }
}