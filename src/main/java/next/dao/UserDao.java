package next.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import core.jdbc.ConnectionManager;
import next.model.User;
import next.support.JdbcTemplate;
import next.support.PreparedStatementSetter;
import next.support.RowMapper;

public class UserDao {
    public void insert(User user) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();

        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        template.executeUpdate(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(User user) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();

        String sql = "update users set name = ?, password = ?, email = ? where userId = ?";
        template.executeUpdate(sql, user.getName(), user.getPassword(), user.getEmail(), user.getUserId());
    }

    public void delete(String userId) throws SQLException {
        JdbcTemplate template = new JdbcTemplate();

        String sql = "delete users where userId = ?";
        template.executeUpdate(sql, userId);
    }

    public List<User> findAll() throws SQLException {
        RowMapper<User> rm = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                return new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email")
                );
            }
        };

        JdbcTemplate template = new JdbcTemplate();

        String sql = "SELECT userId, password, name, email FROM USERS";
        return template.list(sql, rm);
    }

    public User findByUserId(String userId) throws SQLException {
        RowMapper<User> rm = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                return new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email")
                );
            }
        };

        JdbcTemplate template = new JdbcTemplate();

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";
        return template.executeQuery(sql, rm, userId);
    }
}
