package org.sakila;

public class Film {
    private int filmId;
    private String title;


    public Film(int filmId, String title) {
        this.filmId = filmId;
        this.title = title;

    }

    // Getters and setters...

    @Override
    public String toString() {
        return "Film{" +
                "filmId=" + filmId +
                ", title='" + title + '\'' +
                '}';
    }
}