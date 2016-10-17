package com.nibado.example.twisent.sentiment;

import org.springframework.stereotype.Service;
import twitter4j.Status;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyserService {
    private Map<String, Integer> list;

    public AnalyserService() {
        this.list = new HashMap<>();
    }

    public Score analyse(Status status) {
        return score(status.getText());
    }

    Score score(String text) {
        int score = 0;

        String[] words = text.split("\\s+");

        for(String word : words) {
            score += list.getOrDefault(word, 0);
        }

        return new Score(score, words.length);
    }

    public void load(InputStream ins) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(ins))) {
            reader.lines()
                    .map(String::trim)
                    .map(l -> l.split("\\s+"))
                    .filter(a -> a.length == 2)
                    .forEach(a -> list.put(a[0], Integer.parseInt(a[1])));
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void init() {
        load(AnalyserService.class.getResourceAsStream("/data/wordlist.txt"));
    }
}
