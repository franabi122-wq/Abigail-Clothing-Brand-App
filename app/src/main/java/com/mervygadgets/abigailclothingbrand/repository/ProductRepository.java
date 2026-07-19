package com.mervygadgets.abigailclothingbrand.repository;

import com.mervygadgets.abigailclothingbrand.models.Product;
import java.util.ArrayList;
import java.util.List;
public class ProductRepository {
    private static  ProductRepository instance;
    private List<Product> allProducts = new ArrayList<>();

    private ProductRepository() {}

    public static synchronized  ProductRepository getInstance(){
        if (instance == null) instance = new ProductRepository();
        return instance;
    }

    public  void setAllProducts(List<Product> products){
        this.allProducts = products;
    }
    public List<Product> getAllProducts(){
        return allProducts;
    }
}
