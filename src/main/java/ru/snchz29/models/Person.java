package ru.snchz29.models;

import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class Person {
    private Integer id;
    private String firstName;
    private String lastName;
    private String photoUri;
    private List<Integer> friends;

    public Person() {
    }

    public Person(Integer id,
                  String firstName,
                  String lastName,
                  String photoUri,
                  List<Integer> friends) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUri = photoUri;
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "Person{" +
                " id: " + id +
                ", firstName: '" + firstName + "'" +
                ", lastName: '" + lastName + "'" +
                '}';
    }
}
