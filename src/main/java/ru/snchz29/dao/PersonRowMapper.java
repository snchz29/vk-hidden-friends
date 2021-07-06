package ru.snchz29.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.snchz29.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

class PersonRowMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getInt("vk_id"));
        person.setFirstName(resultSet.getString("first_name"));
        person.setLastName(resultSet.getString("last_name"));
        person.setPhotoUri(resultSet.getString("photo_uri"));
        person.setFriends(Arrays.asList((Integer[]) resultSet.getArray("friends").getArray()));
        return person;
    }
}
