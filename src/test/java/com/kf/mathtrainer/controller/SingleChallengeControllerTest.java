package com.kf.mathtrainer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kf.mathtrainer.dto.SingleChallengeDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class SingleChallengeControllerTest {

    private final WebApplicationContext context;
    private final ObjectMapper objectMapper;

    @Autowired
    SingleChallengeControllerTest(
            WebApplicationContext context,
            ObjectMapper objectMapper){
        this.context = context;
        this.objectMapper = objectMapper;
    }

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context).build();
    }

    @Test
    void testGetSingleChallenge() throws Exception {
        mockMvc.perform(
                        get("/single_challenge")
                                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.challenge").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.solution").isEmpty());
    }

    @Test
    void testGuessChallengeValid() throws Exception {
        String responseBody = mockMvc.perform(
                        get("/single_challenge")
                                .contentType("application/json"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        SingleChallengeDTO dto = objectMapper.readValue(responseBody, SingleChallengeDTO.class);
        String challenge = dto.challenge();
        int solution = GuessChallengeSSolve(challenge);
        SingleChallengeDTO solutionDTO = new SingleChallengeDTO(dto.id(), dto.challenge(), solution);

        mockMvc.perform(
                        patch("/single_challenge")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(solutionDTO))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.challenge").value(solutionDTO.challenge()))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.solution").value(solutionDTO.solution()));
    }

    @Test
    void testGuessChallengeInvalid() throws Exception {
        String responseBody = mockMvc.perform(
                        get("/single_challenge")
                                .contentType("application/json"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        SingleChallengeDTO dto = objectMapper.readValue(responseBody, SingleChallengeDTO.class);
        String challenge = dto.challenge();
        int solution = GuessChallengeSSolve(challenge) + 1;
        SingleChallengeDTO solutionDTO = new SingleChallengeDTO(dto.id(), dto.challenge(), solution);

        mockMvc.perform(
                        patch("/single_challenge")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(solutionDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.challenge").value(solutionDTO.challenge()))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.solution").value(solutionDTO.solution()));
    }

    private int GuessChallengeSSolve (String challenge) throws IllegalArgumentException {
        return switch ( challenge )
        {
            case "1+1" -> 2;
            case "2+2" -> 4;
            case "3+3" -> 6;
            default -> throw new IllegalArgumentException("Unknown challenge: " + challenge);
        };
    }

}
