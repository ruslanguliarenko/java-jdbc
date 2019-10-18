package com;



import com.jdbc.CsvConverter;

import com.jdbc.service.MessageService;
import com.jdbc.sql.ConnectionFactory;
import com.jdbc.dao.ProductDao;
import com.jdbc.model.Category;
import com.jdbc.model.Product;
import com.jdbc.service.ProductService;


import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.util.HashSet;
import java.util.Set;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory("jdbc:mysql://localhost:3306/shop_db", "root", "1234");
        ProductDao productDao = new ProductDao(connectionFactory);



        Category category = new Category();
        category.setId(1);
        category.setName("c");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("c_2");

        Product product = new Product();
        product.setId(1);
        product.setName("p");
        product.setDescription("p");
        product.setPrice(12);
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        categories.add(category2);
        product.setCategories(categories);CsvConverter csvConverter = new CsvConverter(connectionFactory);
        csvConverter.readFromDB( "D:\\IntellijProjects\\bd\\yourfile.csv", "categories");

        MessageService.sendMessage();
    }



}
