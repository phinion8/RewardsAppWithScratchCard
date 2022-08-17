package com.dp.firstproject.Models;

public class WithdrawalRequest {
    String emailAddress, userId, registered_email;
    int noOfSpins;
    int noOfScratches;

    public WithdrawalRequest(String emailAddress, String userId, String registered_email, int noOfSpins, int noOfScratches) {
        this.emailAddress = emailAddress;
        this.userId = userId;
        this.registered_email = registered_email;
        this.noOfSpins = noOfSpins;
        this.noOfScratches = noOfScratches;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegistered_email() {
        return registered_email;
    }

    public void setRegistered_email(String registered_email) {
        this.registered_email = registered_email;
    }

    public int getNoOfSpins() {
        return noOfSpins;
    }

    public void setNoOfSpins(int noOfSpins) {
        this.noOfSpins = noOfSpins;
    }

    public int getNoOfScratches() {
        return noOfScratches;
    }

    public void setNoOfScratches(int noOfScratches) {
        this.noOfScratches = noOfScratches;
    }
}
