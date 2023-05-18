package com.example.lisam.controller;

import com.example.lisam.entity.Comment;
import com.example.lisam.repository.CommentRepository;
import com.example.lisam.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;



    @PostMapping("/comments/add")
    public String addComment(@ModelAttribute Comment comment, @AuthenticationPrincipal CurrentUser currentUser) {
        comment.setCommentDate(new Date());
        comment.setUser(currentUser.getUser());
        commentRepository.save(comment);
        return "redirect:/items/" + comment.getItem().getId();
    }

    @GetMapping("/comments/remove")
    public String removeComment(@RequestParam("id") int id) {
        commentRepository.deleteById(id);
        return "redirect:/items";
    }
}
