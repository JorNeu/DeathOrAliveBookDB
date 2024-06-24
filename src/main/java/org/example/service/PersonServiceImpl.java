package org.example.service;
import org.example.repository.PersonRepository;
import java.util.*;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    @Override
    public List<Integer> getPeopleAlivePerYear() {
        List<Integer> result = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        for (int year = 1900; year <= currentYear; year++) {
            int count = personRepository.countAliveInYear(year);
            result.add(count);
        }
        return result;
    }
    @Override
    public int getPeopleAliveInYear(int year) {
        return personRepository.countAliveInYear(year);
    }
    @Override
    public List<String> getNamesAliveInYear(int year) {
        return personRepository.findNamesAliveInYear(year);
    }
    @Override
    public void printPeopleAlivePerYear() {
        List<Integer> peopleAlivePerYear = getPeopleAlivePerYear();
        int yearBase = 1900;
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < peopleAlivePerYear.size(); i++) {
            int year = yearBase + i;
            int peopleAlive = peopleAlivePerYear.get(i);
            System.out.println(year + " : " + peopleAlive);
        }
        if (peopleAlivePerYear.size() < (currentYear - yearBase + 1)) {
            for (int i = peopleAlivePerYear.size(); i <= (currentYear - yearBase); i++) {
                int year = yearBase + i;
                System.out.println(year + " : 0");
            }
        }
    }
    @Override
    public List<String> findNamesAndCountAliveInYear(int year) {
        List<String> result = new ArrayList<>();
        int countAlive = personRepository.countAliveInYear(year);
        List<String> namesAlive = personRepository.findNamesAliveInYear(year);
        result.add("{");
        result.add("\t\"year\" : " + year + ",");
        result.add("\t\"peopleAlive\" : " + countAlive + ",");
        result.add("\t\"Names\": " + namesAlive.toString());
        result.add("}");
        return result;
    }
    @Override
    public List<Object> getPeopleAliveInRanges(String ranges) {
        List<Object> result = new ArrayList<>();
        List<String> invalidRanges = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d{4})-(\\d{4})|(\\d{4})");
        Matcher matcher = pattern.matcher(ranges);
        List<Integer> yearsToAdd = new ArrayList<>();
        while (matcher.find()) {
            String range1Start = matcher.group(1);
            String range1End = matcher.group(2);
            String singleYear = matcher.group(3);
            if (singleYear != null) {
                int year = Integer.parseInt(singleYear);
                if (year >= 1900 && year <= 2000) {
                    yearsToAdd.add(year);
                } else {
                    invalidRanges.add(singleYear);
                }
            } else if (range1Start != null && range1End != null) {
                int startYear = Integer.parseInt(range1Start);
                int endYear = Integer.parseInt(range1End);
                if (startYear <= endYear && startYear >= 1900 && endYear <= 2000) {
                    for (int year = startYear; year <= endYear; year++) {
                        yearsToAdd.add(year);
                    }
                } else {
                    invalidRanges.add(range1Start + "-" + range1End);
                }
            }
        }
        Collections.sort(yearsToAdd);
        for (int year : yearsToAdd) {
            addYearEntry(result, year, year);
        }
        if (!invalidRanges.isEmpty()) {
            result.add(0, "Las siguientes expresiones de rango son inválidas o están fuera del rango válido (1900-2000): " + invalidRanges);
        }
        return result;
    }
    private void addYearEntry(List<Object> result, int startYear, int year) {
        Map<String, Object> yearEntry = createYearEntry(year);
        result.add(yearEntry);
    }
    private void addRangeEntries(List<Object> result, int startYear, int endYear) {
        for (int year = startYear; year <= endYear; year++) {
            addYearEntry(result, startYear, year);
        }
    }
    private Map<String, Object> createYearEntry(int year) {
        int peopleAlive = personRepository.countAliveInYear(year);
        List<String> names = personRepository.findNamesAliveInYear(year);
        Map<String, Object> yearEntry = new HashMap<>();
        yearEntry.put("year", year);
        yearEntry.put("peopleAlive", peopleAlive);
        yearEntry.put("names", names);
        return yearEntry;
    }}