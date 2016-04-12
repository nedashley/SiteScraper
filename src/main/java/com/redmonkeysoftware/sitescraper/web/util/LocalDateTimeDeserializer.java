package com.redmonkeysoftware.sitescraper.web.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        if (parser.hasTokenId(JsonTokenId.ID_STRING)) {
            String string = parser.getText().trim();
            if (string.length() == 0) {
                return null;
            }
            if (StringUtils.containsIgnoreCase(string, "z")) {
                return LocalDateTime.parse(string, DateTimeFormatter.ISO_ZONED_DATE_TIME);
            }
            return LocalDateTime.parse(string, DEFAULT_FORMATTER);
        }
        if (parser.isExpectedStartArrayToken()) {
            if (parser.nextToken() == JsonToken.END_ARRAY) {
                return null;
            }
            int year = parser.getIntValue();

            parser.nextToken();
            int month = parser.getIntValue();

            parser.nextToken();
            int day = parser.getIntValue();

            parser.nextToken();
            int hour = parser.getIntValue();

            parser.nextToken();
            int minute = parser.getIntValue();

            if (parser.nextToken() != JsonToken.END_ARRAY) {
                int second = parser.getIntValue();

                if (parser.nextToken() != JsonToken.END_ARRAY) {
                    int partialSecond = parser.getIntValue();
                    if (partialSecond < 1_000
                            && !context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)) {
                        partialSecond *= 1_000_000; // value is milliseconds, convert it to nanoseconds
                    }
                    if (parser.nextToken() != JsonToken.END_ARRAY) {
                        throw context.wrongTokenException(parser, JsonToken.END_ARRAY, "Expected array to end.");
                    }
                    return LocalDateTime.of(year, month, day, hour, minute, second, partialSecond);
                }
                return LocalDateTime.of(year, month, day, hour, minute, second);
            }
            return LocalDateTime.of(year, month, day, hour, minute);
        }
        throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected array or string.");
    }

}
