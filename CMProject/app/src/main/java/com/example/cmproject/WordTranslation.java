package com.example.cmproject;

public class WordTranslation {
    private int id;
    private String language1;
    private String word1;
    private String language2;
    private String word2;

    public WordTranslation() {
    }

    public WordTranslation(int id, String language1, String word1, String language2, String word2) {
        this.id = id;
        this.language1 = language1;
        this.word1 = word1;
        this.language2 = language2;
        this.word2 = word2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguage1() {
        return language1;
    }

    public void setLanguage1(String language1) {
        this.language1 = language1;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getLanguage2() {
        return language2;
    }

    public void setLanguage2(String language2) {
        this.language2 = language2;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }
}