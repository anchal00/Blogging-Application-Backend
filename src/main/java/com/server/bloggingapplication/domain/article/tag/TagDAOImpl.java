package com.server.bloggingapplication.domain.article.tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class TagDAOImpl implements TagDAO {

    private static final String TAG_EXISTS_STMT = "SELECT * FROM tags WHERE tag = ?";
    private static final String CREATE_TAG_STMT = "INSERT INTO tags(tag) values(?)";
    private static final String INSERT_TAG_FOR_ARTICLE_STMT = "INSERT INTO articles_tags values(?,?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Integer saveTag(String tagValue) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                PreparedStatement preparedStatement = con.prepareStatement(CREATE_TAG_STMT,
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, tagValue);
                return preparedStatement;
            }
        }, keyHolder);
        Integer tagId = keyHolder.getKey().intValue();
        return tagId;
    }

    private Tag getTagIdByValue(String tagValue) {
        List<Tag> tags = null;
        try {

            tags = jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(TAG_EXISTS_STMT);
                    ps.setString(1, tagValue);
                    return ps;
                }
            }, tagRowMapper());

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return (tags == null || tags.isEmpty()) ? null : tags.get(0);
    }

    private RowMapper<Tag> tagRowMapper() {

        RowMapper<Tag> mapper = new RowMapper<Tag>() {
            @Override
            public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {

                Integer tagId = rs.getInt("id");
                String value = rs.getString("tag");
                return new Tag(tagId, value);
            }
        };
        return mapper;
    }

    @Override
    public void saveTags(Integer articleId, Set<String> tags) {

        for (String tagValue : tags) {
            Integer tagId = null;
            Tag tag = getTagIdByValue(tagValue);

            if (tag == null) {
                tagId = saveTag(tagValue);
            } else {
                tagId = tag.getId();
            }

            try {
                jdbcTemplate.update(INSERT_TAG_FOR_ARTICLE_STMT, new Object[] { articleId, tagId });   
            } catch (DataAccessException e) {
                e.printStackTrace();
                System.out.println("Duplicate Tag");
            }
        }
    }

}
