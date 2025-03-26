package com.kevin.file_handler.util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        generateACHReturnFile(DIRECTORY + "ach_returns.txt", 1, 10);
        generateDirectDepositFile(DIRECTORY + "direct_deposits.txt", 5);
    }

    /**
     * Generates an ACH Return file with:
     * - A header record starting with 0
     * - 1 to 10 detail records containing name, amount, and date
     * - A trainer record summing all the amounts
     * @param filename      The path where the file will be written
     * @param minRecords    Minimum number of return records to include
     * @param maxRecords    Maximum number of return records to include
     */
    public static void generateACHReturnFile(String filename, int minRecords, int maxRecords) {
        try (FileWriter writer = new FileWriter(Paths.get(filename).toFile())) {
            
            // Header record
            String header = String.format ("%-2s%-6s%-72s", "01", "ACHRET", "");
            writer.write(header + "\n");

            int recordCount = minRecords + RANDOM.nextInt(maxRecords - minRecords+1);
            String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            int totalCents = 0;

            for(int i = 0; i < recordCount; i++) {
                String name = String.format("%-15s", getRandomName());
                int amountCents = 5000 + RANDOM.nextInt(45000); // 50.00 to 499.99
                totalCents += amountCents;
                String amount = String.format("%09d", amountCents);
                String detail = String.format("%-2s%s%s%s%-46s", "02", name, amount, date, "");
                writer.write(detail + "\n");
            }

            // Trailer
            String trailer = String.format("%2s%-5s%09d%-64s", "99", "TOTAL", totalCents, "");
            writer.write(trailer + "\n");

            System.out.println("Mainframe-style ACH return file generated: " + filename);
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
