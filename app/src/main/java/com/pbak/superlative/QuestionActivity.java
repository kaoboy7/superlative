package com.pbak.superlative;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class QuestionActivity extends ActionBarActivity {
    private QuestionsDao questionsDao;
    private Intent oldIntent;
    private int questionsLeft;
    private long surveyQuestionId;

    public void nextQuestion(View view) {
        ArrayList<String> answers = oldIntent.getStringArrayListExtra(MainActivity.ANSWERS);
        Long surveyId = getIntent().getLongExtra(MainActivity.GAME_ID, 0);

        // 1. Get radio button value
        RadioGroup playersGroup = (RadioGroup) findViewById(R.id.radioGroupPlayers);

        // 2. Add answers to solution
        if (playersGroup.getCheckedRadioButtonId() != -1) {

            int id = playersGroup.getCheckedRadioButtonId();
            View radioButton = playersGroup.findViewById(id);
            int radioId = playersGroup.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) playersGroup.getChildAt(radioId);
            String selection = (String) btn.getText();
            TextView textView = (TextView) findViewById(R.id.textView);
            String answer = textView.getText() + ": " + selection;
            answers.add(answer);

            // 3. add answers to database
            questionsDao.incrementSurveyAnswers(surveyQuestionId, selection);
        }

        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtras(getIntent());
        intent.putExtra(MainActivity.QUESTIONS_LEFT, questionsLeft - 1);
        intent.putStringArrayListExtra(MainActivity.ANSWERS, answers);
        intent.putStringArrayListExtra(MainActivity.QUESTIONS, oldIntent.getStringArrayListExtra(MainActivity.QUESTIONS));


        if (questionsLeft > 0) {
            startActivity(intent);
        } else {
            Intent endIntent = new Intent(this, DisplayMessageActivity.class);
            endIntent.putStringArrayListExtra(MainActivity.ANSWERS, answers);
            //startActivity(endIntent);

            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        }
        finish();
    }

    public void onRadioButtonClicked(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        oldIntent = getIntent();
        questionsLeft = oldIntent.getIntExtra(MainActivity.QUESTIONS_LEFT, 0);


        super.onCreate(savedInstanceState);
        questionsDao = new QuestionsDao(this);
        setContentView(R.layout.activity_question);

        TextView textView = (TextView) findViewById(R.id.textView);

        // Getting the question in the list
        textView.setText(oldIntent.getStringArrayListExtra(MainActivity.QUESTIONS).get(questionsLeft));

        surveyQuestionId = oldIntent.getIntegerArrayListExtra(MainActivity.QUESTION_IDS_KEY).get(questionsLeft);

        // Need to get ID of specific survey
//        ArrayList<SurveyQuestion> surveyQuestions = (ArrayList<SurveyQuestion>) getIntent().getSerializableExtra(MainActivity.EXISTING_SURVEY_KEY);
//        SurveyQuestion surveyQuestion = surveyQuestions.get(questionsLeft);
//        surveyQuestionId = surveyQuestion.getId();


        // Add radio button players
        ArrayList<String> players = getIntent().getStringArrayListExtra(MainActivity.PLAYERS_KEY);
        RadioGroup radioGroupPlayers = (RadioGroup) findViewById(R.id.radioGroupPlayers);

        for (String player : players) {
            RadioButton radioButtonPlayer = new RadioButton(this);
            radioButtonPlayer.setText(player);
            radioGroupPlayers.addView(radioButtonPlayer);
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
