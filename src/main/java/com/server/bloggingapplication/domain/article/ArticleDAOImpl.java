package com.server.bloggingapplication.domain.article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.server.bloggingapplication.application.article.PostArticleRequest;
import com.server.bloggingapplication.application.article.UpdateArticleRequest;
import com.server.bloggingapplication.domain.article.tag.TagDAO;
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

    private static final String UPDATE_ARTICLES_TIMESTAMP = "UPDATE articles SET updated_at = CURRENT_TIMESTAMP where id = ?";
    private final String CREATE_ARTICLE_STMT = "INSERT INTO articles(author_id, title , article_description , body) VALUES(?,?,?,?)";
    private final String FETCH_ARTICLE_USING_ID_STMT = "SELECT * from articles where id = ?";
    private final String FIND_ARTICLE_ID_BY_AUTHORID_STMT = "SELECT id from articles WHERE author_id = ? and title = ?";
    private final String UPDATE_ARTICLE_STMT = "UPDATE articles SET title = ?, body = ? ,article_description = ? WHERE id = ?";

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
                // Integer id = rs.getInt("id");
                Integer authorId = rs.getInt("author_id");
                String author = userDAO.getUserName(authorId);
                String title = rs.getString("title");
                String description = rs.getString("article_description");
                String articleBody = rs.getString("body");
                String createdAt = getFormattedDate(rs.getString("created_at"));
                String updatedAt = getFormattedDate(rs.getString("updated_at"));

                return new Article(title, author, description, articleBody, createdAt, updatedAt);
            }

        };
        return rowMapper;
    }

    private Article getArticleById(Integer articleId) {

        return jdbcTemplate.queryForObject(FETCH_ARTICLE_USING_ID_STMT, articleRowMapper(), new Object[] { articleId });
    }

    @Override
    public Article createArticle(Integer userId, PostArticleRequest articleRequest) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {

            jdbcTemplate.update(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                    PreparedStatement preparedStatement = con.prepareStatement(CREATE_ARTICLE_STMT,
                            new String[] { "id" });
                    preparedStatement.setInt(1, userId);
                    preparedStatement.setString(2, articleRequest.getTitle());
                    preparedStatement.setString(3, articleRequest.getDescription());
                    preparedStatement.setString(4, articleRequest.getBody());
                    return preparedStatement;
                }
            }, keyHolder);

            Integer publishedArticleId = keyHolder.getKey().intValue();
            Article publishedArticle = getArticleById(publishedArticleId);

            tagDAO.saveTags(publishedArticleId, articleRequest.getTags());
            return publishedArticle;

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
    public List<Article> fetchLatestArticles() {

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

            return recentArticleList;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
