package com.nibado.example.twisent.twitter;

import com.nibado.example.twisent.controller.TweetDTO;
import com.nibado.example.twisent.sentiment.AnalyserService;
import com.nibado.example.twisent.sentiment.Score;
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

    @Autowired
    private AnalyserService analyser;

    public void start() {
        LOG.info("Filtering messages with language {} and filter {}", language, filter);
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new Listener());

        FilterQuery fq = new FilterQuery().language(language).track(filter);

        twitterStream.filter(fq);
    }

    public void updateStatus(Status status) {
        if(status.isRetweet()) {
            return;
        }
        Score score = analyser.analyse(status);

        LOG.debug("Message from {}: {} with score {}", status.getUser().getName(), status.getText(), score.getScore());

        TweetDTO tweet = new TweetDTO(status.getText(), status.getUser().getProfileImageURL(), score.getScore());

        broker.convertAndSend("/topic/status", tweet);
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
