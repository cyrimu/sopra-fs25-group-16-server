package ch.uzh.ifi.hase.soprafs24.classes;

public class Guess {
    private Integer cardNumber;
    private String username;

    public Guess(Integer cardNumber, String username) {
        this.cardNumber = cardNumber;
        this.username = username;
    }

    public Integer getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    }