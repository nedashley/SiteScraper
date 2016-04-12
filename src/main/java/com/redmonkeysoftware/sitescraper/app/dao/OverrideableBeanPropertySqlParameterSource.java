package com.redmonkeysoftware.sitescraper.app.dao;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

public class OverrideableBeanPropertySqlParameterSource extends BeanPropertySqlParameterSource {

    private final Map<String, Object> additionals = new HashMap<>();

    public OverrideableBeanPropertySqlParameterSource(Object bean) {
        super(bean);
    }

    public OverrideableBeanPropertySqlParameterSource(Object bean, String name, Object value) {
        super(bean);
        this.putParameter(name, value);
    }

    public OverrideableBeanPropertySqlParameterSource(Object bean, Map<String, Object> params) {
        super(bean);
        this.putAll(params);
    }

    public final void putParameter(String name, Object value) {
        additionals.put(name, value);
    }

    public final void putAll(Map<String, Object> params) {
        additionals.putAll(params);
    }

    @Override
    public boolean hasValue(String paramName) {
        if (additionals.containsKey(paramName)) {
            return true;
        }
        return super.hasValue(paramName);
    }

    @Override
    public Object getValue(String paramName) throws IllegalArgumentException {
        Object result;
        if (additionals.containsKey(paramName)) {
            result = additionals.get(paramName);
        } else {
            result = super.getValue(paramName);
        }
        if (result instanceof LocalDate) {
            result = Date.valueOf((LocalDate) result);
        } else if (result instanceof LocalDateTime) {
            result = Timestamp.valueOf((LocalDateTime) result);
        } else if (result instanceof LocalTime) {
            result = Time.valueOf((LocalTime) result);
        }
        return result;
    }

    @Override
    public String[] getReadablePropertyNames() {
        String[] result = super.getReadablePropertyNames();
        for (String key : additionals.keySet()) {
            if (!ArrayUtils.contains(result, key)) {
                ArrayUtils.add(result, key);
            }
        }
        return result;
    }

    @Override
    public int getSqlType(String paramName) {
        try {
            Object value = getValue(paramName);
            if (value == null) {
                return Types.NULL;
            } else {
                return StatementCreatorUtils.javaTypeToSqlParameterType(value.getClass());
            }
        } catch (IllegalArgumentException iae) {
        }
        return super.getSqlType(paramName);
    }
}
