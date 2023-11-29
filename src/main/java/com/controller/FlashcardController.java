package com.controller;

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
import java.util.ArrayList;
import java.util.List;
import com.flashcard.Flashcard;

public class FlashcardController implements Initializable {

    public Button leftButton;
    public Button rightButton;
    static int currentCard = 0;
    public Label currentCardLabel;
    public Pane cardPane;
    public TextArea answerArea;
    public TextArea questionArea;
    public Button helpButton;
    public TextArea rotateCard;
    List<String> cardAnwserList = new ArrayList<>();
    List<String> cardQuestionList = new ArrayList<>();
    private Flashcard flashcard;


    @Override
    public void initialize(java.net.URL url, java.util.ResourceBundle resourceBundle) {
        //loadCard();
        flashcard = new Flashcard();
        Thread loadThread = new Thread(() -> {
            Platform.runLater(() -> {
                flashcard.initialize();
                startConfig();
            });
        });
        loadThread.start();
        leftButton.setOnMouseClicked(this::onLeftButton);
        rightButton.setOnMouseClicked(this::onRightButton);
        cardPane.setStyle("-fx-background-color: #2e3856");
        cardPane.setOnMouseClicked(this::flipCard);
        questionArea.setOnMouseClicked(this::flipCard);
        answerArea.setOnMouseClicked(this::flipCard);

        System.out.println(cardPane.getPrefHeight() + " " + cardPane.getPrefWidth());

        helpButton.setOnAction(event -> {
            if (helpButton.getText().equals("Hiển thị gợi ý")) {
                helpButton.setText(getProperty());
            } else {
                helpButton.setText("Hiển thị gợi ý");
            }
        });
    }

    private void onRightButton(MouseEvent mouseEvent) {
        if (currentCard < flashcard.size() - 1) {
            currentCard++;
            currentCardLabel.setText((currentCard + 1) + "/" + flashcard.size());
        }
        System.out.println(currentCard + 1);
        showQuestion();
    }

    private void onLeftButton(MouseEvent mouseEvent) {
        if (currentCard >= 1) {
            currentCard--;
            currentCardLabel.setText((currentCard + 1) + "/" +flashcard.size());
        }
        System.out.println(currentCard + 1);
        showQuestion();
    }

    public void flipCard(MouseEvent mouseEvent) {
        boolean isQuestion = questionArea.isVisible();
        answerArea.setVisible(false);
        questionArea.setVisible(false);
        helpButton.setVisible(false);
        rotateCard.setVisible(true);
        RotateTransition rotateTransition = createRotator(rotateCard);
        rotateTransition.setOnFinished(event -> {
            rotateCard.setVisible(false);
            if (isQuestion) {
                showAnswer();
            } else {
                showQuestion();
            }
        });
        rotateTransition.play();
    }

    private void showAnswer() {
        helpButton.setVisible(false);
        questionArea.setVisible(false);
        answerArea.setVisible(true);

        answerArea.setText(flashcard.getAnswer(currentCard));
        hackTextAreaLayout(answerArea);
    }

    private void showQuestion() {
        helpButton.setVisible(true);
        helpButton.setText("Hiển thị gợi ý");
        questionArea.setVisible(true);
        answerArea.setVisible(false);
        questionArea.setText(flashcard.getQuestion(currentCard));
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
        String text = flashcard.getAnswer(currentCard);
        if (text.contains("tính từ")) {
            return "tính từ";
        }
        if (text.contains("danh từ")) {
            return "danh từ";
        }
        if (text.contains("trạng tự")) {
            return "trạng tự";
        }
        return "động từ";
    }

    private RotateTransition createRotator(Node card) {
        RotateTransition rotator = new RotateTransition(Duration.millis(700), card);
        rotator.setAxis(Rotate.X_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(180);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(1);

        return rotator;
    }


    public void startConfig() {
        questionArea.setText(flashcard.getQuestion(currentCard));
        answerArea.setText(flashcard.getAnswer(currentCard));
        questionArea.setVisible(true);
        answerArea.setVisible(false);
        currentCardLabel.setText((currentCard + 1) + "/" + flashcard.size());
        answerArea.setWrapText(true);
        questionArea.setWrapText(true);
        hackTextAreaLayout(questionArea);
    }
}
