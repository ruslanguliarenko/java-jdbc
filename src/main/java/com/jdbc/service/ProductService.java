package com.jdbc.service;


import com.jdbc.CsvConverter;
import com.jdbc.dao.ProductDao;
import com.jdbc.model.Product;
import com.opencsv.bean.CsvToBeanBuilder;

import java.util.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class ProductService{
    private ProductDao productDao;
    private CsvConverter csvConverter;
    public ProductService(ProductDao productDao, CsvConverter csvConverter){
        this.csvConverter = csvConverter;
        this.productDao = productDao;
    }

    public void addProduct(File file) throws SQLException {
        csvConverter.saveInDB(file.getPath(), "products");
    }

}