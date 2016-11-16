package com.qiwi.appwithcustomrestcontroller.dao;

import com.qiwi.appwithcustomrestcontroller.exeption.DuplicatePnoneException;
import com.qiwi.appwithcustomrestcontroller.exeption.NotFoundUserException;
import com.qiwi.appwithcustomrestcontroller.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class UserDAOImpl implements UserDAO {

    public static final String SQL_GET_BY_ID = "SELECT * FROM users WHERE id = :id";
    public static final String SQL_DELETE_BY_ID = "DELETE FROM users WHERE id = :id";
    public static final String SQL_UPDATE_USER_BY_ID = "UPDATE users SET phone = :phone, firstname = :firstname, lastname = :lastname WHERE id = :id";
    public static final String SQL_GET_USERS_ORDER_BY_ID = "SELECT * FROM users ORDER BY id";
    public static final String SQL_INSERT = "INSERT INTO users (phone, firstname, lastname) VALUES (:phone, :firstname, :lastname)";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public User insert(User user) throws DuplicatePnoneException {

        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("phone", user.getPhone());
            parameters.addValue("firstname", user.getFirstName());
            parameters.addValue("lastname", user.getLastName());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(SQL_INSERT, parameters, keyHolder);
            user.setId(keyHolder.getKey().longValue());
        } catch (DuplicateKeyException e) {
            throw new DuplicatePnoneException(user.getPhone());
        }
        return user;
    }

    @Override
    public User get(long id) throws NotFoundUserException {
        Map parameters = new HashMap();
        parameters.put("id", id);
        try {
            return jdbcTemplate.queryForObject(SQL_GET_BY_ID, parameters, new UsersRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundUserException(id);
        }
    }

    @Override
    public void delete(long id) throws NotFoundUserException {
        Map parameters = new HashMap();
        parameters.put("id", id);
        int rows = jdbcTemplate.update(SQL_DELETE_BY_ID, parameters);
        if (rows < 1) throw new NotFoundUserException(id);
    }

    @Override
    public User update(long id, User user) throws EmptyResultDataAccessException, DuplicatePnoneException {
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("phone", user.getPhone());
        parameters.put("firstname", user.getFirstName());
        parameters.put("lastname", user.getLastName());

        try {
            int a = jdbcTemplate.update(SQL_UPDATE_USER_BY_ID, parameters);
            if (a < 1)
                throw new NotFoundUserException(id);
            user.setId(id);
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundUserException(id);
        } catch (DuplicateKeyException e) {
            throw new DuplicatePnoneException(user.getPhone());
        }
    }

    @Override
    public List<User> getAll() throws NotFoundUserException {
        return jdbcTemplate.query(SQL_GET_USERS_ORDER_BY_ID, new UsersRowMapper());
    }
}
