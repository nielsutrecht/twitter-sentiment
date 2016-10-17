package com.nibado.example.twisent.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetDTO {
    private String text;
    private String profileImageURL;
    private int score;
}
