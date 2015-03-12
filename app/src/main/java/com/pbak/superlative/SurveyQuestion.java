package com.pbak.superlative;

/**
 * Created by alexkao on 3/9/2015.
 */
public class SurveyQuestion {
    long id;
    long surveyId;
    String surveyQuestion;

    public SurveyQuestion(long id, long surveyId, String surveyQuestion) {
        this.surveyId = surveyId;
        this.id = id;
        this.surveyQuestion = surveyQuestion;
    }

    public long getId() {
        return id;
    }

    public String getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSurveyQuestion(String surveyQuestion) {
        this.surveyQuestion = surveyQuestion;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", surveyId=" + surveyId +
                ", surveyQuestion='" + surveyQuestion + '\'' +
                '}';
    }
}
