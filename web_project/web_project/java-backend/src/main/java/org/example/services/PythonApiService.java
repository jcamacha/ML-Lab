package org.example.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.entities.Experiment;
import org.example.repositories.ExperimentRepository;

import java.util.Map;

@Service

public class PythonApiService {

    private final RestTemplate restTemplate =
            new RestTemplate();

    private final ExperimentRepository experimentRepository;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    // CONSTRUCTOR

    public PythonApiService(

            ExperimentRepository experimentRepository

    ) {

        this.experimentRepository =
                experimentRepository;
    }

    // LINEAR REGRESSION

    public String runLinearRegression(

            Map<String, Object> requestData

    ) {

        String url =
                "https://python-api-vj5u.onrender.com/linear/run";

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(
                        requestData,
                        headers
                );

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

        // SAVE EXPERIMENT

        try {

            JsonNode json =
                    objectMapper.readTree(
                            response.getBody()
                    );

            Experiment experiment =
                    new Experiment();

            experiment.setModel_name(
                    json.get("model").asText()
            );

            experiment.setDataset_name(
                    json.get("dataset").asText()
            );

            experiment.setAccuracy(0.0);

            experiment.setMse(
                    json.get("mse").asDouble()
            );

            experimentRepository.save(
                    experiment
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        return response.getBody();
    }

    // KNN

    public String runKNN(

            Map<String, Object> requestData

    ) {

        String url =
                "https://python-api-vj5u.onrender.com/knn/run";

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(
                        requestData,
                        headers
                );

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

        // SAVE EXPERIMENT

        try {

            JsonNode json =
                    objectMapper.readTree(
                            response.getBody()
                    );

            Experiment experiment =
                    new Experiment();

            experiment.setModel_name(
                    json.get("model").asText()
            );

            experiment.setDataset_name(
                    json.get("dataset").asText()
            );

            experiment.setAccuracy(
                    json.get("accuracy").asDouble()
            );

            experiment.setMse(0.0);

            experimentRepository.save(
                    experiment
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        return response.getBody();
    }

    // LOGISTIC REGRESSION

    public String runLogistic(

            Map<String, Object> requestData

    ) {

        String url =
                "https://python-api-vj5u.onrender.com/logistic/run";

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(
                        requestData,
                        headers
                );

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

        // SAVE EXPERIMENT

        try {

            JsonNode json =
                    objectMapper.readTree(
                            response.getBody()
                    );

            Experiment experiment =
                    new Experiment();

            experiment.setModel_name(
                    json.get("model").asText()
            );

            experiment.setDataset_name(
                    json.get("dataset").asText()
            );

            experiment.setAccuracy(
                    json.get("accuracy").asDouble()
            );

            experiment.setMse(0.0);

            experimentRepository.save(
                    experiment
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        return response.getBody();
    }
}