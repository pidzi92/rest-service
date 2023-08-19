package com.telemetry.restservice.util;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.telemetry.restservice.dao.TelemetryItemDao;
import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.entity.TelemetryProperty;
import com.telemetry.restservice.model.TelemetryPropertyTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class CsvImporter {

    @Autowired
    private TelemetryItemDao telemetryItemDao;

    @Value("${telemetry.source.csv.root}")
    private String csvSourceFolder;

    public void csvToDb(){
        CSVParser parser = new CSVParserBuilder().withIgnoreQuotations(true).withSeparator(';').build();


        List<String> allFiles = queryCsvFiles(csvSourceFolder);

        for (String file: allFiles) {
            importSingleFile(parser, file);
            moveFileToSubfolder(file, "PROCESSED");
        }

    }

    private void importSingleFile(CSVParser parser, String fullFilePath) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(fullFilePath)).withCSVParser(parser).build()){
            String[] singleRow = reader.readNext();
            String[] headers = singleRow;
            if (headers == null){
                log.error("File does not have headers. Skipping: {}", fullFilePath);
                return;
            }else{
                headers = Arrays.stream(headers)
                        .map(this::csvToDbHeader)
                        .toArray(String[]::new);
            }
            List<TelemetryItem> telemetryItems = new ArrayList<>();

            while((singleRow = reader.readNext()) != null){
                TelemetryItem telItem = TelemetryItem.builder().build();
                List<TelemetryProperty> propsForSingleItem = new ArrayList<>();
                for (int i=0; i< headers.length; i++){
                    //TODO parse column names and check types
                    TelemetryProperty prop = TelemetryProperty.builder()
                            .telPropName(headers[i])
                            .telPropValue(singleRow[i])
                            .telPropType(TelemetryPropertyTypeEnum.STRING)
                            .telItem(telItem)
                            .build();
                    propsForSingleItem.add(prop);

                }
                telItem.setTelProps(propsForSingleItem);
                telemetryItems.add(telItem);
            }
            telemetryItemDao.saveAll(telemetryItems);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> queryCsvFiles(String folderPath) {
        List<String> csvFilePaths = new ArrayList<>();

        // Create a File object for the folder
        File folder = new File(folderPath);

        // Check if the path corresponds to a directory
        if (folder.isDirectory()) {
            // Create a FilenameFilter to filter CSV files
            FilenameFilter csvFileFilter = (dir, name) -> name.toLowerCase().endsWith(".csv");

            // Get a list of CSV files in the folder using the filter
            File[] csvFiles = folder.listFiles(csvFileFilter);

            if (csvFiles != null) {
                // Iterate through each CSV file and add its full path to the list
                for (File csvFile : csvFiles) {
                    csvFilePaths.add(csvFile.getAbsolutePath());
                }
            }
        }

        return csvFilePaths;
    }

    private void moveFileToSubfolder(String filePath, String subfolderName) {
        File sourceFile = new File(filePath);

        // Check if the source file exists
        if (!sourceFile.exists()) {
            log.error("Source file does not exist: {}", sourceFile);
            return;
        }

        // Create the subfolder within the same directory as the source file
        File subfolder = new File(sourceFile.getParent(), subfolderName);
        if (!subfolder.exists()) {
            boolean created = subfolder.mkdir();
            if (!created) {
                log.error("Failed to create subfolder: {}", subfolderName);
                return;
            }
        }

        // Create a new file object for the destination path
        File destinationFile = new File(subfolder, sourceFile.getName());

        // Move the source file to the destination path
        boolean moved = sourceFile.renameTo(destinationFile);
        if (moved) {
            log.info("File moved successfully: {}", filePath);
        } else {
            log.error("Failed to move the file: {}", filePath);
        }
    }

    private static String removeTextInBrackets(String input) {
        return input.replaceAll("\\[[^\\]]*\\]", "");
    }

    private static String removeNonAlphanumeric(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", " ");
    }

    private static String toPascalCase(String input) {
        return WordUtils.capitalizeFully(input).replaceAll(" ", "");
    }

    public String csvToDbHeader(String input) {
        String withoutBrackets = removeTextInBrackets(input);
        String withoutSpecialChars = removeNonAlphanumeric(withoutBrackets);
        return toPascalCase(withoutSpecialChars);
    }
}
