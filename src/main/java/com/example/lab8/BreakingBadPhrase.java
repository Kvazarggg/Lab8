package com.example.lab8;

import java.util.Comparator;

public class BreakingBadPhrase {
    private int quoteId;
    private String quote;
    private String author;
    private String series;

    public BreakingBadPhrase() {
    }

    /**
     *
     * @param quote текст фразы
     * @param author автор фразы
     * @param series название сериала
     * @param quoteId id цитаты
     */

    public BreakingBadPhrase(int quoteId, String quote, String author, String series) {
        super();
        this.quoteId = quoteId;
        this.quote = quote;
        this.author = author;
        this.series = series;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    @Override
    public String toString() {
        return "BreakingBadPhrases{" +
                "quoteId=" + quoteId +
                ", quote='" + quote + '\'' +
                ", author='" + author + '\'' +
                ", series='" + series + '\'' +
                '}';
    }

    public static Comparator<BreakingBadPhrase> byAuthorAsc = Comparator.comparing(o -> o.author);
    public static Comparator<BreakingBadPhrase> byAuthorDesc = (o1, o2) -> o2.author.compareTo(o1.author);
    public static Comparator<BreakingBadPhrase> byQuoteIDAsc = (o1, o2) -> o1.quoteId > o2.quoteId ? 1 : o1.quoteId < o2.quoteId ? -1 : 0;
    public static Comparator<BreakingBadPhrase> byQuoteIDDesc = (o1, o2) -> o1.quoteId < o2.quoteId ? 1 : o1.quoteId > o2.quoteId ? -1 : 0;

}