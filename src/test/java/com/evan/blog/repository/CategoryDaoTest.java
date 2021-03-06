package com.evan.blog.repository;

import com.evan.blog.model.Category;
import com.evan.blog.model.QueryFilter;
import com.evan.blog.model.enums.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryDaoTest {

    @Autowired
    private CategoryDao categoryDao;

//    @Test
//    public void insertCategory() {
//        Category category = new Category("分布式");
//        categoryDao.insertCategory(category);
//        assertNotNull(category.getId());
//    }

    @Test
    public void selectCategoryById() {
        Category category = categoryDao.selectCategoryById(2);
        System.out.println(category);
        assertEquals("Java", category.getName());
    }

    @Test
    public void selectCategoryNameById() {
        String categoryName = categoryDao.selectCategoryNameById(2);
        assertEquals("Java", categoryName);
    }

    @Test
    public void selectCategoriesByName() {
        List<Category> categories = categoryDao.selectCategoriesByName("Type");
        assertFalse(categories.isEmpty());
    }

    @Test
    public void selectCategories() {
        List<Category> categories = categoryDao.selectCategories(new QueryFilter("created_time", Order.Desc));
        Category c1 = categories.get(0);
        Category c2 = categories.get(1);
        int r = c1.getCreatedTime().compareTo(c2.getCreatedTime());
        assertTrue(r > 0);
    }

    @Test
    public void selectCategoriesOrderByIncludedArticles() {
        List<Category> categories = categoryDao.selectCategoriesOrderByIncludedArticles();
        System.out.println(categories);
    }

//    @Test
//    public void deleteCategory() {
//        categoryDao.deleteCategory(5);
//        Category category = categoryDao.selectCategoryById(5);
//        assertNull(category);
//    }
}