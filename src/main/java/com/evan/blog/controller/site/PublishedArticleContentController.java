package com.evan.blog.controller.site;

import com.evan.blog.model.Comment;
import com.evan.blog.model.Commentary;
import com.evan.blog.model.PublishedArticle;
import com.evan.blog.pojo.ItemListData;
import com.evan.blog.pojo.PublishedArticleDetails;
import com.evan.blog.service.CommentaryService;
import com.evan.blog.service.PublishedArticleCacheService;
import com.evan.blog.service.PublishedArticleService;
import com.evan.blog.pojo.BlogJSONResult;
import com.evan.blog.util.IPUtil;
import com.github.pagehelper.PageInfo;
import jdk.nashorn.internal.objects.annotations.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/article")
public class PublishedArticleContentController {

    @Autowired
    PublishedArticleService publishedArticleService;
    @Autowired
    CommentaryService commentaryService;
    @Autowired
    PublishedArticleCacheService publishedArticleCacheService;

    @GetMapping(path = "/{pubId}")
    public BlogJSONResult getPublishedArticleContent(@PathVariable("pubId") Integer pubId) {
        PublishedArticleDetails publishedArticleDetails = publishedArticleService.getPublishedArticle(pubId);
        return BlogJSONResult.ok(publishedArticleDetails);
    }

    @GetMapping(path = "/{pubId}/commentary/p/{pageIndex}")
    public BlogJSONResult getPublishedArticleCommentaries(
            @PathVariable("pubId") Integer pubId,
            @PathVariable("pageIndex") Integer pageIndex) {
        PageInfo<Commentary> commentaryPageInfo = commentaryService.getCommentaryByPubId(pubId, pageIndex);

        return BlogJSONResult.ok(new ItemListData((int)commentaryPageInfo.getTotal(),
                commentaryPageInfo.getList()));
    }

    @PostMapping(path = "/{pubId}/commentary")
    public BlogJSONResult addCommentaryInPublishedArticle(
            @PathVariable("pubId") Integer pubId,
            @RequestBody Comment comment) {
        comment.setPubId(pubId);
        commentaryService.postComment(comment);
        BlogJSONResult blogJSONResult = BlogJSONResult.ok();
        blogJSONResult.setMsg("Comment succeed!");
        return blogJSONResult;
    }

    @PutMapping(path = "/{pubId}/vote")
    public BlogJSONResult voteForPublishedArticle (@PathVariable("pubId") Integer pubId, HttpServletRequest request) {
        String ip = IPUtil.getRealIP(request);

        boolean vote = publishedArticleCacheService.vote(pubId, ip);
        System.out.println(ip);

        BlogJSONResult blogJSONResult = BlogJSONResult.ok(vote);
        blogJSONResult.setMsg("Vote succeed!");
        return blogJSONResult;
    }

    @GetMapping(path = "/{pubId}/vote")
    public BlogJSONResult hasVotedForPublishedArticle(@PathVariable("pubId") Integer pubId, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        System.out.println(ip);
        boolean hasVoted = publishedArticleCacheService.hasVoted(pubId, ip);
        BlogJSONResult blogJSONResult = BlogJSONResult.ok(hasVoted);
        blogJSONResult.setMsg("Has voted for this article");
        return blogJSONResult;
    }
}
