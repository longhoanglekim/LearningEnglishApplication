package com.controller;

import com.dictionary.Word;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

import static com.ui.Model.dictionary;

import java.util.ArrayList;
import java.util.List;

public class FlashcardController implements Initializable {

    public Button leftButton;
    public Button rightButton;
    static int currentCard = 0;
    public Label currentCardLabel;
    public Pane cardPane;
    public TextArea answerArea;
    public TextArea questionArea;
    List<String> cardAnwserList = new ArrayList<>();
    List<String> cardQuestionList = new ArrayList<>();

    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle resourceBundle) {
        loadCard();
        questionArea.setText(cardQuestionList.get(currentCard));
        answerArea.setText(cardAnwserList.get(currentCard));
        questionArea.setVisible(true);
        answerArea.setVisible(false);
        currentCardLabel.setText((currentCard + 1) + "/" + cardQuestionList.size());
        leftButton.setOnMouseClicked(this::onLeftButton);
        rightButton.setOnMouseClicked(this::onRightButton);
        cardPane.setStyle("-fx-background-color: #2e3856");
        cardPane.setOnMouseClicked(this::flipCard);
        questionArea.setOnMouseClicked(this::flipCard);
        answerArea.setOnMouseClicked(this::flipCard);
        answerArea.setWrapText(true);
        questionArea.setWrapText(true);
        System.out.println(cardPane.getPrefHeight() + " " + cardPane.getPrefWidth());
    }

    private void onRightButton(MouseEvent mouseEvent) {
        if (currentCard < cardAnwserList.size() - 1) {
            currentCard++;
            currentCardLabel.setText((currentCard + 1) + "/" + cardQuestionList.size());
        }
        System.out.println(currentCard + 1);
        showQuestion();
    }

    private void onLeftButton(MouseEvent mouseEvent) {
        if (currentCard >= 1) {
            currentCard--;
            currentCardLabel.setText((currentCard + 1) + "/" + cardQuestionList.size());
        }
        System.out.println(currentCard + 1);
        showQuestion();
    }

    public void flipCard(MouseEvent mouseEvent) {
        if (questionArea.isVisible()) {
            showAnswer();
        } else {
            showQuestion();
        }
    }

    public void loadCard() {
        for (int i = 0; i < dictionary.getBookmarkList().getList().size(); i++) {
            cardQuestionList.add(dictionary.getBookmarkList().getList().get(i));
            Word word = dictionary.lookup(cardQuestionList.get(i));
            if (word != null) {
                cardAnwserList.add(word.getDefinition().toString());
            }
        }
    }

    private void showAnswer() {
        hackTextAreaLayout(answerArea);
        questionArea.setVisible(false);
        answerArea.setVisible(true);
        answerArea.setText(cardAnwserList.get(currentCard));
    }

    private void showQuestion() {
        hackTextAreaLayout(questionArea);
        questionArea.setVisible(true);
        answerArea.setVisible(false);
        questionArea.setText(cardQuestionList.get(currentCard));
    }
    private void hackTextAreaLayout(TextArea textArea) {
        textArea.applyCss();
        textArea.layout();

        ScrollPane textAreaScroller = (ScrollPane) textArea.lookup(".scroll-pane");
        Text text = (Text) textArea.lookup(".text");

        ChangeListener<? super Bounds> listener = (obs, oldBounds, newBounds) ->
                centerTextIfNecessary(textAreaScroller, text);
        textAreaScroller.viewportBoundsProperty().addListener(listener);
        text.boundsInLocalProperty().addListener(listener);

        textAreaScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Optional: Hide horizontal scrollbar
    }

    private void centerTextIfNecessary(ScrollPane textAreaScroller, Text text) {
        double textHeight = text.getBoundsInLocal().getHeight();
        double viewportHeight = textAreaScroller.getViewportBounds().getHeight();
        double offset = Math.max(0, (viewportHeight - textHeight) / 2);
        text.setTranslateY(offset);
        Parent content = (Parent) textAreaScroller.getContent();
        for (Node n : content.getChildrenUnmodifiable()) {
            if (n instanceof Path) { // caret
                n.setTranslateY(offset);
            }
        }
    }
}
