package com.nibado.example.twisent.sentiment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    private int score;
    private int words;
}
