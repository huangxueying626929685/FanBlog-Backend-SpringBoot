package com.evan.blog.controller.management;

import com.evan.blog.model.*;
import com.evan.blog.model.enums.ArticleStatus;
import com.evan.blog.model.enums.Order;
import com.evan.blog.pojo.BlogJSONResult;
import com.evan.blog.pojo.ItemListData;
import com.evan.blog.pojo.management.ArticlesStatusUpdate;
import com.evan.blog.pojo.management.DeletedArticlesManagementListItem;
import com.evan.blog.pojo.management.DraftsManagementListItem;
import com.evan.blog.pojo.management.PublishedArticlesManagementListItem;
import com.evan.blog.service.ArticleService;
import com.evan.blog.service.PublishedArticleService;
import com.evan.blog.util.JsonUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/management")
public class ArticlesManagementController {

    @Autowired
    PublishedArticleService publishedArticleService;

    @Autowired
    ArticleService articleService;

    @GetMapping(path = "/publishedArticles/p/{pageIndex}")
    public BlogJSONResult getPublishedArticlesManagementList(
            @PathVariable("pageIndex") Integer pageIndex,
            @RequestParam("filter") String filter) {
        QueryFilter queryFilter;
        try {

            queryFilter = JsonUtil.jsonToPojo(filter, PublishedArticleQueryFilter.class);
        } catch (NullPointerException e) {
            queryFilter = null;
        }
        PageInfo<PublishedArticle> publishedArticlePageInfo = publishedArticleService.getPublishedArticlesByFilter(pageIndex, queryFilter);

        List<PublishedArticlesManagementListItem> items = new ArrayList<>();

        publishedArticlePageInfo.getList().forEach(item ->
            items.add(new PublishedArticlesManagementListItem(item))
        );

        return BlogJSONResult.ok(new ItemListData((int)publishedArticlePageInfo.getTotal(), items));
    }

    @GetMapping(path = "/drafts/p/{pageIndex}")
    public BlogJSONResult getDraftsManagementList(
            @PathVariable("pageIndex") Integer pageIndex,
            @RequestParam("filter") String filter) {
        ArticleQueryFilter queryFilter;
        try {

            queryFilter = JsonUtil.jsonToPojo(filter, ArticleQueryFilter.class);
        } catch (NullPointerException e) {
            queryFilter = new ArticleQueryFilter("created_time", Order.Desc, ArticleStatus.Editing);
        }

        PageInfo<Article> articlePageInfo = articleService.getArticles(pageIndex, queryFilter);

        List<DraftsManagementListItem> items = new ArrayList<>();

        articlePageInfo.getList().forEach(item -> items.add(new DraftsManagementListItem(item)));

        return BlogJSONResult.ok(new ItemListData((int) articlePageInfo.getTotal(), items));
    }

    @GetMapping(path = "/deletedArticles/p/{pageIndex}")
    public BlogJSONResult getDeletedArticlesManagementListItem(
            @PathVariable("pageIndex") Integer pageIndex,
            @RequestParam("filter") String filter) {
        ArticleQueryFilter queryFilter;
        try {

            queryFilter = JsonUtil.jsonToPojo(filter, ArticleQueryFilter.class);
        } catch (NullPointerException e) {
            queryFilter = new ArticleQueryFilter("latest_edited_time", Order.Desc, ArticleStatus.Deleted);
        }

        PageInfo<Article> articlePageInfo = articleService.getArticles(pageIndex, queryFilter);

        List<DeletedArticlesManagementListItem> items = new ArrayList<>();

        articlePageInfo.getList().forEach(item -> items.add(new DeletedArticlesManagementListItem(item)));

        return BlogJSONResult.ok(new ItemListData((int) articlePageInfo.getTotal(), items));
    }

    @PutMapping(value = "/articles/status")
    public BlogJSONResult updateArticlesStatus(@RequestBody ArticlesStatusUpdate update) {
        System.out.println(update);
        articleService.updateArticleStatus(update.getStatus(), update.getArticleIds());
        return BlogJSONResult.ok();
    }

    @DeleteMapping(value = "/articles")
    public BlogJSONResult deleteArticlesPermanently(@RequestParam("ids") String ids) {
        List<Integer> articleIds = JsonUtil.jsonToList(ids, Integer.class);
        System.out.println(articleIds);
        articleService.removeArticles(articleIds);
        return BlogJSONResult.ok();
    }

    @DeleteMapping(value = "/publishedArticles")
    public BlogJSONResult deletePublishedArticles(@RequestParam("ids") String ids) throws Exception {
        List<Integer> pubIds = JsonUtil.jsonToList(ids, Integer.class);
        publishedArticleService.deletePublishedArticles(pubIds);
        return BlogJSONResult.ok();
    }
}
