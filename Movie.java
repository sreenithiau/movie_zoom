package moviezoom;

import java.io.Serializable;
import java.io.*;
public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String director;
    private int year;
    private String mediaFormat;
    private String tapeNumber;
    private String genre;
    private String rating;
    private String personalEvaluation;
    private String generalComment;

    // Default constructor
    public Movie() {
    }

    // Parameterized constructor
    public Movie(String title, String director, int year, String mediaFormat,
                 String tapeNumber, String genre, String rating,
                 String personalEvaluation, String generalComment) {
        this.title = title;
        this.director = director;
        this.year = year;
        this.mediaFormat = mediaFormat;
        this.tapeNumber = tapeNumber;
        this.genre = genre;
        this.rating = rating;
        this.personalEvaluation = personalEvaluation;
        this.generalComment = generalComment;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMediaFormat() {
        return mediaFormat;
    }

    public void setMediaFormat(String mediaFormat) {
        this.mediaFormat = mediaFormat;
    }

    public String getTapeNumber() {
        return tapeNumber;
    }

    public void setTapeNumber(String tapeNumber) {
        this.tapeNumber = tapeNumber;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPersonalEvaluation() {
        return personalEvaluation;
    }

    public void setPersonalEvaluation(String personalEvaluation) {
        this.personalEvaluation = personalEvaluation;
    }

    public String getGeneralComment() {
        return generalComment;
    }

    public void setGeneralComment(String generalComment) {
        this.generalComment = generalComment;
    }

    // toString method (for easy printing of movie details)
    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", year=" + year +
                ", mediaFormat='" + mediaFormat + '\'' +
                ", tapeNumber='" + tapeNumber + '\'' +
                ", genre='" + genre + '\'' +
                ", rating='" + rating + '\'' +
                ", personalEvaluation='" + personalEvaluation + '\'' +
                ", generalComment='" + generalComment + '\'' +
                '}';
    }
}
