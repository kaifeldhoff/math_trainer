package com.kf.mathtrainer.service;

import com.kf.mathtrainer.dto.SingleChallengeDTO;

public interface SingleChallengeService {

    SingleChallengeDTO get_random();

    boolean solve(SingleChallengeDTO solution);

}
