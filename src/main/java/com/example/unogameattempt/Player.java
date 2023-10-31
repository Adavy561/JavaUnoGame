package com.example.unogameattempt;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Player {
    /**
     * Player class represents the user and bot as well as actions they can take
     * */
    private String name;
    private List<Card> hand;

    // player constructor
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<Card>();
    }

    // getter functions for player
    public String getName() {
        return this.name;
    }

    public List<Card> getHand() {
        return this.hand;
    }

    // checks if a player has won
    public boolean hasWon() {
        return getHand().size() == 0;
    }

    // deck modifications to the players specific hand
    public void addCard(Card card) {
        this.hand.add(card);
    }

    public void removeCard(Card card) {
        this.hand.remove(card);
    }

    public boolean hasCard(Card card) {
        return this.hand.contains(card);
    }

    public Card playCard(Card card) throws InputMismatchException {
        // removes card from player hand and returns it to be placed in discard
        if (hasCard(card)) {
            removeCard(card);
            return card;
        } else {
            throw new InputMismatchException("This card is not a valid play");
        }
    }

    public boolean canPlayCard(Card card) {
        // iterates through current player hand checking if anything can be played currently
        for (Card c : this.hand) {
            if (c.canBePlayedOn(card)) {
                return true;
            }
        }
        return false;
    }
}
