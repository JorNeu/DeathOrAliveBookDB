package org.example.repository;
import org.example.entity.Person;
import java.util.List;
public interface PersonRepository {
    List<Person> findAll();
    int countAliveInYear(int year);
    List<String> findNamesAliveInYear(int year);
    List<Person> findAliveInRange(int startYear, int endYear);
    List<String> findNamesAndCountAliveInYear(int year);
}