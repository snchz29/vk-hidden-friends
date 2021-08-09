package ru.snchz29.models;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultimap;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.SneakyThrows;
import lombok.var;
import org.checkerframework.checker.nullness.qual.Nullable;
import ru.snchz29.utils.JsonGeneratorWrapper;
import ru.snchz29.utils.PersonResponseBuilder;

public class PersonMultimap implements Multimap<Person, Person> {

    private final Multimap<Person, Person> personToHiddenFriends;

    public PersonMultimap() {
        personToHiddenFriends = TreeMultimap.create(Person.comparator, Person.comparator);
    }

    @Override
    public int size() {
        return personToHiddenFriends.size();
    }

    @Override
    public boolean isEmpty() {
        return personToHiddenFriends.isEmpty();
    }

    @Override
    public boolean containsKey(@Nullable Object o) {
        return personToHiddenFriends.containsKey(o);
    }

    @Override
    public boolean containsValue(@Nullable Object o) {
        return personToHiddenFriends.containsValue(o);
    }

    @Override
    public boolean containsEntry(@Nullable Object o, @Nullable Object o1) {
        return personToHiddenFriends.containsEntry(o, o1);
    }

    @Override
    public boolean put(@Nullable Person person, @Nullable Person person2) {
        return personToHiddenFriends.put(person, person2);
    }

    @Override
    public boolean remove(@Nullable Object o, @Nullable Object o1) {
        return personToHiddenFriends.remove(o, o1);
    }

    @Override
    public boolean putAll(@Nullable Person person, Iterable<? extends Person> iterable) {
        return personToHiddenFriends.putAll(person, iterable);
    }

    @Override
    public boolean putAll(Multimap<? extends Person, ? extends Person> multimap) {
        return personToHiddenFriends.putAll(multimap);
    }

    @Override
    public Collection<Person> replaceValues(@Nullable Person person, Iterable<? extends Person> iterable) {
        return personToHiddenFriends.replaceValues(person, iterable);
    }

    @Override
    public Collection<Person> removeAll(@Nullable Object o) {
        return personToHiddenFriends.removeAll(o);
    }

    @Override
    public void clear() {
        personToHiddenFriends.clear();
    }

    @Override
    public Collection<Person> get(@Nullable Person person) {
        return personToHiddenFriends.get(person);
    }

    @Override
    public Set<Person> keySet() {
        return personToHiddenFriends.keySet();
    }

    @Override
    public Multiset<Person> keys() {
        return personToHiddenFriends.keys();
    }

    @Override
    public Collection<Person> values() {
        return personToHiddenFriends.values();
    }

    @Override
    public Collection<Entry<Person, Person>> entries() {
        return personToHiddenFriends.entries();
    }

    @Override
    public Map<Person, Collection<Person>> asMap() {
        return personToHiddenFriends.asMap();
    }

    @SneakyThrows
    public String asJson() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        var generator = new JsonFactory().createGenerator(stream, JsonEncoding.UTF8);
        generator.setCodec(new ObjectMapper());
        var wrapper = new JsonGeneratorWrapper(generator);
        wrapper.writeArray(() -> {
            personToHiddenFriends.asMap().forEach((key, value) -> wrapper.writeObject(() -> {
                wrapper.writeObjectField("person", key);
                wrapper.writeFieldName("hiddenFriend");
                wrapper.writeArray(() -> value.forEach(wrapper::writeObject));
            }));
        });
        return stream.toString();
    }
}
