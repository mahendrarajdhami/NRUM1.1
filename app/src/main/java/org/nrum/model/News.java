package org.nrum.model;

/**
 * Created by rajdhami on 12/25/2016.
 */
public class News {
    private String title, thumbnailUrl, detail;

    public News() {
    }

    public News(String name, String thumbnailUrl, String detail) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    /*public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }*/
}