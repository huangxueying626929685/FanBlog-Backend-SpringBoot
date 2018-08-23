package com.evan.blog.service;

import com.evan.blog.model.Draft;
import com.evan.blog.model.Category;
import com.evan.blog.pojo.TempDraft;

import java.util.List;

public interface EditorService {
    long generateTempArticleId();

    long saveDraftInCache(TempDraft tempDraft) throws IllegalAccessException;

    TempDraft getArticleContent(Integer articleId);

    Long saveArticle(Draft draft) throws IllegalAccessException;

    List<Category> searchCategoryByName(String keyword);
}
