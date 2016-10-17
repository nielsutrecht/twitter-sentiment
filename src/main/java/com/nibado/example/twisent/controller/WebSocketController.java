package com.nibado.example.twisent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final static Logger LOG = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/status")
    @SendTo("/topic/status")
    public Update getStatus() {
        LOG.debug("getStatus()");
        return new Update("Connected", null);
    }

    public static class Update {
        public final String text;
        public final String profileImageURL;

        public Update(String text, String profileImageURL) {
            this.text = text;
            this.profileImageURL = profileImageURL;
        }
    }
}