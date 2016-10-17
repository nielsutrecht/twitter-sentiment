package com.nibado.example.twisent.sentiment;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnalyserTest {
    private AnalyserService analyser;
    @Before
    public void setup() {
        analyser = new AnalyserService();
        analyser.load(AnalyserTest.class.getResourceAsStream("/data/wordlist.txt"));
    }

    @Test
    public void testScore() throws Exception {
        Score score = analyser.score("  ability     ability\tability  ");

        assertThat(score.getScore()).isEqualTo(6);
        assertThat(score.getWords()).isEqualTo(3);
    }
}