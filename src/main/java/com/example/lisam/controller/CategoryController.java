package com.example.lisam.controller;

import com.example.lisam.entity.Category;
import com.example.lisam.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public String categoriesPage(ModelMap modelMap) {
        List<Category> categories = categoryRepository.findAll();
        modelMap.addAttribute("categories", categories);
        return "categories";
    }

    @GetMapping("/categories/add")
    public String addCategoryPage() {
        return "addCategory";
    }

    @PostMapping("/categories/add")
    public String addCategory(@RequestParam("name") String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return "redirect:/categories";
    }
    @GetMapping("/categories/remove")
    public String removeCategory(@RequestParam("id") int id){
        categoryRepository.deleteById(id);
        return "redirect:/categories";
    }
}
