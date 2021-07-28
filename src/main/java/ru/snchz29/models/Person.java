package ru.snchz29.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    @JsonAlias("id")
    private Integer id;
    @JsonAlias("first_name")
    private String firstName;
    @JsonAlias("last_name")
    private String lastName;
    @JsonAlias("photo_400")
    private String photoUri;
    private List<Integer> friends;

    public static Comparator<Person> comparator = (lhs, rhs) -> {
        if (lhs == rhs)
            return 0;
        if (lhs == null)
            return -1;
        if (rhs == null)
            return 1;
        return (lhs.getLastName() + lhs.getFirstName()).compareTo(rhs.getLastName() + rhs.getFirstName());
    };


    @Override
    public String toString() {
        return "Person{" +
                " id: " + id +
                ", firstName: '" + firstName + "'" +
                ", lastName: '" + lastName + "'" +
                '}';
    }
}
