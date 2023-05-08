package com.example.lisam.controller;

import com.example.lisam.entity.Comment;
import com.example.lisam.entity.Item;
import com.example.lisam.repository.CommentRepository;
import com.example.lisam.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;


    @GetMapping("/comments/add")
    public String addCommentPage(ModelMap modelMap) {
//        List<Item> items = itemRepository.findAll();
//        modelMap.addAttribute("items", items);
        return "singleItem";
    }

    @PostMapping("/comments/add")
    public String addComment(@RequestParam("comment") String comments, @RequestParam("id") int id) {
        Comment comment = new Comment();
        comment.setComment(comments);
        Optional<Item> byId = itemRepository.findById(id);
        if (byId.isPresent()) {
            Item item = byId.get();
            comment.setItem(item);
            commentRepository.save(comment);
        }
        return "redirect:/items/" + id;
    }

    @GetMapping("/comments/remove")
    public String removeComment(@RequestParam("id") int id) {
        commentRepository.deleteById(id);
        return "redirect:/items";
    }
}
