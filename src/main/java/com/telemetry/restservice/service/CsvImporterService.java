package com.telemetry.restservice.service;

/**
 * Service interface for importing CSV telemetry items into DB.
 */
public interface CsvImporterService {

    /**
     * Imports telemetry files from defined directory into DB
     */
    void csvToDb();
}
