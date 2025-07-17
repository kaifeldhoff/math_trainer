package com.kf.mathtrainer.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;
import java.util.UUID;

@Builder
public record SingleChallengeDTO(
        UUID id,
        String challenge,
        Integer solution
) { }