package com.example.student_processing.service;

import com.example.student_processing.model.Student;
import com.github.javafaker.Faker;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class FileProcessingService {

    private final Faker faker = new Faker();

    /**
     * Generate fake student data and create Excel file
     */
    public byte[] generateExcelFile(int numberOfRecords) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Students");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Student ID");
            headerRow.createCell(1).setCellValue("First Name");
            headerRow.createCell(2).setCellValue("Last Name");
            headerRow.createCell(3).setCellValue("Date of Birth");
            headerRow.createCell(4).setCellValue("Class");
            headerRow.createCell(5).setCellValue("Score");

            // Generate fake data
            for (int i = 1; i <= numberOfRecords; i++) {
                Row row = sheet.createRow(i);
                
                // Generate student data
                long studentId = i;
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                LocalDate dob = generateRandomDate();
                String className = generateRandomClass();
                int score = ThreadLocalRandom.current().nextInt(0, 91); // 0-90 points
                
                row.createCell(0).setCellValue(studentId);
                row.createCell(1).setCellValue(firstName);
                row.createCell(2).setCellValue(lastName);
                row.createCell(3).setCellValue(dob.toString());
                row.createCell(4).setCellValue(className);
                row.createCell(5).setCellValue(score);
            }

            // Auto-size columns
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel file", e);
        }
    }

    /**
     * Convert Excel to CSV and add 10 points to scores
     */
    public byte[] convertExcelToCsv(MultipartFile excelFile) {
        try (InputStream inputStream = excelFile.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             CSVWriter csvWriter = new CSVWriter(writer)) {

            Sheet sheet = workbook.getSheetAt(0);
            
            for (Row row : sheet) {
                String[] csvRow = new String[6];
                
                for (int i = 0; i < 6; i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        if (i == 5 && row.getRowNum() > 0) { // Score column, skip header
                            // Add 10 points to score
                            int originalScore = (int) cell.getNumericCellValue();
                            csvRow[i] = String.valueOf(originalScore + 10);
                        } else {
                            csvRow[i] = getCellValueAsString(cell);
                        }
                    } else {
                        csvRow[i] = "";
                    }
                }
                
                csvWriter.writeNext(csvRow);
            }

            csvWriter.flush();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error converting Excel to CSV", e);
        }
    }

    /**
     * Parse CSV file and return list of students (adds 5 more points to scores)
     */
    public List<Student> parseCsvFile(MultipartFile csvFile) {
        List<Student> students = new ArrayList<>();
        
        try (InputStream inputStream = csvFile.getInputStream();
             InputStreamReader reader = new InputStreamReader(inputStream);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextLine;
            boolean isHeader = true;
            long maxStudentId = getMaxStudentId(); // Get current max student ID
            
            while ((nextLine = csvReader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }
                
                if (nextLine.length >= 6) {
                    // Generate new unique student ID
                    Long studentId = ++maxStudentId;
                    String firstName = nextLine[1];
                    String lastName = nextLine[2];
                    LocalDate dob = LocalDate.parse(nextLine[3]);
                    String className = nextLine[4];
                    int score = Integer.parseInt(nextLine[5]) + 5; // Add 5 more points
                    
                    Student student = new Student(firstName, lastName, className, dob, score, studentId);
                    students.add(student);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error parsing CSV file", e);
        }
        
        return students;
    }

    private long getMaxStudentId() {
        // This will be injected by Spring - we'll add it to the service
        return System.currentTimeMillis() % 1000000; // Simple approach for now
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private LocalDate generateRandomDate() {
        // Generate random date between 1990 and 2010
        long minDay = LocalDate.of(1990, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2010, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    private String generateRandomClass() {
        String[] classes = {"Class A", "Class B", "Class C", "Class D", "Class E", 
                           "Grade 1", "Grade 2", "Grade 3", "Grade 4", "Grade 5"};
        return classes[ThreadLocalRandom.current().nextInt(classes.length)];
    }
}