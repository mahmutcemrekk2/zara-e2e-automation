package com.company.automation.core.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URL;

public class ExcelReader {

    private final String location;

    public ExcelReader(String location) {
        this.location = location;
    }

    public String getCellValue(String sheetName, int rowIndex, int colIndex) {
        try (InputStream is = open(location);
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new IllegalArgumentException("Sheet not found: " + sheetName);

            Row row = sheet.getRow(rowIndex);
            if (row == null) return "";

            Cell cell = row.getCell(colIndex);
            if (cell == null) return "";

            return new DataFormatter().formatCellValue(cell).trim();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel: " + location, e);
        }
    }


    private InputStream open(String loc) throws IOException {
        String cp = loc.startsWith("/") ? loc.substring(1) : loc;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL res = cl.getResource(cp);
        if (res != null) {
            InputStream in = res.openStream();
            if (in != null) return in;
        }

        File f = new File(loc);
        if (f.exists()) return new FileInputStream(f);

        throw new FileNotFoundException(
                "Excel not found. Tried classpath:[" + cp + "] and filesystem:[" + f.getAbsolutePath() + "]"
        );
    }

    public boolean resourceExists() {
        String cp = location.startsWith("/") ? location.substring(1) : location;
        return Thread.currentThread().getContextClassLoader().getResource(cp) != null
                || new File(location).exists();
    }
}
