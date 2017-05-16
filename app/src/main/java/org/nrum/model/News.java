package org.nrum.model;

/**
 * Created by rajdhami on 12/25/2016.
 */
public class News {
    private String title, featureImage, detail;

    public News() {
    }

    public News(String name, String featureImage, String detail) {
        this.title = name;
        this.featureImage = featureImage;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getFeatureImage() {
        return featureImage;
    }

    public void setFeatureImage(String featureImage) {
        this.featureImage = featureImage;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}