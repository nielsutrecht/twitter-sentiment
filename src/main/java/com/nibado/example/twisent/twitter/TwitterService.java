package com.nibado.example.twisent.twitter;

import com.nibado.example.twisent.WebSocketController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import twitter4j.*;

@Service
public class TwitterService {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterService.class);
    @Value("${twitter.language}")
    private String language;

    @Value("${twitter.filter}")
    private String filter;
    @Autowired
    private SimpMessagingTemplate broker;

    public void start() {
        LOG.info("Filtering messages with language {} and filter {}", language, filter);
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new Listener());

        FilterQuery fq = new FilterQuery().language(language).track(filter);

        twitterStream.filter(fq);
    }

    public void updateStatus(Status status) {
        LOG.debug("Message from {}: {}", status.getUser().getName(), status.getText());
        broker.convertAndSend("/topic/status", new WebSocketController.Update(status.getText(), status.getUser().getProfileImageURL()));
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
