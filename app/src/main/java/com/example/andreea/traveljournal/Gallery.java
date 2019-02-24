package com.example.andreea.traveljournal;

public class Gallery {
    private String mPhotoUrl;
    private String mTitle;
    private String mCountry;
    private double mPrice;
    private double mRating;
    private String typeTrip;
    private String startDate;
    private String endDate;

    public Gallery () {}

    public Gallery(String mPhotoUrl, String mTitle, String mCountry, double mPrice, double mRating, String typeTrip, String startDate, String endDate) {
        this.mPhotoUrl = mPhotoUrl;
        this.mTitle = mTitle;
        this.mCountry = mCountry;
        this.mPrice = mPrice;
        this.mRating = mRating;
        this.typeTrip = typeTrip;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public void setmPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public double getmPrice() {
        return mPrice;
    }

    public void setmPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }

    public String getTypeTrip() {
        return typeTrip;
    }

    public void setTypeTrip(String typeTrip) {
        this.typeTrip = typeTrip;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Gallery{" +
                "mPhotoUrl='" + mPhotoUrl + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mCountry='" + mCountry + '\'' +
                ", mPrice=" + mPrice +
                ", mRating=" + mRating +
                ", type='" + typeTrip + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
