package com.telemetry.restservice.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.telemetry.restservice.model.TelemetryItemDTO;
import java.io.IOException;
import java.util.Map;


/**
 * Custom Jackson JSON serializer for serializing TelemetryItemDTO objects.
 */
public class TelemetryItemDTOSerializer extends JsonSerializer<TelemetryItemDTO> {

    /**
     * Serializes a TelemetryItemDTO object as a JSON object with key-value pairs.
     *
     * @param telemetryItemDTO   The TelemetryItemDTO object to be serialized.
     * @param jsonGenerator      The JsonGenerator used to write the JSON content.
     * @param serializerProvider The SerializerProvider for accessing serializers.
     * @throws IOException If an I/O error occurs during serialization.
     */
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
