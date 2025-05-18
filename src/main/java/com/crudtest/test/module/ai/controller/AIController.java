package com.crudtest.test.module.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AIController {

    // private final ChatClient chatClient;

    // public AIController(ChatClient.Builder chatClientBuilder) {
    // this.chatClient = chatClientBuilder.build();
    // }

    // @GetMapping
    // public String getAIResponse(String input) {
    // String response = "Hola, como estas?";
    // return this.chatClient.prompt()
    // .user(response)
    // .call()
    // .content();
    // }

}
