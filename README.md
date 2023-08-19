# rest-service
This is simple app that is processing CSV files and importing rows into in memory H@ database.

## DB
In memory H2 DB is used. schema.sql is read on startup and database is set up.

DB consists of two tables. 

![img.png](img.png)

This can be optimized furthermore:

1. Create vehicle table with ID and Serial Number and create a relation to property_item via id.
Benefit: In case of changing SN, all telemetry data will get new values
2. Create telemetry_property_definition table that will have property_name and type. Then create a relation to telemetry_property and telemetry_property_definition
3. Using some king of DB versioning system, like [LiquiBase](https://www.liquibase.org/)

## CSV parsing

All CSV files starting with LD_A and LD_C are parsed, one by one.
Parsing can be optimized by implementing parsing in batches of rows and/or threads.

First the header is read. Each row in csv represents single telemetry item. Property values are dynamically assigned from column headers.

### Parsing headers

For each header there is a parsing process
1. Everything is removed in between [], including square brackets. This will remove unit from the header
2. All special characters are replaced with empty space
3. Convert Words To PascalCase
4. Remove all spaces

Example:
Ambient temperature [°C] -> AmbientTemperature

### Moving Processed Files
After the file is processed, it will be moved into PROCESSED subfolder

## DAO
This layer can be optimized by using Page and Pageable
For more info check this links

[Page](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html)

[Pageable](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Pageable.html)

## Endpoints

Once the application is started, endpoints will be available on port 8080 by default. To change the port, please check application.properties

## Other
1. There is just one application.properties file. Multiple files can be added for different environments so that spring-boot profiles can be utilized