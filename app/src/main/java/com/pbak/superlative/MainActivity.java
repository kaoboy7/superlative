package com.pbak.superlative;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "key";
    public final static String QUESTIONS_LEFT = "questions_left";
    public final static String ANSWERS = "answers";
    public final static String QUESTIONS = "questions";
    public final static int QUESTIONS_IN_GAME = 4;
    private QuestionsDao questionsDao;

    public void startGame(View view) {
        Intent intent = new Intent(this, QuestionActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();

        // Get answers
        ArrayList<String> questions = questionsDao.getNQuestions(4);

        intent.putExtra(EXTRA_MESSAGE, "Wrong Answer!");
        intent.putExtra(QUESTIONS_LEFT, QUESTIONS_IN_GAME - 1);
        intent.putStringArrayListExtra(ANSWERS, new ArrayList<String>());
        intent.putStringArrayListExtra(QUESTIONS, questions);
        startActivity(intent);
        finish();
    }

    public void onRadioButtonClicked(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionsDao = new QuestionsDao(this);
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
