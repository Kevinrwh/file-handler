package com.kevin.file_handler.service;

import com.kevin.file_handler.model.ACHReturn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

@Service
public class FileProcessingService {
    private final List<ACHReturn> achReturns = new ArrayList<>();

    public String processFile(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
                
                achReturns.clear();
                String line;
                int computedTotalCents = 0;
                int expectedTotalCents = -1;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("01")) {
                        String fileType = line.substring(2,8).trim();
                        if (!fileType.equals("ACHRET")) {
                            return "Invalid file type: " + fileType;
                        }
                    } else if (line.startsWith("02")) {
                        String name = line.substring(2,17).trim();
                        String amountStr = line.substring(17,26); // 9-digit integer in cents
                        String date = line.substring(26, 34); // YYYYMMDD

                        int amountCents = Integer.parseInt(amountStr);
                        computedTotalCents += amountCents;

                        double amount = amountCents / 100.0;
                        achReturns.add(new ACHReturn(name, amount, formatDate(date)));
                    } else if (line.startsWith("99")) {
                        String label = line.substring(2,7).trim();
                        if(!label.equals("TOTAL")) {
                            return "Invalid trailer record label: " + label + "expected 'TOTAL";
                        }
                        expectedTotalCents = Integer.parseInt(line.substring(7,16));
                    }
                    else {
                        return "Unknown record type: " + line;
                    }
                }

                if (expectedTotalCents == -1) {
                    return "Missing trailer record";
                }

                if (computedTotalCents == -1) {
                    return String.format("Total mismatch. Trailer: %.2f, Computed: %.2f",
                        expectedTotalCents / 100.0, computedTotalCents / 100.0);
                }

                return String.format("Parsed %d ACH Returns. Total: %.2f", achReturns.size(), computedTotalCents / 100.0);
    
            }
        }
        public List<ACHReturn> getACHReturns() {
            return achReturns;
        }

        private String formatDate(String raw) {
            return raw.substring(0, 4) + "-" + raw.substring(4, 6) + "-" + raw.substring(6);
        }
}
