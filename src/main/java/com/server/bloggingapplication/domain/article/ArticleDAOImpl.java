package com.server.bloggingapplication.domain.article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.server.bloggingapplication.application.article.CommentResponse;
import com.server.bloggingapplication.application.article.CreateCommentRequest;
import com.server.bloggingapplication.application.article.PostArticleRequest;
import com.server.bloggingapplication.application.article.UpdateArticleRequest;
import com.server.bloggingapplication.domain.article.tag.Tag;
import com.server.bloggingapplication.domain.article.tag.TagDAO;
import com.server.bloggingapplication.domain.user.User;
import com.server.bloggingapplication.domain.user.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("deprecation")
public class ArticleDAOImpl implements ArticleDAO {

    private static final String FETCH_ARTICLES_FROM_USERFOLLOWINGS_STMT = "SELECT * FROM articles WHERE author_id IN (SELECT followeeId FROM user_followings WHERE followerId = ?)";
    private final String UPDATE_ARTICLES_TIMESTAMP = "UPDATE articles SET updated_at = CURRENT_TIMESTAMP where id = ?";
    private final String CREATE_ARTICLE_STMT = "INSERT INTO articles(author_id, title , article_description , body) VALUES(?,?,?,?)";
    private final String FETCH_ARTICLE_USING_ID_STMT = "SELECT * from articles where id = ?";
    private final String FIND_ARTICLE_ID_BY_AUTHORID_STMT = "SELECT id from articles WHERE author_id = ? and title = ?";
    private final String UPDATE_ARTICLE_STMT = "UPDATE articles SET title = ?, body = ? ,article_description = ? WHERE id = ?";

    private final String CREATE_COMMENT_ON_ARTICLE_STMT = "INSERT INTO comments(body, user_id, article_id) VALUES (?,?,?)";
    private final String GET_CREATED_TIMESTAMP_OF_CMNT_STMT = "SELECT created_at from comments WHERE id = ";

    private final String FETCH_COMMENTS_ON_ARTICLE_STMT = "SELECT * FROM comments WHERE article_id = ?";

    private final String MARK_ARTICLE_AS_FAVOURITE_STMT = "INSERT INTO article_favourites VALUES(?, ?)";
    private final String UNFAVOURITE_ARTICLE_STMT = "DELETE FROM article_favourites WHERE article_id = ? and user_id = ?";
    private final String FIND_FAVOURITED_ARTICLE_STMT = "SELECT COUNT(*) FROM article_favourites WHERE article_id = ? and user_id = ?";

    private final String FETCH_ARTICLES_BY_TAGS_STMT = "SELECT * FROM articles WHERE articles.id IN (select article_id from  articles_tags LEFT JOIN tags ON articles_tags.tag_id = tags.id where tag = ?)";
    private final String GET_TAGS_FOR_ARTICLE_STMT = "SELECT * FROM tags WHERE id IN (SELECT tag_id FROM articles_tags WHERE article_id = ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private TagDAO tagDAO;

    private String getFormattedDate(String source) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(source);
            // System.out.println(date.toString());
            // Timestamp timestamp = new Timestamp(date.getTime());
            return date.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private RowMapper<Article> articleRowMapper() {

        RowMapper<Article> rowMapper = new RowMapper<Article>() {

            @Override
            public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
                Integer id = rs.getInt("id");
                Integer authorId = rs.getInt("author_id");
                String author = userDAO.getUserName(authorId);
                String title = rs.getString("title");
                String description = rs.getString("article_description");
                String articleBody = rs.getString("body");
                String createdAt = getFormattedDate(rs.getString("created_at"));
                String updatedAt = getFormattedDate(rs.getString("updated_at"));

                return new Article(id, title, author, description, articleBody, createdAt, updatedAt);
            }

        };
        return rowMapper;
    }

    private RowMapper<CommentResponse> commentRowMapper() {
        RowMapper<CommentResponse> rowMapper = new RowMapper<CommentResponse>() {
            @Override
            public CommentResponse mapRow(ResultSet rs, int rowNum) throws SQLException {

                Integer commentId = rs.getInt(1);
                String body = rs.getString(2);
                Integer userId = rs.getInt(3);
                String author = userDAO.getUserName(userId);
                Integer articleId = rs.getInt(4);
                String createdAt = getFormattedDate(rs.getString(5));

                CommentResponse comment = new CommentResponse(commentId, userId, articleId, author, body, createdAt);
                return comment;
            }
        };
        return rowMapper;
    }

    private Article getArticleById(Integer articleId) {

        return jdbcTemplate.queryForObject(FETCH_ARTICLE_USING_ID_STMT, articleRowMapper(), new Object[] { articleId });
    }

    @Override
    public ArticleResponse createArticle(Integer userId, PostArticleRequest articleRequest) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {

            jdbcTemplate.update(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                    PreparedStatement preparedStatement = con.prepareStatement(CREATE_ARTICLE_STMT,
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setInt(1, userId);
                    preparedStatement.setString(2, articleRequest.getTitle());
                    preparedStatement.setString(3, articleRequest.getDescription());
                    preparedStatement.setString(4, articleRequest.getBody());
                    return preparedStatement;
                }
            }, keyHolder);

            Integer publishedArticleId = keyHolder.getKey().intValue();
            Article publishedArticle = getArticleById(publishedArticleId);

            ArticleResponse articleResponse = new ArticleResponse(publishedArticle, articleRequest.getTags());

            tagDAO.saveTags(publishedArticleId, articleRequest.getTags());

            return articleResponse;

        } catch (DataAccessException e) {
            System.out.println("Article with same Title already exists");
            return null;
        }

    }

    private Integer getArticleIdWithRequiredTitleForGivenUserId(Integer userId, String title) {
        try {

            Integer articleId = jdbcTemplate.queryForObject(FIND_ARTICLE_ID_BY_AUTHORID_STMT,
                    new Object[] { userId, title }, Integer.class);

            return articleId;
        } catch (DataAccessException e) {
            System.out.println("ARTICLE DOESNT EXIST");
            return null;
        }
    }

    @Override
    public Article updateArticle(Integer userId, UpdateArticleRequest articleRequest) {

        Integer articleId = getArticleIdWithRequiredTitleForGivenUserId(userId, articleRequest.getTitle());
        // no article exists
        if (articleId == null) {
            return null;
        }

        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement statement = con.prepareStatement(UPDATE_ARTICLE_STMT);
                    statement.setString(1, articleRequest.getTitle());
                    statement.setString(2, articleRequest.getBody());
                    statement.setString(3, articleRequest.getDescription());
                    statement.setInt(4, articleId);

                    return statement;
                }
            });

            jdbcTemplate.update(UPDATE_ARTICLES_TIMESTAMP, new Object[] { articleId });
            Article updatedArticle = getArticleById(articleId);
            return updatedArticle;

        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<ArticleResponse> fetchLatestArticles() {

        List<Article> recentArticleList = null;

        try {
            recentArticleList = jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement statement = con
                            .prepareStatement("SELECT * FROM articles ORDER BY created_at DESC");
                    return statement;
                }
            }, articleRowMapper());

            return populateTagsForArticle(recentArticleList);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<ArticleResponse> populateTagsForArticle(List<Article> articles) {

        List<ArticleResponse> articleResponses = new ArrayList<>();

        for (Article article : articles) {
            List<Tag> tags = jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                    PreparedStatement statement = con.prepareStatement(GET_TAGS_FOR_ARTICLE_STMT);
                    statement.setInt(1, article.getId());
                    return statement;
                }
            }, tagRowMapper());
            Set<String> allTags = new HashSet<>();
            for (Tag tag : tags) {
                allTags.add(tag.getValue());
            }
            articleResponses.add(new ArticleResponse(article,allTags));
        }
        return articleResponses;

    }

    private RowMapper<Tag> tagRowMapper() {

        RowMapper<Tag> rowMapper = new RowMapper<Tag>() {
            @Override
            public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
                Integer id = rs.getInt("id");
                String value = rs.getString("tag");
                return new Tag(id, value);
            }
        };
        return rowMapper;
    }

    @Override
    public CommentResponse createCommentOnArticle(Integer articleId, Integer publisherId, String publisherName,
            CreateCommentRequest commentRequest) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String bodyOfComment = commentRequest.getBody();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                    PreparedStatement statement = con.prepareStatement(CREATE_COMMENT_ON_ARTICLE_STMT,
                            Statement.RETURN_GENERATED_KEYS);
                    statement.setString(1, bodyOfComment);
                    statement.setInt(2, publisherId);
                    statement.setInt(3, articleId);
                    return statement;
                }
            }, keyHolder);

            String createdAtTimeStamp = jdbcTemplate
                    .queryForObject(GET_CREATED_TIMESTAMP_OF_CMNT_STMT + keyHolder.getKey().intValue(), String.class);

            return new CommentResponse(keyHolder.getKey().intValue(), publisherId, articleId, publisherName,
                    bodyOfComment,
                    getFormattedDate(createdAtTimeStamp));

        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<CommentResponse> fetchAllCommentsForArticle(Integer articleId) {

        try {

            List<CommentResponse> comments = jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                    PreparedStatement statement = con.prepareStatement(FETCH_COMMENTS_ON_ARTICLE_STMT);
                    statement.setInt(1, articleId);
                    return statement;
                }
            }, commentRowMapper());

            return comments;

        } catch (DataAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

    @Override
    public boolean markArticleAsFavouriteForUser(Integer articleId, String userName) {

        Optional<User> currentUser = userDAO.findByUserName(userName);
        if (!currentUser.isPresent()) {
            return false;
        }
        try {
            jdbcTemplate.update(MARK_ARTICLE_AS_FAVOURITE_STMT, new Object[] { articleId, currentUser.get().getId() });
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean markArticleAsUnFavouriteForUser(Integer articleId, String userName) {

        Optional<User> currentUser = userDAO.findByUserName(userName);
        if (!currentUser.isPresent()) {
            return false;
        }
        try {
            Integer exists = jdbcTemplate.queryForObject(FIND_FAVOURITED_ARTICLE_STMT,
                    new Object[] { articleId, currentUser.get().getId() }, Integer.class);
            if (exists == 0) {
                return false;
            }

            jdbcTemplate.update(UNFAVOURITE_ARTICLE_STMT, new Object[] { articleId, currentUser.get().getId() });
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ArticleResponse> fetchArticlesFromFollowedUsers(String userName) {

        Optional<User> optionalOfFollower = userDAO.findByUserName(userName);
        if (!optionalOfFollower.isPresent()) {
            return Collections.emptyList();
        }
        Integer followerId = optionalOfFollower.get().getId();
        List<Article> articlesPublishedByUserFollowings = jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                PreparedStatement statement = con.prepareStatement(FETCH_ARTICLES_FROM_USERFOLLOWINGS_STMT);
                statement.setInt(1, followerId);
                return statement;
            }
        }, articleRowMapper());
        return populateTagsForArticle(articlesPublishedByUserFollowings);
    }

    @Override
    public List<ArticleResponse> fetchArticlesByTag(String tag) {

        try {
            List<Article> articlesByTag = jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                    PreparedStatement statement = con.prepareStatement(FETCH_ARTICLES_BY_TAGS_STMT);
                    statement.setString(1, tag);
                    return statement;
                }
            }, articleRowMapper());
            return populateTagsForArticle(articlesByTag);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
