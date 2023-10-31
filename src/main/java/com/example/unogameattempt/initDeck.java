package com.example.unogameattempt;

//import javax.swing.text.html.ImageView;
//import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class initDeck {
    /**
     * Class instantiates and manipulates UNO deck, includes methods to get aspects of cards + shuffle deck
     * */

    private ArrayList<Card> deck;
    // deck object to be used throughout class and later returned to driver

    public initDeck() {
        deck = new ArrayList<>();

        String[] colors = { "Red", "Yellow", "Green", "Blue" };
        String[] actions = { "Skip", "Reverse", "Draw2" };
        // lists representing aspects of cards, colors + special cards

        for (String color : colors) {
            // for loop creates all cards one color at a time
            for (int i = 0; i <= 10; i++) {
                // creates cards 0-9
                if (i == 0 ) {
                    deck.add(new Card(color, i, null));
                } else if (i < 10) {
                    deck.add(new Card(color, i, null));
                    deck.add(new Card(color, i, null));
                } else {
                    for (String action : actions) {
                        // creates special cards excluding wilds
                        deck.add(new Card(color, i, action));
                        deck.add(new Card(color, i, action));
                        i++;
                    }
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            // generates wild cards
            deck.add(new Card("Wild", 13, "Wild"));
        }

        for (int i = 0; i < 4; i++) {
            // generates wild draw 4 cards
            deck.add(new Card("Wild_Draw4", 14, "Wild_Draw4"));
        }
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public List<Card> getDeck() {
        // returns deck object to driver class
        return deck;
    }

    public void shuffle() {
        // uses Collections class to shuffle
        Collections.shuffle(deck);
    }

    // checks if a deck object is empty
    public boolean isEmpty(){
        return deck.size() == 0;
    }

    // draws card from deck object
    public Card drawCard() {
        Card drawnCard = deck.remove(0);
        return drawnCard;
    }
}
