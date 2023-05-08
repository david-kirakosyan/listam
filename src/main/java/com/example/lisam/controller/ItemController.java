package com.example.lisam.controller;

import com.example.lisam.entity.Category;
import com.example.lisam.entity.Comment;
import com.example.lisam.entity.Item;
import com.example.lisam.repository.CategoryRepository;
import com.example.lisam.repository.CommentRepository;
import com.example.lisam.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/items")
    public String ItemsPage(ModelMap modelMap) {
        List<Item> items = itemRepository.findAll();
        modelMap.addAttribute("items", items);
        return "items";
    }

    @GetMapping("/items/{id}")
    public String singleItemPage(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<Item> byId = itemRepository.findById(id);
        List<Comment> comments = commentRepository.findAll();
        if (byId.isPresent()) {
            Item item = byId.get();
            modelMap.addAttribute("item", item);
            modelMap.addAttribute("comments", comments);
            return "singleItem";
        } else {
            return "redirect:/items";
        }


    }

    @GetMapping("/items/add")
    public String addItemPage(ModelMap modelMap) {
        List<Category> categories = categoryRepository.findAll();
        modelMap.addAttribute("categories", categories);
        return "addItem";
    }

    @PostMapping("/items/add")
    public String addItem(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "redirect:/items";
    }

    @GetMapping("/items/remove")
    public String removeItem(@RequestParam("id") int id) {
        itemRepository.deleteById(id);
        return "redirect:/items";
    }


}
