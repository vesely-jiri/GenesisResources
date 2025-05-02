package cz.jpcz.util;

import cz.jpcz.exceptions.PersonNotFoundException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class UserVerify {

    private static final String FILE_NAME = "src/main/resources/dataPersonId.txt";

    public static void validatePerson(String personId) {
        if (personId == null) throw new PersonNotFoundException("Person can't be null");
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(FILE_NAME)))) {
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equals(personId)) {
                    return;
                }
            }
            throw new PersonNotFoundException("Person not found");
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e.getMessage());
        }
    }
}
