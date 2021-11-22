package com.server.bloggingapplication.domain.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import com.server.bloggingapplication.application.user.CreateUserRequestDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDAOImpl implements UserDAO {

    private final String INSERT_STMT = "INSERT INTO users(firstname, lastname , username, bio, email, passwd) "
            + "VALUES (?, ? , ? , ?, ? , ?)";

    private final String COUNT_OCCURENCES_STMT = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ? ";

    private final String FIND_BY_USERNAME_STMT = "SELECT * FROM users WHERE username = ? ";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findByUserName(String username) {

        List<User> users = jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(FIND_BY_USERNAME_STMT);
                statement.setString(1, username);
                return statement;
            }
        }, getUserTableRowMapper());

        return (users.isEmpty()) ? Optional.empty() : Optional.of(users.get(0));
    }

    private RowMapper<User> getUserTableRowMapper() {

        RowMapper<User> rowMapper = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int arg1) throws SQLException {

                Integer id = resultSet.getInt("id");
                String userName = resultSet.getString("username");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String bio = resultSet.getString("bio");
                String email = resultSet.getString("email");
                String password = resultSet.getString("passwd");
                User user = new User(id, firstName, lastName, userName, bio, email, password);
                return user;
            }
        };
        return rowMapper;

    }

    private boolean isValidUserData(CreateUserRequestDTO userToBeValidated) {

        Integer existingRecords = jdbcTemplate.queryForObject(COUNT_OCCURENCES_STMT, Integer.class,
                new Object[] { userToBeValidated.getUsername(), userToBeValidated.getEmail() });

        return existingRecords == 0;

    }

    @Override
    public Integer saveUser(CreateUserRequestDTO user) {

        // if (isValidUserData(user) == false) {
        // return -1;
        // }

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

                    PreparedStatement statement = con.prepareStatement(INSERT_STMT, Statement.RETURN_GENERATED_KEYS);
                    statement.setString(1, user.getFirstname());
                    statement.setString(2, user.getLastname());
                    statement.setString(3, user.getUsername());
                    statement.setString(4, user.getBio());
                    statement.setString(5, user.getEmail());
                    statement.setString(6, new BCryptPasswordEncoder().encode(user.getPassword()));

                    return statement;
                }

            }, keyHolder);

            return keyHolder.getKey().intValue();
        } catch (DataAccessException e) {
            System.out.println("User with same username or email exists already ! ");
            return -1;
        }

    }

    @Override
    public String getUserName(Integer userId) {

        return jdbcTemplate.queryForObject(
                "SELECT CONCAT(firstname,' ',lastname) AS fullname from users WHERE id = " + userId, String.class);
    }

}
