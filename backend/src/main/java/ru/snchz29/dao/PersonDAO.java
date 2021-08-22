package ru.snchz29.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.snchz29.models.Person;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public class PersonDAO {
    private static final Logger logger = LogManager.getLogger(PersonDAO.class);
    private final JdbcTemplate jdbcTemplate;


    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        deleteObsoleteEntries();
    }

    private void deleteObsoleteEntries() {
        jdbcTemplate.update("DELETE FROM person WHERE DATE_PART('day', NOW()::timestamp - created_on) > 1");
        logger.info("Entries amount: " + getAllPersons().size());
    }

    public void savePerson(Person person) {
        logger.info("Save " + person.getFirstName() + " " + person.getLastName());

        jdbcTemplate.update(connection -> {
            final String sqlUpdate = "INSERT INTO person(vk_id, first_name, last_name, photo_uri, created_on, friends) VALUES (?, ?, ?, ?, ?, ?)";
            final PreparedStatement ps = connection.prepareStatement(sqlUpdate);
            ps.setInt(1, person.getId());
            ps.setString(2, person.getFirstName());
            ps.setString(3, person.getLastName());
            ps.setString(4, person.getPhotoUri());
            ps.setTimestamp(5, getCurrentTimestamp());
            ps.setArray(6, connection.createArrayOf("integer", person.getFriends().toArray()));
            return ps;
        });
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
}

