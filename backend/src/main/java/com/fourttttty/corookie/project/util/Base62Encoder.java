package com.fourttttty.corookie.project.util;

import org.springframework.stereotype.Component;

@Component
public class Base62Encoder {

    public static final int RADIX = 62;
    private static final String BASE62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public String encoding(long value) {
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % RADIX)));
            value /= RADIX;
        }
        return sb.toString();
    }

    public long decoding(String value) {
        long sum = 0;
        long power = 1;
        for (int i = 0; i < value.length(); i++) {
            sum += BASE62.indexOf(value.charAt(i)) * power;
            power *= RADIX;
        }
        return sum;
    }
}
