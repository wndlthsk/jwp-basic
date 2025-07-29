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
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setParameters(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };

        JdbcTemplate template = new JdbcTemplate();

        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        template.executeUpdate(sql, pss);
    }

    public void update(User user) throws SQLException {
        PreparedStatementSetter pss = new PreparedStatementSetter() {

            @Override
            public void setParameters(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(4, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(1, user.getName());
                pstmt.setString(3, user.getEmail());
            }
        };

        JdbcTemplate template = new JdbcTemplate();

        String sql = "update users set name = ?, password = ?, email = ? where userId = ?";
        template.executeUpdate(sql, pss);
    }

    public void delete(String userId) throws SQLException {
        PreparedStatementSetter pss = new PreparedStatementSetter() {

            @Override
            public void setParameters(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);

            }
        };

        JdbcTemplate template = new JdbcTemplate();

        String sql = "delete users where userId = ?";
        template.executeUpdate(sql, pss);
    }

    public List<User> findAll() throws SQLException {
        // TODO 구현 필요함.
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            String sql = "SELECT userId, password, name, email FROM USERS";
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<User> users = new ArrayList<>();
            if (rs.next()) {
                users.add(new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                    rs.getString("email")));
            }

            return users;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public User findByUserId(String userId) throws SQLException {
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setParameters(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }
        };
        RowMapper rm = new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs) throws SQLException {
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
        return (User)template.executeQuery(sql, pss, rm);
    }
}
