package com.telemetry.restservice.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.telemetry.restservice.model.TelemetryItemDTO;
import java.io.IOException;
import java.util.Map;

public class TelemetryItemDTOSerializer extends JsonSerializer<TelemetryItemDTO> {
    @Override
    public void serialize(TelemetryItemDTO telemetryItemDTO, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        Map<String, String> telProps = telemetryItemDTO.getTelProps();
        for (Map.Entry<String, String> entry : telProps.entrySet()) {
            jsonGenerator.writeStringField(entry.getKey(), entry.getValue());
        }

        jsonGenerator.writeEndObject();
    }
}
