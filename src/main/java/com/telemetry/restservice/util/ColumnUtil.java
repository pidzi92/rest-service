package com.telemetry.restservice.util;

import com.telemetry.restservice.model.Filter;
import com.telemetry.restservice.model.TelemetryPropertyTypeEnum;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;

@Component
public class ColumnUtil {

    //date format for CSV columns with date type and for filters
    public SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy, h:mm:ss a");

    /**
     * Returns column type for predefined headers. If the header is not predefined, it will return string.
     *
     * @param dbHeader - Title of column
     * @return - @{@link TelemetryPropertyTypeEnum} object, specifying column type
     */
    public TelemetryPropertyTypeEnum getColumnType(String dbHeader){

        switch(dbHeader){
            case "DateTime":
                return TelemetryPropertyTypeEnum.DATETIME;
            case "AllWheelDriveStatus":
            case "ActualStatusOfCreeper":
            case "Chopper":
            case "FrontAttachmentOnOff":
            case "WorkingPosition":
            case "GrainTankUnloading":
            case "MainDriveStatus":
            case "GrainTank70":
            case "GrainTank100":
            case "YieldMeasurement":
            case "ReturnsAugerMeasurement":
            case "MoistureMeasurement":
            case "AutoPilotStatus":
                return TelemetryPropertyTypeEnum.BOOLEAN;
            case "EngineSpeed":
            case "EngineLoad":
            case "CoolantTemperature":
            case "SpeedFrontPto":
            case "SpeedRearPto":
            case "CurrentGearShift":
            case "ParkingBrakeStatus":
            case "TransverseDifferentialLockStatus":
            case "DrumSpeed":
            case "FanSpeed":
            case "RotorStrawWalkerSpeed":
            case "NoOfPartialWidths":
            case "MaxNoOfPartialWidths":
            case "FeedRakeSpeed":
            case "ConcavePosition":
            case "UpperSievePosition":
            case "LowerSievePosition":
            case "RadialSpreaderSpeed":
            case "GrainInReturns":
            case "SpecificCropWeight":
            case "CruisePilotStatus":
            case "SeparationSensitivity":
            case "SieveSensitivity":
                return TelemetryPropertyTypeEnum.INTEGER;
            case "GpsLongitude":
            case "GpsLatitude":
            case "TotalWorkingHoursCounter":
            case "FuelConsumption":
            case "GroundSpeedGearbox":
            case "GroundSpeedRadar":
            case "AmbientTemperature":
            case "GroundSpeed":
            case "SeparationLosses":
            case "SieveLosses":
            case "DieselTankLevel":
            case "GrainMoistureContent":
            case "Throughput":
            case "ChannelPosition":
            case "RateOfWork":
            case "Yield":
            case "QuantimeterCalibrationFactor":
                return TelemetryPropertyTypeEnum.DOUBLE;
            case "SerialNumber":
            case "TypeOfCrop":
            default:
                return TelemetryPropertyTypeEnum.STRING;
        }
    }
}
