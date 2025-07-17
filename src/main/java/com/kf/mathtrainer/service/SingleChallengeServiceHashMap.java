package com.kf.mathtrainer.service;

import com.kf.mathtrainer.dto.SingleChallengeDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SingleChallengeServiceHashMap implements  SingleChallengeService {

    private final LinkedList<SingleChallengeDTO> challenges = new LinkedList<>();

    public SingleChallengeServiceHashMap () {
        challenges.add(new SingleChallengeDTO(UUID.randomUUID(), "1+1", 2));
        challenges.add(new SingleChallengeDTO(UUID.randomUUID(), "2+2", 4));
        challenges.add(new SingleChallengeDTO(UUID.randomUUID(), "3+3", 6));
    }

    public SingleChallengeDTO get_random() {
        Random rand = new Random();
        SingleChallengeDTO challenge = challenges.get(rand.nextInt(challenges.size()));
        return new SingleChallengeDTO(challenge.id(), challenge.challenge(), null);
    }

    public boolean solve(SingleChallengeDTO guessedSolution) {
        return challenges
                .stream()
                .filter(item -> item.id().equals(guessedSolution.id()))
                .findFirst()
                .map(match -> Objects.equals(match.solution(), guessedSolution.solution()))
                .orElse(false);
    }
}
