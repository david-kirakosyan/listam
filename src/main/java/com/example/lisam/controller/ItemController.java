package com.example.lisam.controller;

import com.example.lisam.entity.*;
import com.example.lisam.repository.CategoryRepository;
import com.example.lisam.repository.CommentRepository;
import com.example.lisam.repository.HashtagRepository;
import com.example.lisam.repository.ItemRepository;
import com.example.lisam.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    @Autowired
    private HashtagRepository hashtagRepository;

    @Value("${listam.upload.image.path}")
    private String imageUploadPath;

    @GetMapping("/items")
    public String ItemsPage(ModelMap modelMap,
                            @AuthenticationPrincipal CurrentUser currentUser) {
        List<Item> items;
        if (currentUser.getUser().getUserType() == UserType.ADMIN) {
            items = itemRepository.findAll();
        } else {
            items = itemRepository.findAllByUser_id(currentUser.getUser().getId());
        }
        modelMap.addAttribute("items", items);
        return "items";
    }

    @GetMapping("/items/{id}")
    public String singleItemPage(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<Item> byId = itemRepository.findById(id);
        if (byId.isPresent()) {
            Item item = byId.get();
            List<Comment> comments = commentRepository.findAllByItem_id(item.getId());
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
        modelMap.addAttribute("hashtags", hashtagRepository.findAll());
        return "addItem";
    }

    @PostMapping("/items/add")
    public String addItem(@ModelAttribute Item item, @RequestParam("image") MultipartFile multipartFile,
                          @AuthenticationPrincipal CurrentUser currentUser) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = System.nanoTime() + "_" + multipartFile.getOriginalFilename();
            File file = new File(imageUploadPath + fileName);
            multipartFile.transferTo(file);
            item.setImgName(fileName);
        }
        item.setUser(currentUser.getUser());
//        if (tags != null && tags.isEmpty()){
//            List<Hashtag> hashtags = new ArrayList<>();
//            for (Integer tagId : tags) {
//                Optional<Hashtag> byId = hashtagRepository.findById(tagId);
//                if (byId.isPresent()){
//                    hashtags.add(byId.get());
//                }
//            }
//
//            item.setHashtagList(hashtags);
//        }
        itemRepository.save(item);
        return "redirect:/items";
    }

    @GetMapping("/items/remove")
    public String removeItem(@RequestParam("id") int id) {
        itemRepository.deleteById(id);
        return "redirect:/items";
    }
}
