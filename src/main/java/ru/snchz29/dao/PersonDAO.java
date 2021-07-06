package ru.snchz29.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.snchz29.models.Person;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LogManager.getLogger(PersonDAO.class);


    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        deleteObsoleteEntries();
    }

    private void deleteObsoleteEntries() {
        jdbcTemplate.update("DELETE FROM person WHERE DATE_PART('day', NOW()::timestamp - created_on) > 1");
    }

    public void savePerson(Person person) {
        logger.info("Save " + person.getFirstName() + " " + person.getLastName());
        jdbcTemplate.update("INSERT INTO person(vk_id, first_name, last_name, photo_uri, created_on, friends) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getPhotoUri(),
                getCurrentTimestamp(),
                createSqlArray(person.getFriends()));
    }

    public List<Person> getAllPersons() {
        return jdbcTemplate.query("SELECT * FROM person", new PersonRowMapper());
    }

    public Person getPerson(Integer id) {
        logger.info("Get user: " + id + " from db");
        List<Person> res = jdbcTemplate.query("SELECT * FROM person WHERE vk_id=?", new PersonRowMapper(), id);
        if (res.size() > 0)
            return res.get(0);
        return null;
    }

    private Timestamp getCurrentTimestamp() {
        Date date = new Date();
        long time = date.getTime();
        return new Timestamp(time);
    }

    private java.sql.Array createSqlArray(List<Integer> list) {
        java.sql.Array intArray = null;
        try {
            intArray = jdbcTemplate.getDataSource().getConnection().createArrayOf("integer", list.toArray());
        } catch (SQLException ignore) {
        }
        return intArray;
    }
}

