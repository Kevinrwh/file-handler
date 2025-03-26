# ACH File Handler (Mainframe-Style)

This Spring Boot application simulates a mainframe-style ACH Return file processing system. It supports uploading ACH return files formatted with fixed-width records and processes them into structured data.

## ✅ Features
- Accepts fixed-width ACH Return files via a REST API.
- Follows mainframe conventions:
  - No delimiters (commas, tabs)
  - Line-based records
  - Record type indicators: `01` (Header), `02` (Detail), `99` (Trailer)
- Parses and stores ACH return records in memory.
- Validates total amounts against trailer records.
- Provides API endpoint to retrieve parsed data.

---

## 📄 File Format Specification

### Header Record (1st line)
```
01ACHRET
```
- `01` — Record Type: Header
- `ACHRET` — File type indicator

### Detail Records (1–10)
Each line contains:
```
02<NAME(15 chars padded)><AMOUNT(9 digits in cents)><DATE(YYYYMMDD)>
```
Example:
```
02JOHN DOE       00001005020250303
```

### Trailer Record (last line)
```
99TOTAL<amount>
```
Example:
```
99TOTAL000050250
```

---

## 📁 Directory Structure
```
src/
├── main/
│   ├── java/com/kevin/file_handler/
│   │   ├── controller/FileUploadController.java
│   │   ├── model/ACHReturn.java
│   │   ├── service/FileProcessingService.java
│   │   └── util/TestFileGenerator.java
│   └── resources/application.properties
```

---

## 🔧 How to Run

### 1. Generate Test Files
Run the file generator:
```bash
./mvnw compile exec:java -Dexec.mainClass="com.kevin.file_handler.util.TestFileGenerator"
```

### 2. Start the Application
```bash
./mvnw spring-boot:run
```

### 3. Upload a File
```bash
curl -X POST -F "file=@test-files/ach_returns.txt" http://localhost:8080/api/files/upload
```

### 4. View Parsed Records
```bash
curl http://localhost:8080/api/files/returns
```

---

## 📌 To Do (Coming Soon)
- Add support for Direct Deposit files
- Persist data to a database (H2 or PostgreSQL)
- Add unit and integration tests
- Add front-end UI for file uploads and visualization

---

## 👨‍💻 Author
Kevin R. — Software Consultant / Developer  
Built as a real-world simulation of mainframe file handling using modern Java + Spring Boot.