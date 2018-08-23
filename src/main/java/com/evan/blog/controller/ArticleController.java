package com.evan.blog.controller;

import com.evan.blog.pojo.BlogJSONResult;
import com.evan.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "apis/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @GetMapping(path = "/{articleId}")
    private BlogJSONResult getArticle(@PathVariable Long articleId) {
        articleService.getArticle(articleId.intValue());
        return BlogJSONResult.ok();
    }
}
