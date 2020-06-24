package com.k2thend.supervisor.model;


public class Form {

    private String formId;
    private Long insertDate;
    private String userId;
    private String pLunchTime;
    private String pEvening;
    private String checkoutArea;
    private String imgUrl;
    private int nbOfCleaningStuff;
    private String reamrqs;
    private String problems;

    public Form(String formId, Long insertDate, String userId, String pLunchTime, String pEvening, String checkoutArea, String imgUrl, int nbOfCleaningStuff, String reamrqs, String problems) {
        this.formId = formId;
        this.insertDate = insertDate;
        this.userId = userId;
        this.pLunchTime = pLunchTime;
        this.pEvening = pEvening;
        this.checkoutArea = checkoutArea;
        this.imgUrl = imgUrl;
        this.nbOfCleaningStuff = nbOfCleaningStuff;
        this.reamrqs = reamrqs;
        this.problems = problems;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Long getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Long insertDate) {
        this.insertDate = insertDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getpLunchTime() {
        return pLunchTime;
    }

    public void setpLunchTime(String pLunchTime) {
        this.pLunchTime = pLunchTime;
    }

    public String getpEvening() {
        return pEvening;
    }

    public void setpEvening(String pEvening) {
        this.pEvening = pEvening;
    }

    public String getCheckoutArea() {
        return checkoutArea;
    }

    public void setCheckoutArea(String checkoutArea) {
        this.checkoutArea = checkoutArea;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getNbOfCleaningStuff() {
        return nbOfCleaningStuff;
    }

    public void setNbOfCleaningStuff(int nbOfCleaningStuff) {
        this.nbOfCleaningStuff = nbOfCleaningStuff;
    }

    public String getReamrqs() {
        return reamrqs;
    }

    public void setReamrqs(String reamrqs) {
        this.reamrqs = reamrqs;
    }

    public String getProblems() {
        return problems;
    }

    public void setProblems(String problems) {
        this.problems = problems;
    }
}
