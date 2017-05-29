package org.nrum.model;

/**
 * Created by rajdhami on 12/25/2016.
 */
public class News {
    private String title, featureImage, detail,category_name, publish_date_from;
    private int news_id;
    public News () {

    }
    public News(int news_id, String name, String featureImage, String detail, String category_name, String publish_date_from) {
        this.news_id = news_id;
        this.title = name;
        this.featureImage = featureImage;
        this.detail = detail;
        this.category_name = category_name;
        this.publish_date_from = publish_date_from;
    }

    public int getNewsID() {
        return news_id;
    }

    public void setNewsID(int newsID) {
        this.news_id = newsID;
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


    public String getCategoryName() {
        return category_name;
    }

    public void setCategoryName(String category_name) {
        this.category_name = category_name;
    }

    public String getPublishDateFrom() {
        return publish_date_from;
    }

    public void setPublishDateFrom(String publish_date_from) {
        this.publish_date_from = publish_date_from;
    }
}