package ch.uzh.ifi.hase.soprafs24.classes;

public class Clue {
    private String clueText;
    private Integer clueNumber;
    private String username;
    
    public Clue(String clueText, Integer clueNumber, String username) {
        this.clueText = clueText;
        this.clueNumber = clueNumber;
        this.username = username;
    }

    public String getClueText() {
        return clueText;
    }

    public void setClueText(String clueText) {
        this.clueText = clueText;
    }

    public Integer getClueNumber() {
        return clueNumber;
    }

    public void setClueNumber(Integer clueNumber) {
        this.clueNumber = clueNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
