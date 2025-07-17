package com.kf.mathtrainer.controller;

import com.kf.mathtrainer.dto.SingleChallengeDTO;
import com.kf.mathtrainer.service.SingleChallengeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/single_challenge")
public class SingleChallengeController {

    private final SingleChallengeService singleChallengeService;

    public SingleChallengeController(SingleChallengeService singleChallengeService) {
        this.singleChallengeService = singleChallengeService;
    }

    @GetMapping(
            produces = "application/json")
    public ResponseEntity<SingleChallengeDTO> getSingleChallenge() {
        final SingleChallengeDTO singleChallenge = singleChallengeService.get_random();
        return new ResponseEntity<>(singleChallenge, HttpStatus.OK);
    }

    @PatchMapping(
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<SingleChallengeDTO> guessChallenge(
            @RequestBody SingleChallengeDTO guessedChallenge) {
        HttpStatus staus = null;
        if (singleChallengeService.solve(guessedChallenge)) {
            staus = HttpStatus.OK;
        } else {
            staus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(guessedChallenge, staus);
    }
}
