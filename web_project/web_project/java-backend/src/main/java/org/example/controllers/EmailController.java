package org.example.controllers;

import org.example.services.EmailService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/email")

@CrossOrigin(origins = "*")

public class EmailController {

    @Autowired

    private EmailService emailService;

    @PostMapping("/send")

    public String sendEmail(

            @RequestParam String to

    ) {

        emailService.sendEmail(

                to,

                "ML Lab Notification",

                "Your experiment was executed successfully."

        );

        return "Email sent successfully!";
    }
}