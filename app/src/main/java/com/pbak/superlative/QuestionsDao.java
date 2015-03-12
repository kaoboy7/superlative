package com.pbak.superlative;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexkao on 3/3/2015.
 */
public class QuestionsDao {

    private static final String TAG = "QuestionsDao";

    private static final String QUESTIONS_QUESTION_COLUMN_NAME = "question";
    private static final String QUESTIONS_TABLE_NAME = "questions";

    private static final String SURVEYS_TABLE_NAME = "surveys";
    private static final String SURVEYS_STATUS_COLUMN_NAME = "status";
    private static final String SURVEYS_SURVEY_ID_COLUMN_NAME = "id";

    private static final String PLAYERS_TABLE_NAME = "players";
    private static final String PLAYERS_NAME_COLUMN_NAME = "player_name";

    private static final String SURVEY_QUESTIONS_ID_COLUMN_NAME = "id";
    private static final String SURVEY_QUESTIONS_TABLE_NAME = "survey_questions";
    private static final String SURVEY_QUESTIONS_QUESTION_COLUMN_NAME = "question";

    private static final String SURVEY_ANSWERS_TABLE_NAME = "survey_answers";
    private static final String SURVEY_ANSWERS_QUESTION_ID_COLUMN_NAME = "question_id";
    private static final String SURVEY_ANSWERS_PLAYER_NAME_COLUMN_NAME = "player_name";
    private static final String SURVEY_ANSWERS_COUNT_COLUMN_NAME = "count";

    ArrayList<String> questions = new ArrayList<String>();
    SQLiteDatabase db;
    private static String PATH_TO_DATABASE = "";


    public QuestionsDao(Context context) {

        SuperlativesDatabaseHelper myDbHelper = new SuperlativesDatabaseHelper(context);

        try {
            myDbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        db = myDbHelper.getReadableDatabase();
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

    public ArrayList<Question> getQuestionsBySurveyId(long surveyId) {
        String[] projection = {
                "id",
                "question"
        };


        String whereClause = "survey_id = " + surveyId;
        ArrayList<Question> questions = new ArrayList<Question>();

        Cursor cursor = db.query(SURVEY_QUESTIONS_TABLE_NAME, projection, whereClause, null, null, null, null);
        Log.i(TAG, "Trying to get questionsBySurveyId");
        while (cursor.moveToNext()) {

            String surveyQuestion = cursor.getString(cursor.getColumnIndexOrThrow(SURVEY_QUESTIONS_QUESTION_COLUMN_NAME));
            Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(SURVEY_QUESTIONS_ID_COLUMN_NAME));
            Question question = new Question(id, surveyId, surveyQuestion);
            questions.add(question);
            Log.i(TAG, "Found question" + question);
        }

        return questions;
    }


    public ArrayList<SurveyQuestion> getSurveyQuestionsBySurveyId(long surveyId) {
        String[] projection = {
                "id",
                "question"
        };


        String whereClause = "survey_id = " + surveyId;
        ArrayList<SurveyQuestion> surveyQuestions = new ArrayList<SurveyQuestion>();

        Cursor cursor = db.query(SURVEY_QUESTIONS_TABLE_NAME, projection, whereClause, null, null, null, null);
        Log.i(TAG, "Trying to get questionsBySurveyId");
        while (cursor.moveToNext()) {

            Long surveyQuestionId = cursor.getLong(cursor.getColumnIndexOrThrow(SURVEY_QUESTIONS_ID_COLUMN_NAME));
            String question = cursor.getString(cursor.getColumnIndexOrThrow(SURVEY_QUESTIONS_QUESTION_COLUMN_NAME));
            surveyQuestions.add(new SurveyQuestion(surveyQuestionId, surveyId, question));
            Log.i(TAG, "Found question" + question);
        }

        return surveyQuestions;
    }



    public ArrayList<Question> getQuestionsAndIdsBySurveyId(long surveyId) {
        String[] projection = {
                "id",
                "question"
        };


        String whereClause = "survey_id = " + surveyId;
        ArrayList<Question> questions = new ArrayList<Question>();

        Cursor cursor = db.query(SURVEY_QUESTIONS_TABLE_NAME, projection, whereClause, null, null, null, null);
        Log.i(TAG, "Trying to get questionsIdBySurveyId");
        while (cursor.moveToNext()) {

            Integer questionId = cursor.getInt(cursor.getColumnIndexOrThrow(SURVEY_QUESTIONS_ID_COLUMN_NAME));
            String question = cursor.getString(cursor.getColumnIndexOrThrow(SURVEY_QUESTIONS_QUESTION_COLUMN_NAME));
            Question newQuestionObject = new Question(questionId, surveyId, question);


            questions.add(newQuestionObject);
            Log.i(TAG, "Found question" + question);
        }

        return questions;
    }

    public ArrayList<Integer> addSurveyQuestionsToSurvey(long surveyId, ArrayList<String> surveyQuestions) {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (String surveyQuestion : surveyQuestions) {
            ContentValues values = new ContentValues();
            values.put("survey_id", surveyId);
            values.put("question", surveyQuestion);
            long id = db.insert(SURVEY_QUESTIONS_TABLE_NAME, null, values);
            ids.add((int) id);
        }

        return ids;

    }

    public ArrayList<Long> getExistingGames() {
        String[] projection = {"id", "status"};
        Log.i(TAG, "About to call db, existing games");

        ArrayList<Long> existingGames = new ArrayList<Long>();
        Cursor cursor = db.query(SURVEYS_TABLE_NAME, projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(SURVEYS_SURVEY_ID_COLUMN_NAME));
            Log.i(TAG, "Found existing game in db: " + id);
            existingGames.add(id);
        }

        return existingGames;
    }

    public long startNewGame() {
        ContentValues values = new ContentValues();
        values.put(SURVEYS_STATUS_COLUMN_NAME, 0);
        long id = db.insert(SURVEYS_TABLE_NAME, null, values);
        return id;
    }

    public void addPlayers(long gameId, ArrayList<String> players) {
        for (String player : players) {
            ContentValues values = new ContentValues();
            values.put("survey_id", gameId);
            values.put("player_name", player);
            db.insert(PLAYERS_TABLE_NAME, null, values);
        }
    }


    public ArrayList<String> getPlayersById(long surveyId) {
        ArrayList<String> players = new ArrayList<String>();
        String[] projection = {"survey_id", "player_name"};
        String whereClause = "survey_id = " + surveyId;
        Cursor cursor = db.query(PLAYERS_TABLE_NAME, projection, whereClause, null, null, null, null);
        while (cursor.moveToNext()) {
            String player = cursor.getString(cursor.getColumnIndexOrThrow(PLAYERS_NAME_COLUMN_NAME));
            players.add(player);
        }

        return players;
    }

    // survey_answers query
    public void addInitialSurveyAnswers(long questionId, ArrayList<String> players) {
        for (String player : players) {
            ContentValues values = new ContentValues();
            values.put(SURVEY_ANSWERS_QUESTION_ID_COLUMN_NAME, questionId);
            values.put(SURVEY_ANSWERS_PLAYER_NAME_COLUMN_NAME, player);
            values.put(SURVEY_ANSWERS_COUNT_COLUMN_NAME, 0);
            db.insert(SURVEY_ANSWERS_TABLE_NAME, null, values);
        }
    }

    public void incrementSurveyAnswers(long questionId, String player) {
        String rawSql = "UPDATE survey_answers SET count = count + 1 " +
                "WHERE question_id = " + questionId + " AND player_name = '" + player + "'";
        db.execSQL(rawSql);
    }

    public HashMap<String, Long> getSurveyResultsByQuestionId(long questionId) {

        HashMap<String, Long> surveyAnswers = new HashMap<String, Long>();
        String[] projection = {"question_id", "player_name", "count"};
        String whereClause = "question_id = " + questionId;
        Cursor cursor = db.query(SURVEY_ANSWERS_TABLE_NAME, projection, whereClause, null, null, null, null);
        Log.i(TAG, "Querying getSurveyResultsByQuestionId: " + questionId);
        while (cursor.moveToNext()) {
            String player_name = cursor.getString(cursor.getColumnIndexOrThrow(SURVEY_ANSWERS_PLAYER_NAME_COLUMN_NAME));
            Long column_name = cursor.getLong(cursor.getColumnIndexOrThrow(SURVEY_ANSWERS_COUNT_COLUMN_NAME));
            Log.i(TAG, "Found:" + player_name + ", " + column_name);
            surveyAnswers.put(player_name, column_name);
        }

        return surveyAnswers;
    }


}
