package com.fourttttty.corookie.videochannel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultVideoChannel {
    CONFERENCE("회의"),
    CHAT("잡담");

    private final String channelName;
}
