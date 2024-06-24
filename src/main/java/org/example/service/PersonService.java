package org.example.service;
import java.util.List;
public interface PersonService {
    List<Integer> getPeopleAlivePerYear();
    int getPeopleAliveInYear(int year);
    List<String> getNamesAliveInYear(int year);
    void printPeopleAlivePerYear();
    List<String> findNamesAndCountAliveInYear(int year);
    List<Object> getPeopleAliveInRanges(String ranges);
}