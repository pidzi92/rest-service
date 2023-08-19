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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CsvUtil {

    @Autowired
    private TelemetryItemDao telemetryItemDao;

    public void csvToDb(){
        CSVParser parser = new CSVParserBuilder().withIgnoreQuotations(true).withSeparator(';').build();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader("C:\\CSV\\LD_A5304997_20230331_20230401.csv")).withCSVParser(parser).build()){
            List<TelemetryItem> telemetryItems = new ArrayList<>();
            String[] singleRow = reader.readNext();
            String[] headers = singleRow;
            if (headers == null){
                log.error("File does not have headers. Skipping.");
                return;
            }

            while((singleRow = reader.readNext()) != null){
                List<TelemetryProperty> propsForSingleItem = new ArrayList<>();
                for (int i=0; i< headers.length; i++){
                    //TODO parse column names and check types
                    TelemetryProperty prop = TelemetryProperty.builder().telPropName(headers[i]).telPropValue(singleRow[i]).telPropType(TelemetryPropertyTypeEnum.STRING).build();
                    propsForSingleItem.add(prop);

                }
                TelemetryItem telItem = TelemetryItem.builder().telProps(propsForSingleItem).build();
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
}
