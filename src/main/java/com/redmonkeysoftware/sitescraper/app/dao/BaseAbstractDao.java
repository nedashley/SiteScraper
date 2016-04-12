package com.redmonkeysoftware.sitescraper.app.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public abstract class BaseAbstractDao extends NamedParameterJdbcDaoSupport {

    @Autowired
    @Qualifier("dataSource")
    protected DataSource dataSource;
    @Autowired
    protected ObjectMapper mapper;
    protected String selectIdSql = "select nextval(?)";

    @PostConstruct
    public void init() {
        setDataSource(dataSource);
    }

    protected synchronized Long getNextId(String sequence) {
        this.logger.debug("Generating id using sequence: " + sequence);
        Long id = this.getJdbcTemplate().queryForObject(selectIdSql, Long.class, sequence);
        this.logger.debug("Generated: " + id + " as next id with sequence: " + sequence);
        return id;
    }

    protected synchronized String getRandomId() {
        return UUID.randomUUID().toString();
    }

    protected String getReference(String prefix, Long id) {
        String result = "";
        int lettercode = (int) (id / 999999);
        String letter = lettercode > -1 && lettercode < 26 ? String.valueOf((char) (lettercode + 65)) : null;
        result = prefix + StringUtils.upperCase(letter + StringUtils.leftPad(id.toString(), 6, "0"));
        return result;
    }

    protected <T extends Enum<T>> T toEnum(Class<T> enumType, String name) {
        return StringUtils.isBlank(name) ? null : Enum.valueOf(enumType, name);
    }

    protected Long getLong(ResultSet rs, String name) throws SQLException {
        return rs.getObject(name) != null ? rs.getLong(name) : null;
    }

    protected Integer getInteger(ResultSet rs, String name) throws SQLException {
        return rs.getObject(name) != null ? rs.getInt(name) : null;
    }

    protected Short getShort(ResultSet rs, String name) throws SQLException {
        return rs.getObject(name) != null ? rs.getShort(name) : null;
    }

    protected Boolean getBoolean(ResultSet rs, String name) throws SQLException {
        return rs.getObject(name) != null ? rs.getBoolean(name) : null;
    }

    protected LocalDate getLocalDate(ResultSet rs, String name) throws SQLException {
        Date d = rs.getObject(name) != null ? new Date(rs.getTimestamp(name).getTime()) : null;
        return d != null ? d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    protected LocalDateTime getLocalDateTime(ResultSet rs, String name) throws SQLException {
        Date d = rs.getObject(name) != null ? new Date(rs.getTimestamp(name).getTime()) : null;
        return d != null ? d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
    }

    protected String toJson(Object obj, String fallback) {
        if (obj != null) {
            try {
                return mapper.writeValueAsString(obj);
            } catch (IOException ioe) {
                logger.warn("Error mapping to JSON", ioe);
            }
        }
        return fallback;
    }

    protected <T extends Object> T getJson(String json, Class<T> type, T fallback) {
        if (StringUtils.isNotBlank(json)) {
            try {
                return mapper.readValue(json, type);
            } catch (IOException e) {
                logger.warn("Error mapping from JSON", e);
            }
        }
        return fallback;
    }

    protected <T> List<T> getJsonList(String json, Class<T> type) {
        if (StringUtils.isNotBlank(json)) {
            try {
                return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, type));
            } catch (IOException e) {
                logger.warn("Error mapping from JSON", e);
            }
        }
        return new ArrayList<>();
    }

    public class StringMapper implements RowMapper<String> {

        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString(1);
        }
    }

    public class LongMapper implements RowMapper<Long> {

        @Override
        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getLong(1);
        }
    }
}
