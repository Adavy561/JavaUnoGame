package com.example.unogameattempt;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import java.util.concurrent.CompletableFuture;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class UnoController implements Initializable {
  /**
   * Controller Class for entire application, uses multiple functions from each class to facilitate the gameplay
   * */

  // how many cards are in the players hand
  @FXML
  private Text playerCardCount;

  // completion for the color picking buttons
  private CompletableFuture<String> colorSelection = new CompletableFuture<>();

  // any additional text the player may need to read to guide the game
  @FXML
  private Text errorOut;

  // checks if player has called uno
  private boolean unoCalled = false;

  @FXML
  private void onUnoButtonClick() {
    // call uno
    unoCalled = true;
    updateScene();
  }

  @FXML
  private void onUpdateButtonClick() {
    updateScene();
  }

  // buttons that all allow the user to select the wild card color
  @FXML
  private void onRedButtonClick() {
    colorSelection.complete("Red");
    updateScene();
  }

  @FXML
  private void onBlueButtonClick() {
    colorSelection.complete("Blue");
    updateScene();
  }

  @FXML
  private void onGreenButtonClick() {
    colorSelection.complete("Green");
    updateScene();
  }

  @FXML
  private void onYellowButtonClick() {
    colorSelection.complete("Yellow");
    updateScene();
  }

  // draws a card for the user
  @FXML
  private void onDrawButtonClick() {
    playerHand.add(deck.drawCard());
    updateScene();
    playCPU();
  }

  @FXML
  private FlowPane playerHandPane;

  @FXML
  private Text cpuCardCount;

  @FXML
  private ImageView deckCardViewer;

  // necessary class variables
  private initDeck deck;
  private List<Player> playerList = new ArrayList<>();
  private int playerCounter;
  private String colorSelect = null;

  private List<Card> playerHand;
  private List<Card> cpuHand;
  private List<Card> discard = new ArrayList<>();

  private Card currentTopDiscard;

  private Card lastCard = null;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    /*
     *  function which first runs whenever controller is executed, basic gameplay
     *  */
    deck = new initDeck();
    deck.shuffle();
    // starts deck

    String[] colors = {"Red", "Green", "Blue", "Yellow"};
    String colorChoice = colors[(int) (Math.random() * colors.length)];
    discard.add(new Card(colorChoice, 0, colorChoice));
    // instantiates random color card as a 0

    playerList.add(new Player("user"));
    playerList.add(new Player("cpu"));
    // creates player objects

    for (Player player : playerList) {
      for (int i = 0; i < 7; i++) {
        player.addCard(deck.drawCard());
      }
    }
    // deals initial hands

    playerHand = playerList.get(0).getHand();
    cpuHand = playerList.get(1).getHand();

    playerCounter = 0;
    errorOut.setText("Welcome to UNO!");
    playPlayer();
    // starts the game
  }

  private void updateDiscard() {
    /*
    * updates the card at the top of the discard pile
    * */
    currentTopDiscard = discard.get(discard.size() - 1);

    deckCardViewer.setImage(currentTopDiscard.getImageLink());
  }

  @FXML
  private Text t;

  private void updateScene() {
    // Reinitialize any necessary components or data structures
    updateDiscard();
    playerCardCount.setText("Player Card Count: " + playerHand.size());
    cpuCardCount.setText("CPU Card Count: " + cpuHand.size());
    playerHandPane.getChildren().clear();
    // resetting card counts and player hand pane

    for (Card card : playerHand) {
      ImageView cardView = new ImageView(card.getImageLink());
      cardView.setOnMouseClicked(mouseEvent -> {
        try {
          playPlayerClickEvent(card);
        } catch (ExecutionException e) {
          throw new RuntimeException(e);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      });
      cardView.setFitHeight(100);
      cardView.setFitWidth(75);
      playerHandPane.getChildren().add(cardView);
      // readding each card back to the hand pane for an updated deck
    }
  }

  private void playPlayer() {
    /*
    * activates the game, calls updateScene()
    * */
//        playerHandPane.getChildren().clear();
//        for (Card card : playerHand) {
//            ImageView cardView = new ImageView(card.getImageLink());
//            cardView.setOnMouseClicked(mouseEvent -> { playPlayerClickEvent(card);});
//            cardView.setFitHeight(100);
//            cardView.setFitWidth(75);
//            playerHandPane.getChildren().add(cardView);
//        }
    updateScene();
  }

  private boolean playOnColorSelect(Card card) {
    /*
    * returns if the card can be played on the wild card color selected
    * */
    return card.getCardColor() == colorSelect;
  }

  private void playPlayerHelper(Card card) throws ExecutionException, InterruptedException {
    /*
    * helper function for the user input, gathers input and then deposits card on top of deck, executing what comes with it
    * */
    playerCounter = 0;
    if (card.canBePlayedOn(currentTopDiscard)) {
      // checks if card is playable
      discard.add(card);
      playerHand.remove(card);
      errorOut.setText("");
      updateScene();
      // puts card ontop of discard

      if (playerHand.size() == 1) {
        // checking if the user will have to call uno else draw 2
        errorOut.setText("Call UNO Quick!");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
          @Override
          public void run() {
            if (!unoCalled) {
              playerHand.add(deck.drawCard());
              playerHand.add(deck.drawCard());
              errorOut.setText("You missed it!");
            }
          }
        }, 3000);
        updateScene();
      }
      else if (playerHand.isEmpty()) {
        // win condition
        if (unoCalled) {
          errorOut.setText("You won!");
          return;
        }
      }
      if (card.isWild()) {
        // checks if card is wild, sets next card color to user selection
        card.setCardColor(String.valueOf(colorSelection.get()));
      }
      if (card.isPlusFour()) {
        // checks if card is wild, sets next card color to user selection + draw 4 for cpu
        for (int i = 0; i < 4; i++) {
          cpuHand.add(deck.drawCard());
        }
        card.setCardColor(String.valueOf(colorSelection.get()));
        playerCounter++;
      } else if (card.isSkip()) {
        // skips cpu turn allows for additional user turn
        playerCounter++;
      } else if (card.isPlusTwo()) {
        // skips cpu turn allows for additional user turn
        for (int i = 0; i < 2; i++) {
          cpuHand.add(deck.drawCard());
        }
        playerCounter++;
      } else if (card.isReverse()) {
        playerCounter++;
      }
      playerCounter++;
      updateScene();
      if (playerCounter % 2 == 0 && card.isWild()) {
        // determines if user gets to play a second time and is on a wild card
        errorOut.setText("Select the color from the wild card selector");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
          @Override
          public void run() {
            // timer delays running of cpu turn to simulate "thinking"
            playCPU();
          }
        }, 2000);
      } else if (playerCounter % 2 == 0) {
        // determines if it is the player gets to play again from special cards
        errorOut.setText("Play again, Pick a card that's the same color, number or a wild to play on discard!");
      } else {
        errorOut.setText("CPU taking turn");
        playCPU();
      }
    }
  }

  private void playPlayerClickEvent(Card card) throws ExecutionException, InterruptedException {
    /*
    * handler for click on a card in the users deck
    * */
    updateScene();
    if (card.canBePlayedOn(currentTopDiscard)) {
      // if the player can play the card, play it
      playPlayerHelper(card);
    } else if (playOnColorSelect(card)) {
      // if the player can play the card on the wild card, play it
      playPlayerHelper(card);
      colorSelect = null;
    } else {
      // if selected card is not playable, tells player to select another card
      errorOut.setText("Pick a card that's the same color, number or a wild to play on discard!");
    }
  }

  private void playCPU() {
    /*
    * handler for cpu turn
    * */
    errorOut.setText("CPU taking turn");
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        // timer to simulate cpu "thinking"
        playCPUHelper();
      }
    }, 2000);
    updateScene();
  }

  private void playCPUHelper() {
    /*
    * helper function for cpu player
    * */
    if (cpuHand.isEmpty()){
      // cpu win condition
      errorOut.setText("CPU won!");
      return;
    }
    for (Card card : cpuHand) {
      // iterates through cpu hand looking for playable card
      playerCounter = 1;
      if (card.canBePlayedOn(currentTopDiscard) || lastCard != null && lastCard.getCardColor() == card.getCardColor()) {
        // checks if card is playable, if so will be played
        discard.add(card);
        cpuHand.remove(card);
        errorOut.setText("");

        updateDiscard();
        errorOut.setText("Pick a card that's the same color, number or a wild to play on discard!");

        if (card.isWild()) {
          // checks if cpu card is a wild card
          String[] colors = {"Red", "Green", "Blue", "Yellow"};
          colorSelect = colors[(int) (Math.random() * colors.length)];
          errorOut.setText("Bot chose: " + colorSelect);
          card.setCardColor(colorSelect);
        }
        if (card.isPlusFour()) {
          // checks if the cpu plays a wild draw 4
          String[] colors = {"Red", "Green", "Blue", "Yellow"};
          colorSelect = colors[(int) (Math.random() * colors.length)];
          errorOut.setText("Bot chose: " + colorSelect);
          card.setCardColor(colorSelect);
          for (int i = 0; i < 4; i++) {
            playerHand.add(deck.drawCard());
            updateScene();
          }
          playerCounter++;

        } else if (card.isSkip()) {
          // checks if cpu play is a skip
          playerCounter++;
          playCPU();
          updateScene();
        } else if (card.isPlusTwo()) {
          // checks if cpu play is a +2 and adds 2 and skips
          playerHand.add(deck.drawCard());
          playerHand.add(deck.drawCard());
          updateScene();
          playerCounter++;
        } else if (card.isReverse()) {
          // skips player
          playerCounter++;
          playCPU();
        }

        playerCounter++;
        updateScene();
        // plays cpu again if it is its turn
        if (playerCounter % 2 == 1) {
          playCPU();
        }
      }
    }
    // else draws for cpu
    cpuHand.add(deck.drawCard());
    errorOut.setText("CPU Drew, Pick a card that's the same color, number or a wild to play on discard!");
    updateScene();
  }
}
