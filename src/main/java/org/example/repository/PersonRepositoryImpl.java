package org.example.repository;
import org.example.entity.Person;
import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import javax.sql.DataSource;
public class PersonRepositoryImpl implements PersonRepository {
    private final DataSource dataSource;
    public PersonRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public List<Person> findAll() {
        List<Person> persons = new ArrayList<>();
        String query = "SELECT name, birth, death FROM persons";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                LocalDate birthDate = resultSet.getDate("birth").toLocalDate();
                LocalDate deathDate = resultSet.getDate("death").toLocalDate();
                Person.Dates dates = new Person.Dates(birthDate, deathDate);
                persons.add(new Person(name, dates));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }
    @Override
    public int countAliveInYear(int year) {
        String query = "SELECT COUNT(*) FROM persons WHERE YEAR(birth) <= ? AND YEAR(death) >= ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, year);
            statement.setInt(2, year);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public List<String> findNamesAliveInYear(int year) {
        List<String> names = new ArrayList<>();
        String query = "SELECT name FROM persons WHERE YEAR(birth) <= ? AND YEAR(death) >= ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, year);
            statement.setInt(2, year);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                names.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }
    @Override
    public List<Person> findAliveInRange(int startYear, int endYear) {
        List<Person> persons = new ArrayList<>();
        String query = "SELECT name, birth, death FROM persons WHERE YEAR(birth) <= ? AND YEAR(death) >= ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, endYear);
            statement.setInt(2, startYear);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                LocalDate birthDate = resultSet.getDate("birth").toLocalDate();
                LocalDate deathDate = resultSet.getDate("death").toLocalDate();
                Person.Dates dates = new Person.Dates(birthDate, deathDate);
                persons.add(new Person(name, dates));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }
    @Override
    public List<String> findNamesAndCountAliveInYear(int year) {
        List<String> result = new ArrayList<>();
        String query = "SELECT name FROM persons WHERE YEAR(birth) <= ? AND YEAR(death) >= ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, year);
            statement.setInt(2, year);
            ResultSet resultSet = statement.executeQuery();
            int count = 0;
            List<String> names = new ArrayList<>();
            while (resultSet.next()) {
                names.add(resultSet.getString("name"));
                count++;
            }
            result.add("year : " + year);
            result.add("peopleAlive : " + count);
            result.add("Names : " + names);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}