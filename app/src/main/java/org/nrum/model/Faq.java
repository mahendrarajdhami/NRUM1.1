package org.nrum.model;

/**
 * Created by rajdhami on 12/25/2016.
 */
public class Faq {
    private String question, answer,category_name;
    private int faq_id;
    public Faq () {

    }
    public Faq(int faq_id, String question, String featureImage, String answer, String category_name) {
        this.faq_id = faq_id;
        this.question = question;
        this.answer = answer;
        this.category_name = category_name;
    }

    public int getFaqID() {
        return faq_id;
    }

    public void setFaqID(int faqID) {
        this.faq_id = faqID;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public String getCategoryName() {
        return category_name;
    }

    public void setCategoryName(String category_name) {
        this.category_name = category_name;
    }
}