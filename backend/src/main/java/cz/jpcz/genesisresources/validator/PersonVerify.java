package cz.jpcz.genesisresources.validator;

import cz.jpcz.genesisresources.exceptions.PersonFileReadException;
import cz.jpcz.genesisresources.exceptions.PersonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@Slf4j
@Component
public class PersonVerify {

    private static final String FILE_NAME = "dataPersonId.txt";
    private Set<String> personIdSet = new HashSet<>();

    public PersonVerify() {
        this.personIdSet = loadPersonIds();
    }

    public Set<String> loadPersonIds() {
        Set<String> personSet = new HashSet<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (inputStream == null) {
                throw new PersonNotFoundException("File not found: " + FILE_NAME);
            }
            try (Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(inputStream)))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (line.isBlank()) continue;
                    personSet.add(line);
                }
            }
        } catch (IOException e) {
            throw new PersonFileReadException("Unable to read file: " + FILE_NAME + " " + e.getMessage());
        }
        return personSet;
    }

    public void validatePerson(String personId) {
        if (personId == null) throw new PersonNotFoundException("Person can't be null");

        if (!personIdSet.contains(personId.trim())) throw new PersonNotFoundException("Person with id " + personId + " not found");
    }
}