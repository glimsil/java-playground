package br.com.poc.simpleapi.service;

import br.com.poc.simpleapi.api.dto.ProductDto;
import br.com.poc.simpleapi.repository.Product;
import br.com.poc.simpleapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public Iterable<Product> find() {
        return productRepository.findAll();
    }

    public Optional<Product> find(Integer id) {
        return productRepository.findById(id);
    }

    public Product save(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        return productRepository.save(product);
    }
}
