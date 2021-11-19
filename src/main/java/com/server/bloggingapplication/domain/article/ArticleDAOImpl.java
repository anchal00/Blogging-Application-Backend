package com.server.bloggingapplication.domain.article;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.server.bloggingapplication.application.article.PostArticleRequest;
import com.server.bloggingapplication.domain.user.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleDAOImpl implements ArticleDAO {

    private final String CREATE_ARTICLE_STMT = "INSERT INTO articles(author_id, title , article_description , body) VALUES(?,?,?,?)";
    private final String FETCH_ARTICLE_USING_ID_STMT = "SELECT * from articles where id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDAO userDAO;

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
                Date createdAt = rs.getDate("created_at");
                Date updatedAt = rs.getDate("updated_at");

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

        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                PreparedStatement preparedStatement = con.prepareStatement(CREATE_ARTICLE_STMT, new String[] { "id" });
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, articleRequest.getTitle());
                preparedStatement.setString(3, articleRequest.getDescription());
                preparedStatement.setString(4, articleRequest.getBody());
                return preparedStatement;
            }
        }, keyHolder);

        Integer publishedArticleId = keyHolder.getKey().intValue();

        Article publishedArticle = getArticleById(publishedArticleId);
        return publishedArticle;
    }

}
