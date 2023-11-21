package com.controller;

import com.dictionary.Word;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import static com.ui.Model.dictionary;

import java.util.ArrayList;
import java.util.List;

public class FlashcardController implements Initializable {
    public Label questionLabel;
    public Label answerLabel;
    public Pane cardPane;
    public Button leftButton;
    public Button rightButton;
    public TextField currentCardField;
    static int currentCard = 0;
    List<String> cardAnwserList = new ArrayList<>();
    List<String> cardQuestionList = new ArrayList<>();

    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle resourceBundle) {
        loadCard();
        questionLabel.setText(cardQuestionList.get(currentCard));
        answerLabel.setText(cardAnwserList.get(currentCard));
        questionLabel.setVisible(true);
        answerLabel.setVisible(false);
        cardPane.setOnMouseClicked(this::flipCard);
        currentCardField.setText("1/" + cardQuestionList.size());
        leftButton.setOnMouseClicked(this::onLeftButton);
        rightButton.setOnMouseClicked(this::onRightButton);
    }

    private void onRightButton(MouseEvent mouseEvent) {
        if (currentCard < cardAnwserList.size() - 1) {
            currentCard++;
            currentCardField.setText((currentCard + 1) + "/" + cardQuestionList.size());
        }
        System.out.println(currentCard + 1);
        showQuestion();
    }

    private void onLeftButton(MouseEvent mouseEvent) {
        if (currentCard >= 1) {
            currentCard--;
            currentCardField.setText((currentCard + 1) + "/" + cardQuestionList.size());
        }
        System.out.println(currentCard + 1);
        showQuestion();
    }

    public void flipCard(MouseEvent mouseEvent) {
        if (questionLabel.isVisible()) {
            showAnswer();
        } else {
            showQuestion();
        }
    }

    public void loadCard() {
//        cardAnwserList.add("answer 1");
//        cardAnwserList.add("answer 2");
//        cardAnwserList.add("answer 3");
//        cardAnwserList.add("answer 4");
//        cardQuestionList.add("question 1");
//        cardQuestionList.add("question 2");
//        cardQuestionList.add("question 3");
//        cardQuestionList.add("question 4");
        for (int i = 0; i < dictionary.getBookmarkList().getList().size(); i++) {
            cardQuestionList.add(dictionary.getBookmarkList().getList().get(i));
            Word word = dictionary.lookup(cardQuestionList.get(i));
            if (word != null) {
                cardAnwserList.add(word.getExplain());
            }
        }
    }

    private void showAnswer() {
        questionLabel.setVisible(false);
        answerLabel.setVisible(true);
        answerLabel.setText(cardAnwserList.get(currentCard));
    }

    private void showQuestion() {
        questionLabel.setVisible(true);
        answerLabel.setVisible(false);
        questionLabel.setText(cardQuestionList.get(currentCard));
    }
}
