package org.example;
import org.apache.commons.dbcp2.BasicDataSource;
import org.example.repository.PersonRepository;
import org.example.repository.PersonRepositoryImpl;
import org.example.service.PersonService;
import org.example.service.PersonServiceImpl;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
public class Main {
    private static PersonService personService;
    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/persons");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        PersonRepository personRepository = new PersonRepositoryImpl(dataSource);
        personService = new PersonServiceImpl(personRepository);
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            printMenu();
            try {
                option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1:
                        printPeopleAlivePerYear();
                        break;
                    case 2:
                        showPeopleAliveInSpecificYear(scanner);
                        break;
                    case 3:
                        handlePeopleAliveInRanges(scanner);
                        break;
                    case 0:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, ingrese una opción válida.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número válido del menú.");
                scanner.nextLine();
                option = -1;
            }
        } while (option != 0);
        scanner.close();
    }
    private static void printMenu() {
        System.out.println("Elija una opción:");
        System.out.println("1. Mostrar personas vivas por año del 1900 al actual");
        System.out.println("2. Mostrar personas vivas en un año específico");
        System.out.println("3. Mostrar personas vivas por rango de años");
        System.out.println("0. Salir del programa");
    }
    private static void printPeopleAlivePerYear() {
        System.out.println("Mostrando personas vivas por año del 1900 al actual:");
        personService.printPeopleAlivePerYear();
        System.out.println();
    }
    private static void showPeopleAliveInSpecificYear(Scanner scanner) {
        try {
            int currentYear = LocalDate.now().getYear();
            System.out.println("Ingrese el año para ver personas vivas (1900 - " + currentYear + "):");
            int year = scanner.nextInt();
            scanner.nextLine();
            if (year < 1900 || year > currentYear) {
                System.out.println("Error: Ingrese un año válido dentro del rango permitido.");
                return;
            }
            List<String> result = personService.findNamesAndCountAliveInYear(year);
            for (String line : result) {
                System.out.println(line);
            }
            System.out.println();
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un año válido (número entero).");
            scanner.nextLine();
        }
    }
    private static void handlePeopleAliveInRanges(Scanner scanner) {
        try {
            System.out.println("Ingrese la expresión de rangos (ej. 1930-1932,1940,1942-1955,1999): ");
            String ranges = scanner.nextLine().trim();
            List<Object> peopleAliveInRanges = personService.getPeopleAliveInRanges(ranges);
            for (Object entry : peopleAliveInRanges) {
                if (entry instanceof Map) {
                    Map<String, Object> yearEntry = (Map<String, Object>) entry;
                    int year = (int) yearEntry.get("year");
                    int peopleAlive = (int) yearEntry.get("peopleAlive");
                    List<String> names = (List<String>) yearEntry.get("names");
                    System.out.println("Year: " + year);
                    System.out.println("People alive: " + peopleAlive);
                    System.out.println("Names: " + names);
                    System.out.println();
                } else if (entry instanceof String) {
                    System.out.println(entry);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al procesar la expresión de rangos: " + e.getMessage());
        }
    }
}