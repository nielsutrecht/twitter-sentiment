package com.nibado.example.twisent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {
    private final static Logger LOG = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private SimpMessagingTemplate broker;

    private List<String> colors;

    @MessageMapping("/status")
    @SendTo("/topic/status")
    public Update getStatus() {
        LOG.debug("getStatus()");
        return new Update("Hello!");
    }

    public static class Update {
        public final String message;

        public Update(String message) {
            this.message = message;
        }
    }
}