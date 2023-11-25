package com.controller;

import com.dictionary.Word;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
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
    public Button helpButton;
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
        Platform.runLater(() -> {
            hackTextAreaLayout(questionArea);
        });
        String tmp = answerArea.getText();
        helpButton.setOnAction(event -> {
            if (helpButton.getText().equals("Hiển thị gợi ý")) {
                helpButton.setText(getProperty());
            } else {
                helpButton.setText("Hiển thị gợi ý");
            }
        });
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
        RotateTransition rotateTransition = createRotator(cardPane);

        if (questionArea.isVisible()) {
            showAnswer();
        } else {
            showQuestion();
        }
        rotateTransition.play();
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
        helpButton.setVisible(false);
        questionArea.setVisible(false);
        answerArea.setVisible(true);

        answerArea.setText(cardAnwserList.get(currentCard));
        hackTextAreaLayout(answerArea);
    }

    private void showQuestion() {
        helpButton.setVisible(true);
        helpButton.setText("Hiển thị gợi ý");
        questionArea.setVisible(true);
        answerArea.setVisible(false);
        questionArea.setText(cardQuestionList.get(currentCard));
        hackTextAreaLayout(questionArea);
    }

    /**
     * set the vertical alignment to center.
     *
     * @param textArea the text area.
     */
    private void hackTextAreaLayout(TextArea textArea) {
        // set css and layout.
        textArea.applyCss();
        textArea.layout();

        ScrollPane textAreaScroller = (ScrollPane) textArea.lookup(".scroll-pane");
        Text text = (Text) textArea.lookup(".text");
        // Calculate the offset to center the text vertically
        double offset = (textArea.getHeight() - text.getBoundsInLocal().getHeight()) / 2;

        // If the offset is negative, move the text to the top to ensure visibility
        offset = Math.max(offset, 0);

        // Set the translation to center the text vertically
        text.setTranslateY(offset);

        // Set the vertical alignment to center
        text.setTextOrigin(VPos.TOP);

        // Force an immediate layout update
        textAreaScroller.getParent().requestLayout();

        textAreaScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Optional: Hide vertical scrollbar

        // Optional: Hide horizontal scrollbar
        textAreaScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private String getProperty() {
        String text = cardAnwserList.get(currentCard);
        if (text.contains("tính từ")) {
            return "tính từ";
        }
        if (text.contains("danh từ")) {
            return "danh từ";
        }
        return "động từ";
    }

    private RotateTransition createRotator(Node card) {
        RotateTransition rotator = new RotateTransition(Duration.millis(1000), card);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(360);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(1);

        return rotator;
    }

}
