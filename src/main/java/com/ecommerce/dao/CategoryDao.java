package com.ecommerce.dao;

import com.ecommerce.bean.Category;
import com.ecommerce.bean.Item;
import java.util.List;

public interface CategoryDao {

    public boolean updateCategory(Category category);
    
    public List<Item> getCategoryItems(long id, String sort);

    public boolean addCategory(Category category);

    public boolean deleteCategory(long id);

    public List<Category> getAllSupCategories(String sort);
    
    public List<Category> getAllSubCategories(String sort);

    public Category getCategoryById(long id);

    public int getNumCategories();

    public List<Category> getLatestCategories(int num);
}
