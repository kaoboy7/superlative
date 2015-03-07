package com.pbak.superlative;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by alexkao on 3/3/2015.
 */
public class QuestionsDao {

    private static final String TAG = "QuestionsDao";
    private static final String QUESTIONS_QUESTION_COLUMN_NAME = "question";
    private static final String QUESTIONS_TABLE_NAME = "questions";
    ArrayList<String> questions = new ArrayList<String>();
    SQLiteDatabase db;
    private static String PATH_TO_DATABASE="";


    public QuestionsDao(Context context) {
//
        SuperlativesDatabaseHelper myDbHelper = new SuperlativesDatabaseHelper(context);

        try {
            myDbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.w(TAG, "ABout to throw exception somewhere");

            if (myDbHelper == null) {
            Log.e(TAG, "ERROR, myDbHelper is null");
        }

        db = myDbHelper.getReadableDatabase();

        String[] projection = {
          "id",
          "question"
        };

        Cursor c = db.query("questions", projection, null, null, null, null, null);

        c.moveToFirst();
        String question = c.getString(c.getColumnIndexOrThrow("question"));
        Log.i(TAG, "Question = " + question);

//        myDbHelper = new SuperlativesDatabaseHelper(this);





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

        String[] projection = {
                "id",
                QUESTIONS_QUESTION_COLUMN_NAME
        };
        Cursor c = db.query(QUESTIONS_TABLE_NAME, projection, null, null, null, null, null);

        c.moveToFirst();
        String question = c.getString(c.getColumnIndexOrThrow(QUESTIONS_QUESTION_COLUMN_NAME));
        Log.i(TAG, question);

        return questions.get(answer);
    }

    public ArrayList<String> getNQuestions(int i) {
        String[] projection = {
                "id",
                "question"
        };

        String orderBy = "RANDOM() LIMIT " + i;

        ArrayList<String> questions = new ArrayList<String>();

        Cursor cursor = db.query(QUESTIONS_TABLE_NAME, projection, null, null, null, null, orderBy);
        while (cursor.moveToNext()) {
            String question = cursor.getString(cursor.getColumnIndexOrThrow(QUESTIONS_QUESTION_COLUMN_NAME));
            questions.add(question);
            Log.i(TAG, question);
        }

        return questions;
    }
}
