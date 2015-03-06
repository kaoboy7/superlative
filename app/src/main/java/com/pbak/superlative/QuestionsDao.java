package com.pbak.superlative;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by alexkao on 3/3/2015.
 */
public class QuestionsDao {

    ArrayList<String> questions = new ArrayList<String>();

    public QuestionsDao() {
        questions.add("Who is likely to fart and accuse someone else of it?");
        questions.add("Who will win the next New York fashion show award");
        questions.add("Who is likely to win in the next Hunger Games");
        questions.add("Who is likely to die first in the next Hunger Games");
        questions.add("Who has the best smile");
        questions.add("Who is most likely to become the next millionaire");
        questions.add("Who is most likely to take their significant other out on a candle light dinner");
        questions.add("Who is going to gain the most weight this next year");
        questions.add("Who is going to get married first?");
        questions.add("Who will be the first to have their own kid");

    }

    public String getQuestion() {
        Random rn = new Random();
        int answer = rn.nextInt(10);
        return questions.get(answer);
    }

}
