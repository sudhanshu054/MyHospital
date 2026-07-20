package com.hospital.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hospital.dto.AIConsultationDto;
import com.hospital.dto.AIConsultationRequest;
import com.hospital.entity.AIConsultation;
import com.hospital.entity.Patient;
import com.hospital.repository.AIConsultationRepository;
import com.hospital.service.AIConsultationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class AIConsultationServiceImpl implements AIConsultationService {
    private final AIConsultationRepository consultationRepository;
    private final String openAiApiKey;
    private final String openAiModel;
    private final RestTemplate restTemplate;

    private static final List<String> EMERGENCY_TRIGGERS = List.of(
            "chest pain", "stroke", "severe bleeding", "breathing difficulty",
            "shortness of breath", "loss of consciousness");

    public AIConsultationServiceImpl(AIConsultationRepository consultationRepository,
                                     @Value("${openai.api.key:}") String openAiApiKey,
                                     @Value("${openai.api.model:gpt-4o-mini}") String openAiModel,
                                     RestTemplate restTemplate) {
        this.consultationRepository = consultationRepository;
        this.openAiApiKey = openAiApiKey;
        this.openAiModel = openAiModel;
        this.restTemplate = restTemplate;
    }

    @Override
    public AIConsultationDto consult(Patient patient, AIConsultationRequest request) {
        boolean emergency = detectEmergency(request.getSymptoms());
        String response = buildAiResponse(request.getSymptoms(), emergency);
        AIConsultation consultation = AIConsultation.builder()
                .patient(patient)
                .symptoms(request.getSymptoms())
                .response(response)
                .category(emergency ? "EMERGENCY" : "GENERAL_HEALTH")
                .emergencyRecommended(emergency)
                .createdAt(Instant.now())
                .build();
        AIConsultation saved = consultationRepository.save(consultation);
        return AIConsultationDto.builder()
                .id(saved.getId())
                .patientId(patient.getId())
                .symptoms(saved.getSymptoms())
                .response(saved.getResponse())
                .category(saved.getCategory())
                .emergencyRecommended(saved.isEmergencyRecommended())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    private String buildAiResponse(String symptoms, boolean emergency) {
        if (emergency) {
            return "Your symptoms suggest a possible medical emergency. Please seek immediate medical "
                    + "attention or call emergency services. This AI consultation is informational only "
                    + "and is not a substitute for professional medical advice.";
        }

        String generated = callExternalAi(symptoms);
        String disclaimer = "\n\nDisclaimer: This AI consultation is informational only and is not a "
                + "substitute for professional medical advice. If symptoms worsen, visit a hospital.";
        return generated + disclaimer;
    }

    private String callExternalAi(String symptoms) {
        if (openAiApiKey == null || openAiApiKey.isBlank()) {
            return fallbackResponse(symptoms);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            Map<String, Object> systemMessage = Map.of(
                    "role", "system",
                    "content", "You are a careful, empathetic medical triage assistant for a hospital. "
                            + "Provide clear, general guidance for the described symptoms. Never prescribe "
                            + "specific medications. Always advise consulting a licensed physician.");
            Map<String, Object> userMessage = Map.of(
                    "role", "user",
                    "content", "Patient reports the following symptoms: " + symptoms);

            Map<String, Object> body = Map.of(
                    "model", openAiModel,
                    "messages", List.of(systemMessage, userMessage),
                    "temperature", 0.3,
                    "max_tokens", 500);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            String url = "https://api.openai.com/v1/chat/completions";
            OpenAiResponse response = restTemplate.postForObject(url, entity, OpenAiResponse.class);
            if (response != null && response.choices != null && !response.choices.isEmpty()) {
                return response.choices.get(0).message.content.trim();
            }
            return fallbackResponse(symptoms);
        } catch (Exception ex) {
            return fallbackResponse(symptoms);
        }
    }

    private String fallbackResponse(String symptoms) {
        return "Based on your symptoms, take rest, stay hydrated, and consult a licensed physician for "
                + "personalized care. Avoid taking prescription medicines without medical advice. "
                + "If symptoms worsen, visit the hospital immediately.";
    }

    private boolean detectEmergency(String symptoms) {
        String normalized = symptoms.toLowerCase();
        return EMERGENCY_TRIGGERS.stream().anyMatch(normalized::contains);
    }

    private static class OpenAiResponse {
        public List<Choice> choices;
    }

    private static class Choice {
        public Message message;
    }

    private static class Message {
        public String role;
        public String content;
    }
}
