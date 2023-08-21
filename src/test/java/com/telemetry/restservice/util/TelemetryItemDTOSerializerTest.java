package com.telemetry.restservice.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.telemetry.restservice.model.TelemetryItemDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TelemetryItemDTOSerializerTest {

    @Test
    public void testTelemetryItemSerialization() throws IOException {
        TelemetryItemDTO telemetryItemDTO = new TelemetryItemDTO();
        Map<String, String> telemetryProps = new HashMap<>();
        telemetryProps.put("telPropName1", "telPropValue1");
        telemetryProps.put("telPropName2", "telPropValue2");
        telemetryItemDTO.setTelProps(telemetryProps);

        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        TelemetryItemDTOSerializer serializer = new TelemetryItemDTOSerializer();
        serializer.serialize(telemetryItemDTO, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeStartObject();
        Mockito.verify(jsonGenerator).writeStringField("telPropName1", "telPropValue1");
        Mockito.verify(jsonGenerator).writeStringField("telPropName2", "telPropValue2");
        Mockito.verify(jsonGenerator).writeEndObject();
    }

    @Test
    public void testNullTelemetryItemSerialization() throws IOException {
        TelemetryItemDTO telemetryItemDTO = null;

        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        TelemetryItemDTOSerializer serializer = new TelemetryItemDTOSerializer();
        serializer.serialize(telemetryItemDTO, jsonGenerator, serializerProvider);

        Mockito.verifyNoInteractions(jsonGenerator);
    }
    @Test
    public void testEmptyTelemetryPropertiesMapSerialization() throws IOException {
        TelemetryItemDTO telemetryItemDTO = new TelemetryItemDTO();
        telemetryItemDTO.setTelProps(Collections.emptyMap());

        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

        TelemetryItemDTOSerializer serializer = new TelemetryItemDTOSerializer();
        serializer.serialize(telemetryItemDTO, jsonGenerator, serializerProvider);

        Mockito.verify(jsonGenerator).writeStartObject();
        Mockito.verify(jsonGenerator).writeEndObject();
    }
}
