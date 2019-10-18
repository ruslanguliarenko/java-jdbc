package com.jdbc.service;

import com.jdbc.dao.CategoryDao;
import com.jdbc.model.Category;

public class CategoryService {
    private CategoryDao categoryDao;

    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public void setCategory(Category category, Category category2){
        categoryDao.beginTx();
        categoryDao.save(category);
        categoryDao.save(category2);
        categoryDao.endTx();
        categoryDao.save(category2);
    }

}
