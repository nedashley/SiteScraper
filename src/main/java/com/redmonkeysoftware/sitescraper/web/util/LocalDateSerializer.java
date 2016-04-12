package com.redmonkeysoftware.sitescraper.web.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void serialize(LocalDate date, JsonGenerator generator, SerializerProvider provider) throws IOException {
        /*if (useTimestamp(provider)) {
         generator.writeStartArray();
         generator.writeNumber(date.getYear());
         generator.writeNumber(date.getMonthValue());
         generator.writeNumber(date.getDayOfMonth());
         generator.writeEndArray();
         } else {*/
        String str = (DEFAULT_FORMATTER == null) ? date.toString() : date.format(DEFAULT_FORMATTER);
        generator.writeString(str);
        //}
    }
}
