package com.example.lab8;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Date;

public class Phrases {
    private ObservableList<BreakingBadPhrase> phrases;

    public ObservableList<BreakingBadPhrase> getPhrases() {
        return phrases;
    }

    public Phrases(ObservableList<BreakingBadPhrase> phrases) {
        this.phrases = phrases;
    }

    public void setPhrases(ObservableList<BreakingBadPhrase> phrases) {
        this.phrases = phrases;
    }

    public Phrases() {
        phrases = FXCollections.observableArrayList();
    }

    public void add(BreakingBadPhrase bbp) {
        this.phrases.add(bbp);
    }

    public Phrases filterByAuthor(String str) {
        Phrases tempObj = new Phrases();
        for (BreakingBadPhrase phrase : this.phrases) {
            if (phrase.getAuthor().toLowerCase().contains(str.toLowerCase()))
                tempObj.add(phrase);
        }
        return tempObj;
    }

    public void ReadAndParseToList(JSONGetter jsonGetter) {
        String jsonString = jsonGetter.jsonIn;

        Object obj = null;
        try {
            obj = new JSONParser().parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = (JSONArray) obj;

        assert jsonArray != null;
        for (Object jsonObject : jsonArray) {
            JSONObject current = (JSONObject) jsonObject;
            int quoteId = Integer.parseInt(String.valueOf(current.get("quote_id")));
            String quote = (String) current.get("quote");
            String author = (String) current.get("author");
            String series = (String) current.get("series");
            BreakingBadPhrase bbp = new BreakingBadPhrase(quoteId, quote, author, series);
            phrases.add(bbp);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Phrases (" + (new Date()).toLocaleString() + ") " + System.lineSeparator());
        for (BreakingBadPhrase bbp : phrases) {
            result.append(bbp).append(System.lineSeparator());
        }
        return result.toString();
    }
}