package com.dp.firstproject.Models;

public class User {
    String name, email, pass;

    int coins = 100;
    int numberOfSpins = 0;
    int numberOfScratches = 0;

    public int getNumberOfSpins() {
        return numberOfSpins;
    }




    public User(int numberOfSpins) {
        this.numberOfSpins = numberOfSpins;
    }

    public void setNumberOfSpins(int numberOfSpins) {
        this.numberOfSpins = numberOfSpins;
    }

    public int getNumberOfScratches() {
        return numberOfScratches;
    }

    public void setNumberOfScratches(int numberOfScratches) {
        this.numberOfScratches = numberOfScratches;
    }

    public User() {

    }

    public User(String name, String email, String pass) {

        this.name = name;
        this.email = email;
        this.pass = pass;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
