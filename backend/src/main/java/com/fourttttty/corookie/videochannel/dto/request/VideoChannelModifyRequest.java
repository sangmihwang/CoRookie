package com.fourttttty.corookie.videochannel.dto.request;

import jakarta.validation.constraints.NotBlank;

public record VideoChannelModifyRequest(@NotBlank String name) {
}
