package com.evan.blog.service;

import com.evan.blog.model.Draft;
import com.evan.blog.model.QueryFilter;
import com.evan.blog.model.enums.DraftStatus;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface DraftService {
    PageInfo<Draft> getDrafts(Integer pageIndex, QueryFilter filter);
    Draft queryDraftById(long id);
    void addDraft(Draft draft);
    void updateDraft(Draft draft);
    void updateDraftStatus(DraftStatus status, List<Long> ids);
    void removeDrafts(List<Long> articleIds);
}
