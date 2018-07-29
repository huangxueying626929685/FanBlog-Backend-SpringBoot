package com.evan.blog.service;

import com.evan.blog.model.PublishedArticle;
import com.github.pagehelper.PageInfo;

public interface PublishedArticleService {
    PageInfo<PublishedArticle> getAllPublishedArticles(Integer pageIndex);
    PageInfo<PublishedArticle> getPublishedArticlesByCategoryId(Integer categoryId, Integer pageIndex);
    PublishedArticle getPublishedArticle(Integer pubId);
}
