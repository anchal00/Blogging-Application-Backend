package com.server.bloggingapplication.domain.article.tag;

import java.util.Set;

public interface TagDAO {

    void saveTags(Integer articleId, Set<String> tags);
}
