package core.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextReportWriter {

    private static final Logger log = LogManager.getLogger(TextReportWriter.class);
    private static final String REPORT_DIR = "reports/data/";

    public void writeToFile(String baseName, String content) {
        try {
            String sanitized = baseName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = REPORT_DIR + sanitized + "_" + timestamp + ".txt";

            File dir = new File(REPORT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
                writer.write(content);
            }

            log.info("Report written to file: {}", fileName);
        } catch (Exception e) {
            log.error("Failed to write report", e);
        }
    }
}
