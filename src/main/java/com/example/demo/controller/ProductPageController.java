package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class ProductPageController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String products(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "q", required = false) String q,
            Model model
    ) {
        List<Product> products;

        boolean hasCategory = category != null && !category.trim().isEmpty();
        boolean hasQ = q != null && !q.trim().isEmpty();

        if (hasCategory) {
            products = productService.findByCategory(category.trim());
        } else {
            products = productService.findAll();
        }

        if (hasQ) {
            String keyword = q.trim().toLowerCase(Locale.ROOT);
            products = products.stream()
                    .filter(p -> p.getName() != null && p.getName().toLowerCase(Locale.ROOT).contains(keyword))
                    .collect(Collectors.toList());
        }

        model.addAttribute("products", products);
        model.addAttribute("category", hasCategory ? category.trim() : "");
        model.addAttribute("q", hasQ ? q.trim() : "");

        return "products";
    }
}