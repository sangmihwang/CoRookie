package com.fourttttty.corookie.textchannel.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TextChannelModifyRequest(@NotBlank String name) {
}
