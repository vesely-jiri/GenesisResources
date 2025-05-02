package cz.jpcz.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class UserVerify {

    public static boolean verify(String personId) {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader("src/main/resources/dataPersonId.txt")))) {
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equals(personId)) {
                    System.out.println("Successful verification");
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
