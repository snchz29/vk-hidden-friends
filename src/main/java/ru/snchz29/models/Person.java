package ru.snchz29.models;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

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
                  Timestamp timestamp,
                  List<Integer> friends) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUri = photoUri;
        this.friends = friends;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public List<Integer> getFriends() {
        return friends;
    }

    public void setFriends(List<Integer> friends) {
        this.friends = friends;
    }
}
