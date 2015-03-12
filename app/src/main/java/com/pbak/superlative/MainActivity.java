package com.pbak.superlative;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "key";
    public final static String QUESTIONS_LEFT = "questions_left";
    public final static String ANSWERS = "answers";
    public final static String QUESTIONS = "questions";
    public final static String GAME_ID = "game_id";
    public final static String PLAYERS_KEY = "players";
    public final static String EXISTING_SURVEY_KEY = "existing_survey_key";
    public final static int QUESTIONS_IN_GAME = 4;
    public static final String QUESTION_IDS_KEY = "question_ids_key";
    private static String TAG = "MainActivity";
    private QuestionsDao questionsDao;

    public void startGame(View view) {

        Intent intent = new Intent(this, AddPlayersActivity.class);

        // Get answers
        ArrayList<String> questions = questionsDao.getNQuestions(QUESTIONS_IN_GAME);
        intent.putExtra(EXTRA_MESSAGE, "Wrong Answer!");
        intent.putExtra(QUESTIONS_LEFT, QUESTIONS_IN_GAME - 1);
        intent.putStringArrayListExtra(ANSWERS, new ArrayList<String>());
        intent.putStringArrayListExtra(QUESTIONS, questions);

        long gameId = questionsDao.startNewGame();
        ArrayList<Integer> ids = questionsDao.addSurveyQuestionsToSurvey(gameId, questions);
        Log.i(TAG, "New Superlatives Game Started: " + gameId);
        intent.putExtra(GAME_ID, gameId);
        intent.putIntegerArrayListExtra(QUESTION_IDS_KEY, ids);

        startActivity(intent);
        finish();
    }

    public void startGameWithoutAddingPlayers() {

    }

    public void onRadioButtonClicked(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionsDao = new QuestionsDao(this);

        // 1. Display all existing games
        ArrayList<Long> surveyIds = questionsDao.getExistingGames();
        TableLayout tableLayout =
                (TableLayout) findViewById(R.id.existing_games_table);
        for (final Long surveyId : surveyIds) {
            TableRow tableRow = new TableRow(this);
            Log.i(TAG, "About to add existing game: " + surveyId);
            Button existingGameButton = new Button(this);
            existingGameButton.setText("Existing Game " + surveyId);
            // 2. Adding existing game button + listeners (will link to old existing game)
            existingGameButton.setOnClickListener(


                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);

                            ArrayList<String> players = questionsDao.getPlayersById(surveyId);
                            intent.putExtra(EXTRA_MESSAGE, "Wrong Answer!");
                            intent.putExtra(QUESTIONS_LEFT, QUESTIONS_IN_GAME - 1);
                            intent.putStringArrayListExtra(ANSWERS, new ArrayList<String>());
                            intent.putStringArrayListExtra(PLAYERS_KEY, players);


                            // 3. Get Questions by Existing Survey Id
                            ArrayList<Question> questionsBySurveyId = questionsDao.getQuestionsBySurveyId(surveyId);
                            ArrayList<String> questions = new ArrayList<String>();
                            ArrayList<Integer> questionIds = new ArrayList<Integer>();
                            for (Question questionBySurveyId: questionsBySurveyId) {
                                Log.i(TAG, "ITERATING THROUGH QUESTIONSBYSURVEYID: " + questionBySurveyId);
                                questions.add(questionBySurveyId.getSurveyQuestion());
                                questionIds.add(questionBySurveyId.getId());
                            }

                            ArrayList<SurveyQuestion> surveyQuestions = questionsDao.getSurveyQuestionsBySurveyId(surveyId);





                            //intent.putExtra(EXISTING_SURVEY_KEY, surveyQuestions);



                            intent.putStringArrayListExtra(QUESTIONS, questions);
                            intent.putIntegerArrayListExtra(QUESTION_IDS_KEY, questionIds);

                            startActivity(intent);
                            finish();
                        }
                    }

            );
            tableRow.addView(existingGameButton);


            // 4. Adding View Results Button + Listeners
            Button viewResultsButton = new Button(this);
            viewResultsButton.setText("View Results");
            viewResultsButton.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), ViewResultsActivity.class);
                            intent.putExtra(GAME_ID, surveyId);
                            startActivity(intent);
                        }
                    }
            );

            tableRow.addView(viewResultsButton);
            tableLayout.addView(tableRow
            );


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
