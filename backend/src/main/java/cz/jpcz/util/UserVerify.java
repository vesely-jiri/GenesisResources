package cz.jpcz.util;

import cz.jpcz.exceptions.PersonNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class UserVerify {

    private static final String FILE_NAME = "dataPersonId.txt";

    public static void validatePerson(String personId) {
        if (personId == null) throw new PersonNotFoundException("Person can't be null");
        try (InputStream inputStream = UserVerify.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (inputStream == null) {
                throw new PersonNotFoundException("File not found: " + FILE_NAME);
            }
            try (Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(inputStream)))) {
                while (scanner.hasNextLine()) {
                    if (scanner.nextLine().equals(personId)) {
                        return;
                    }
                }
                throw new PersonNotFoundException("Person not found");
            }
        } catch (NullPointerException | IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
        }
    }
}