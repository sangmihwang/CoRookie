package com.fourttttty.corookie.issue.util;

import com.fourttttty.corookie.issue.domain.IssuePriority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter
public class IssuePriorityConverter implements AttributeConverter<IssuePriority, Integer> {
    @Override
    public Integer convertToDatabaseColumn(IssuePriority column) {
        if (Objects.isNull(column)) {
            return null;
        }
        return column.getValue();
    }

    @Override
    public IssuePriority convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return IssuePriority.from(dbData);
    }
}
