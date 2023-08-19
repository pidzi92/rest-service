# rest-service
This is simple app that is processing CSV files and importing rows into in memory H@ database.

### DB
In memory H2 DB is used.

DB consists of two tables. 

![img.png](img.png)

This can be optimized furthermore:

1. Create vehicle table with ID and Serial Number and create a relation to property_item via id.
Benefit: In case of changing SN, all telemetry data will get new values
2. Create telemetry_property_definition table that will have property_name and type. Then create a relation to telemetry_property and telemetry_property_definition