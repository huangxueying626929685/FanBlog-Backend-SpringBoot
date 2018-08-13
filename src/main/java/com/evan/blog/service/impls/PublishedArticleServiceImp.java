package com.evan.blog.service.impls;

import com.evan.blog.model.Article;
import com.evan.blog.model.PublishedArticle;
import com.evan.blog.model.PublishingArticle;
import com.evan.blog.pojo.PublishedArticleDetails;
import com.evan.blog.pojo.PublishedArticleItem;
import com.evan.blog.repository.ArticleDao;
import com.evan.blog.repository.PublishedArticleDao;
import com.evan.blog.service.PublishedArticleCacheService;
import com.evan.blog.service.PublishedArticleService;
import com.evan.blog.util.PubIdGenerator;
import com.evan.blog.util.RedisOperator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "publishedArticleService")
public class PublishedArticleServiceImp implements PublishedArticleService {

    private int pageSize = 6;

    @Autowired
    PublishedArticleDao publishedArticleDao;
    @Autowired
    ArticleDao articleDao;
    @Autowired
    PublishedArticleCacheService publishedArticleCacheService;
    @Autowired
    PubIdGenerator pubIdGenerator;

    @Override
    public PageInfo<PublishedArticleItem> getAllPublishedArticleItems(Integer pageIndex) {
        PageHelper.startPage(pageIndex, pageSize);

        List<PublishedArticle> publishedArticles = publishedArticleDao.selectPublishedArticles();
        PageInfo<PublishedArticle> publishedArticlePageInfo = new PageInfo<>(publishedArticles);

        long total = publishedArticlePageInfo.getTotal();

        List<PublishedArticleItem> publishedArticleItems = new ArrayList<>();

        Integer[] pubIds = new Integer[publishedArticles.size()];
        for (int i = 0; i < pubIds.length; i++) {
            pubIds[i] = publishedArticles.get(i).getPubId();
        }

        PublishedArticleItem publishedArticleItem;
        Long[] voteCounts = publishedArticleCacheService.bulkGetVoteCount(pubIds);
        for (int j = 0; j < publishedArticles.size(); j++) {
            publishedArticleItem = new PublishedArticleItem(publishedArticles.get(j));
            publishedArticleItem.setVoteCount(voteCounts[j].intValue());
            publishedArticleItems.add(publishedArticleItem);
        }

        //dumpy code

        PageInfo<PublishedArticleItem> publishedArticleItemPageInfo = new PageInfo<>(publishedArticleItems);
        publishedArticleItemPageInfo.setTotal(total);
        return publishedArticleItemPageInfo;
    }

    @Override
    public PageInfo<PublishedArticleItem> getPublishedArticleItemsByCategoryId(Integer categoryId, Integer pageIndex) {
        PageHelper.startPage(pageIndex, pageSize);
        List<PublishedArticle> publishedArticles = publishedArticleDao.selectPublishedArticlesByCategoryId(categoryId);
        PageInfo<PublishedArticle> publishedArticlePageInfo = new PageInfo<>(publishedArticles);

        long total = publishedArticlePageInfo.getTotal();
        List<PublishedArticleItem> publishedArticleItems = new ArrayList<>();

        Integer[] pubIds = new Integer[publishedArticles.size()];
        for (int i = 0; i < pubIds.length; i++) {
            pubIds[i] = publishedArticles.get(i).getPubId();
        }

        PublishedArticleItem publishedArticleItem;
        Long[] voteCounts = publishedArticleCacheService.bulkGetVoteCount(pubIds);
        for (int j = 0; j < publishedArticles.size(); j++) {
            publishedArticleItem = new PublishedArticleItem(publishedArticles.get(j));
            publishedArticleItem.setVoteCount(voteCounts[j].intValue());
            publishedArticleItems.add(publishedArticleItem);
        }

        //dumpy code

        PageInfo<PublishedArticleItem> publishedArticleItemPageInfo = new PageInfo<>(publishedArticleItems);
        publishedArticleItemPageInfo.setTotal(total);
        return publishedArticleItemPageInfo;
    }

    @Override
    public PublishedArticleDetails getPublishedArticle(Integer pubId) {

        PublishedArticle publishedArticle = publishedArticleDao.selectPublishedArticleByPubId(pubId);

        Long articleVisitorCount = publishedArticleCacheService.getArticleVisitorCount(pubId);
        Long voteCount = publishedArticleCacheService.getVoteCount(pubId);

        PublishedArticleDetails publishedArticleDetails = new PublishedArticleDetails(publishedArticle);

        publishedArticleDetails.setVoteCount(voteCount.intValue());
        publishedArticleDetails.setVisitorCount(articleVisitorCount.intValue());

        return publishedArticleDetails;
    }

    @Override
    public String getTitleByPubId(Integer pubId) {
        return publishedArticleDao.selectPublishedArticleTitleByPubId(pubId);
    }


    @Override
    @Transactional
    public void addPublishedArticle(PublishingArticle publishingArticle) {
        Integer pubId = pubIdGenerator.generatePubId();
        publishingArticle.setPubId(pubId);

        String title = publishingArticle.getTitle();

        Article article = new Article();

        article.setId(publishingArticle.getArticleId());

        article.setTitle(title);

        articleDao.updateArticle(article);

//        if (publishingArticle.getCategory() == null)

        publishedArticleDao.insertPublishingArticle(publishingArticle);

        publishedArticleCacheService.updateLatestPublishedArticle(pubId, title);
    }
}
