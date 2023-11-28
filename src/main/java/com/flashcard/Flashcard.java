package com.flashcard;
import com.dictionary.Word;

import java.util.ArrayList;
import java.util.List;

import static com.ui.Model.dictionary;
public class Flashcard {
    private List<String> questionList;
    private List<String> answerList;

    public Flashcard() {
    }

    public List<String> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<String> questionList) {
        this.questionList = questionList;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    public void initialize() {
        questionList = new ArrayList<>();
        answerList = new ArrayList<>();
        loadCard();
        System.out.println("Flashcard is initialized");

    }

    public void loadCard() {
        for (int i = 0; i < dictionary.getBookmarkList().getList().size(); i++) {
            questionList.add(dictionary.getBookmarkList().getList().get(i));
            Word word = dictionary.lookup(questionList.get(i));
            if (word != null) {
                answerList.add(word.getDefinition().toString());
            }
        }
    }

    public String getAnswer(int index) {
        return answerList.get(index);
    }

    public String getQuestion(int index) {
        return questionList.get(index);
    }

    public int size() {
        return questionList.size();
    }
}
