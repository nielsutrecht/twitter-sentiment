package com.nibado.example.twisent.twitter;

import org.springframework.stereotype.Service;
import twitter4j.*;

@Service
public class TwitterService {
    public void start() {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new Listener());
        // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        twitterStream.sample();
    }

    private static class Listener implements StatusListener {

        @Override
        public void onStatus(Status status) {
            System.out.println(status.getUser().getName() + " : " + status.getText());
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
