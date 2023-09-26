package com.fourttttty.corookie.textchannel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultChannel {
    NOTICE("1-공지"),
    FREE("2-자유"),
    BACKEND("3-Backend"),
    FRONTEND("4-Frontend");

    private final String channelName;
}
