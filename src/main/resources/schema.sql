CREATE SCHEMA IF NOT EXISTS telemetry;
SET SCHEMA telemetry;
CREATE TABLE telemetry_item(tel_id int primary key auto_increment);
CREATE TABLE telemetry_property(
    tel_prop_id int primary key auto_increment
    ,tel_prop_name varchar(255) not null
    ,tel_prop_value varchar(255)
    ,tel_prop_type int not null
    ,tel_id int not null
    ,foreign key (tel_id) references telemetry_item(tel_id)
);