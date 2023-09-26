package com.fourttttty.corookie.issue.util;

import com.fourttttty.corookie.issue.domain.IssueProgress;
import org.springframework.core.convert.converter.Converter;

public class IssueProgressConverter implements Converter<String, IssueProgress> {
    @Override
    public IssueProgress convert(String source) {
        return IssueProgress.from(source);
    }
}
