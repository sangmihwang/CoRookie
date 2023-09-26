package com.fourttttty.corookie.thread.util;

import com.fourttttty.corookie.thread.domain.Emoji;
import org.springframework.core.convert.converter.Converter;

public class EmojiConverter implements Converter<String, Emoji> {
    @Override
    public Emoji convert(String source) {
        return Emoji.from(source);
    }
}
