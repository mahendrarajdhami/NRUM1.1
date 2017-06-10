package org.nrum.model;

/**
 * Created by rajdhami on 6/1/2017.
 */
public class Post {
    private String post_title, bannerImage, detail,category_name, publish_date;
    private int post_id;
    public Post () {

    }
    public Post(int post_id, String name, String bannerImage, String detail, String category_name, String publish_date) {
        this.post_id = post_id;
        this.post_title = name;
        this.bannerImage = bannerImage;
        this.detail = detail;
        this.category_name = category_name;
        this.publish_date = publish_date;
    }

    public int getPostID() {
        return post_id;
    }

    public void setPostID(int postID) {
        this.post_id = postID;
    }
    public String getPostTitle() {
        return post_title;
    }

    public void setPostTitle(String name) {
        this.post_title = name;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setFeatureImage(String bannerImage) {
        this.bannerImage = bannerImage;
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
        return publish_date;
    }

    public void setPublishDateFrom(String publish_date) {
        this.publish_date = publish_date;
    }
}
