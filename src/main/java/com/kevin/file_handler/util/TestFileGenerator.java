package com.kevin.file_handler.util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.List;
import java.util.Arrays;

/**
 * Utility class to generate test files for ACH Returns and Direct Deposit Changes
 * @param args
 */
public class TestFileGenerator {
    
    private static final List<String> NAMES = Arrays.asList(
        "John Doe", "Jane Smith", "Alice Johnson", "Bob Brown", "Charlie White"
    );

    private static final Random RANDOM = new Random();
    private static final String DIRECTORY = "test-files/";

    public static void main(String[] args) {
        ensureTestDirectoryExists();
        generateACHReturnFile(DIRECTORY + "ach_returns.txt", 5);
        generateDirectDepositFile(DIRECTORY + "direct_deposits.txt", 5);
    }

    /**
     * Generates an ACH Return file with multiple records
     * @param filename
     * @param recordCount
     */
    public static void generateACHReturnFile(String filename, int recordCount) {
        try (FileWriter writer = new FileWriter(Paths.get(filename).toFile())) {
            writer.write("0\n"); // File type indicator (ACH Return)
            for(int i = 0; i < recordCount; i++) {
                double amount = 50 + (RANDOM.nextDouble() * 450);
                String name = getRandomName();
                writer.write(String.format("%.2f,%s%n", amount, name));
            }
            System.out.println("Generated ACH Return file: " + filename);
        } catch (IOException e) {
            System.err.println("Error generating ACH Return file: " + e.getMessage());
        }
    }

    /**
     * Generates a Direct Deposit file with multiple records
     * @param filename
     * @param recordCount
     */
    public static void generateDirectDepositFile(String filename, int recordCount) {
        try (FileWriter writer = new FileWriter(Paths.get(filename).toFile())) {
            writer.write("1\n"); // File type indicator (Direct Deposit)
            for(int i = 0; i < recordCount; i++) {
                String name = getRandomName();
                String accountNumber = generateRandomAccountNumber();
                writer.write(String.format("%s,%s%n", name, accountNumber));
            }
            System.out.println("Generated Direct Deposit file: " + filename);
        } catch (IOException e) {
            System.err.println("Error generating Direct Deposit file: " + e.getMessage());
        }
    }

    /**
     * Generates the test-files directory exists.
     */
    private static void ensureTestDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(DIRECTORY));
        } catch (IOException e) {
            System.err.println("Error creating test-files directory: " + e.getMessage());
        }
    }

    /**
     * Returns a random name from the list.
     */
    private static String getRandomName() {
        return NAMES.get(RANDOM.nextInt(NAMES.size()));
    }

    /**
     * Generates a random 8-digit account number
     */
    private static String generateRandomAccountNumber() {
        return String.format("%08d", RANDOM.nextInt(100000000));
    }
}
