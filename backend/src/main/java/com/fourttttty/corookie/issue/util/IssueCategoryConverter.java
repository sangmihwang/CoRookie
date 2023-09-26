package com.fourttttty.corookie.issue.util;

import com.fourttttty.corookie.issue.domain.IssueCategory;
import org.springframework.core.convert.converter.Converter;

public class IssueCategoryConverter implements Converter<String, IssueCategory> {
    @Override
    public IssueCategory convert(String source) {
        return IssueCategory.from(source);
    }
}
