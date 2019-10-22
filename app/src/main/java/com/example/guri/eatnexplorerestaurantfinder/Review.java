package com.example.guri.eatnexplorerestaurantfinder;

import com.google.firebase.database.IgnoreExtraProperties;

public class Review {

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    private String rName;
    public String review;
    public float stars;
    public int votes;
    private String reviewDate;
    private String userName;
    private String UId;

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }



    public Review(String review, float stars,String reviewDate,String userName,String UId){
        this.review = review;
        this.stars = stars;
        this.reviewDate=reviewDate;
        this.userName=userName;
        this.UId=UId;
    }
    public Review(float stars,int votes){
        this.stars = stars;
        this.votes = votes;
    }
    public Review(String rName,float stars,int votes){
        this.rName=rName;
        this.stars = stars;
        this.votes = votes;
    }


    public Review(){

    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
