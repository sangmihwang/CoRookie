package com.fourttttty.corookie.issue.util;

import org.springframework.core.convert.converter.Converter;

public class IssueFilterTypeConverter implements Converter<String, IssueFilterType> {

    @Override
    public IssueFilterType convert(String source) {
        return IssueFilterType.from(source);
    }
}
