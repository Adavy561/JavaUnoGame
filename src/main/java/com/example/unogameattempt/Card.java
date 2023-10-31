package com.example.unogameattempt;

import javafx.scene.image.Image;

public class Card {
    /**
     * Card object used to contain aspects of each uno card
     * */
    private int cardNum;
    private String cardAction;
    private String cardColor;

    // constructor for regular card
    Card(String cardColor, int cardNum, String cardAction) {
        this.cardNum = cardNum;
        this.cardColor = cardColor;
        this.cardAction = cardAction;
    }

    // getter functions for cards
    public int getCardNum() {
        return cardNum;
    }

    public String getCardAction() {
        return cardAction;
    }

    public String getCardColor() {
        return cardColor;
    }

    // setter functions for cards
    public void setCardAction(String cardAction) {
        this.cardAction = cardAction;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }

    public void setCardNum(int cardNum) {
        this.cardNum = cardNum;
    }

    // returns the link to the png file of each card
    public Image getImageLink() {
        // https://stackoverflow.com/questions/61531317/how-do-i-determine-the-correct-path-for-fxml-files-css-files-images-and-other
//        InputStream inputStream = getClass().getResourceAsStream("/PineTools.com_files/");
        return new Image(getClass().getResource("/images/"+ getCardNum() + "_" + getCardColor() + ".png").toExternalForm());
//        return new Image("https://www.shutterstock.com/image-vector/uno-card-vector-illustration-isolated-600w-2167463409.jpg");
    }

    // checks if a card can be played on another
    public boolean canBePlayedOn(Card otherCard) {
        if (this.cardColor == "Wild" || this.cardColor == "Wild_Draw4" || this.cardColor == otherCard.cardColor
                || this.cardNum == otherCard.cardNum) {
            return true;
        }
        return false;
    }

    // functions which check for special types of cards
    public boolean isWild() {
        return this.cardColor == "Wild";
    }

    public boolean isSkip() {
        return this.getCardNum() == 10;
    }

    public boolean isPlusTwo() {
        return this.getCardNum() == 12;
    }

    public boolean isPlusFour() {
        return this.getCardNum() == 14;
    }

    public boolean isReverse() { return this.getCardNum() == 11; }

    // string representation of a card
    @Override
    public String toString() {
        return getCardNum() + "_" + getCardColor();
    }
}