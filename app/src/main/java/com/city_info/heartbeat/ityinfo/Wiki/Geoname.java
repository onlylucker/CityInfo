package com.city_info.heartbeat.ityinfo.Wiki;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geoname {

    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("elevation")
    @Expose
    private Integer elevation;
    @SerializedName("geoNameId")
    @Expose
    private Integer geoNameId;
    @SerializedName("lng")
    @Expose
    private Float lng;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("rank")
    @Expose
    private Integer rank;
    @SerializedName("thumbnailImg")
    @Expose
    private String thumbnailImg;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("lat")
    @Expose
    private Float lat;
    @SerializedName("wikipediaUrl")
    @Expose
    private String wikipediaUrl;
    @SerializedName("feature")
    @Expose
    private String feature;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getElevation() {
        return elevation;
    }

    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    public Integer getGeoNameId() {
        return geoNameId;
    }

    public void setGeoNameId(Integer geoNameId) {
        this.geoNameId = geoNameId;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getThumbnailImg() {
        return thumbnailImg;
    }

    public void setThumbnailImg(String thumbnailImg) {
        this.thumbnailImg = thumbnailImg;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public String getWikipediaUrl() {
        return wikipediaUrl;
    }

    public void setWikipediaUrl(String wikipediaUrl) {
        this.wikipediaUrl = wikipediaUrl;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

}