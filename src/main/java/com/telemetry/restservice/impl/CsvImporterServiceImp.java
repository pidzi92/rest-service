package com.telemetry.restservice.impl;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.telemetry.restservice.dao.TelemetryItemDao;
import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.entity.TelemetryProperty;
import com.telemetry.restservice.service.CsvImporterService;
import com.telemetry.restservice.util.ColumnUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the CsvImporterService interface providing methods for importing CSV telemetry items from CSV into DB.
 */
@Slf4j
@Service
public class CsvImporterServiceImp implements CsvImporterService {

    private static final String PROCESSED_SUBFOLDER = "PROCESSED";
    public static final String MACHINE_TYPE_PROP = "MachineType";
    public static final String NOT_APPLICABLE = "NA";
    public static final String MACHINE_TYPE_TRACTOR = "Tractor";
    public static final String MACHONE_TYPE_COMBINE = "Combine";
    public static final String MACHINE_TYPE_UNKNOWN = "Unknown";
    String TRACTOR_PATTERN = "ld_a.*";
    String COMBINE_PATTERN = "ld_c.*";

    @Autowired
    private TelemetryItemDao telemetryItemDao;
    @Autowired
    ColumnUtil columnUtil;

    @Value("${telemetry.source.csv.root}")
    private String csvSourceFolder;

    /**
     * Imports CSV files from specified folder on csvSourceFolder into DB
     */
    public void csvToDb(){
        CSVParser parser = new CSVParserBuilder().withIgnoreQuotations(true).withSeparator(';').build();

        List<String> allFiles = queryCsvFiles(csvSourceFolder);

        for (String file: allFiles) {
            try {
                importSingleFile(parser, file);
            } catch (IOException | CsvValidationException e) {
                log.error("Error while importing file");
            }
            moveFileToSubfolder(file);
        }
    }

    /**
     * Transforms a CSV header to a database-friendly header.
     *
     * @param csvHeader CSV header.
     * @return Transformed header in a database-friendly format.
     */

    private String csvToDbHeader(String csvHeader) {
        log.debug("CSV header received: {}", csvHeader);
        String withoutBrackets = removeTextInBrackets(csvHeader);
        String withoutSpecialChars = removeNonAlphanumeric(withoutBrackets);
        String dbHeader = toPascalCase(withoutSpecialChars);
        log.debug("DB header after transformation: {}", dbHeader);
        return dbHeader;
    }

    /**
     * Imports a single CSV file into the database.
     *
     * @param parser CSVParser bean.
     * @param fullFilePath Location of the CSV file.
     */
    private void importSingleFile(CSVParser parser, String fullFilePath) throws IOException, CsvValidationException {
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

            String machineType = getMachineType(fullFilePath);

            while((singleRow = reader.readNext()) != null){
                TelemetryItem telItem = TelemetryItem.builder().build();
                List<TelemetryProperty> propsForSingleItem = new ArrayList<>();

                TelemetryProperty machineTypeProp = TelemetryProperty.builder()
                        .telPropName(MACHINE_TYPE_PROP)
                        .telPropValue(machineType)
                        .telPropType(columnUtil.getColumnType(MACHINE_TYPE_PROP))
                        .telItem(telItem)
                        .build();
                propsForSingleItem.add(machineTypeProp);

                for (int i = 0; i< headers.length; i++){

                    String value = singleRow[i];

                    // Seems like NA means not applicable, according to CSV samples
                    // In such case, skip property save
                    if (value.equals(NOT_APPLICABLE)){
                        continue;
                    }

                    value = convertSpecificTypesToString(headers, i, value, singleRow);

                    TelemetryProperty prop = TelemetryProperty.builder()
                            .telPropName(headers[i])
                            .telPropValue(value)
                            .telPropType(columnUtil.getColumnType(headers[i]))
                            .telItem(telItem)
                            .build();
                    propsForSingleItem.add(prop);

                }
                telItem.setTelProps(propsForSingleItem);
                telemetryItems.add(telItem);
            }
            telemetryItemDao.saveAll(telemetryItems);
        } catch (FileNotFoundException e) {
            log.error("File {} not found. Skipping.", fullFilePath);
            throw e;
        } catch (IOException e) {
            log.error("File {} cannot be read. Skipping.", fullFilePath);
            throw e;
        } catch (CsvValidationException e) {
            log.error("File {} is corrupted or not in appropriate CSV format. Skipping.", fullFilePath);
            throw e;
        }
    }

    private String getMachineType(String fullFilePath) {
        String fileName = fullFilePath.substring(fullFilePath.lastIndexOf("\\") + 1);
        String lowercaseFileName = fileName.toLowerCase(); // Convert to lowercase for case-insensitive comparison

        if (lowercaseFileName.matches(TRACTOR_PATTERN)) {
            return MACHINE_TYPE_TRACTOR;
        } else if (lowercaseFileName.matches(COMBINE_PATTERN)) {
            return MACHONE_TYPE_COMBINE;
        } else {
            log.info("Unknown machine type: {}", fullFilePath );
            return MACHINE_TYPE_UNKNOWN;
        }
    }

    private String convertSpecificTypesToString(String[] headers, int i, String value, String[] singleRow) {
        // Database design suggest that all property values are saved in tel_prop_value as strings.
        // A bit of transformation, so it is easier for comparison later.
        switch (columnUtil.getColumnType(headers[i])) {
            case DATETIME:
                //if we convert datetime to int (ms)
                try {
                    return String.valueOf(columnUtil.dateFormat.parse(value).toInstant().toEpochMilli());
                } catch (ParseException e) {
                    log.error("Wrong data format. Csv column skipped: {}", Arrays.toString(singleRow));
                    break;
                }
            case BOOLEAN:
                //here are all possible values for boolean type, according to the CSV file.
                //this may be also exported into some kind of enum
                String lowerCaseValue = value.toLowerCase();
                return String.valueOf(lowerCaseValue.equals("yes")
                        || lowerCaseValue.equals("active")
                        || lowerCaseValue.equals("on")
                        || lowerCaseValue.equals("true")
                        || lowerCaseValue.equals("1") ? 1 : 0);
            default:
                // No transformation needed.
                return value;
        }
        // Header is not predefined. Consider it string.
        return value;
    }

    private List<String> queryCsvFiles(String folderPath) {
        List<String> csvFilePaths = new ArrayList<>();

        // Create a File object for the folder
        File folder = new File(folderPath);

        // Check if the path corresponds to a directory
        if (folder.isDirectory()) {
            // Create a FilenameFilter to filter CSV files starting with ld_a or ld_c
            FilenameFilter csvFileFilter = (dir, name) ->
                    (name.toLowerCase().startsWith("ld_a")
                            || name.toLowerCase().startsWith("ld_c")) &&
                            name.toLowerCase().endsWith(".csv");

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

    private void moveFileToSubfolder(String filePath) {
        File sourceFile = new File(filePath);

        // Check if the source file exists
        if (!sourceFile.exists()) {
            log.error("Source file does not exist: {}", sourceFile);
            return;
        }

        // Create the subfolder within the same directory as the source file
        File subfolder = new File(sourceFile.getParent(), PROCESSED_SUBFOLDER);
        if (!subfolder.exists()) {
            boolean created = subfolder.mkdir();
            if (!created) {
                log.error("Failed to create subfolder: {}", PROCESSED_SUBFOLDER);
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

}
