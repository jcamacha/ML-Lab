package org.example.controllers;

import org.example.services.PythonApiService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController

@RequestMapping("/ml")

@CrossOrigin(origins = "*")

public class MLController {

    @Autowired

    private PythonApiService pythonApiService;

    @PostMapping("/linear")

    public String linearRegression(

            @RequestBody
            Map<String, Object> requestData

    ) {

        return pythonApiService
                .runLinearRegression(
                        requestData
                );
    }
    @PostMapping("/knn")

    public String knn(

            @RequestBody
            Map<String, Object> requestData

    ) {

        return pythonApiService
                .runKNN(
                        requestData
                );
    }
    @PostMapping("/logistic")

    public String logistic(

            @RequestBody
            Map<String, Object> requestData

    ) {

        return pythonApiService
                .runLogistic(
                        requestData
                );
    }


}

