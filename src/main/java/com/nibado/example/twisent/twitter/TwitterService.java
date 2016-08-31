package com.nibado.example.twisent.twitter;

import com.nibado.example.twisent.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import twitter4j.*;

@Service
public class TwitterService {
    @Autowired
    private SimpMessagingTemplate broker;

    public void start() {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new Listener());
        // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        //twitterStream.f

        FilterQuery fq = new FilterQuery().language("en").track("java");

        twitterStream.filter(fq);
    }

    public void updateStatus(Status status) {
        System.out.println(status.getLang() + ":" + status.getUser().getName() + " : " + status.getText());
        broker.convertAndSend("/topic/status", new WebSocketController.Update(status.getText()));
    }

    private class Listener implements StatusListener {

        @Override
        public void onStatus(Status status) {
            updateStatus(status);
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        }

        @Override
        public void onTrackLimitationNotice(int i) {
        }

        @Override
        public void onScrubGeo(long l, long l1) {
        }

        @Override
        public void onStallWarning(StallWarning stallWarning) {
        }

        @Override
        public void onException(Exception e) {
        }
    }
}
