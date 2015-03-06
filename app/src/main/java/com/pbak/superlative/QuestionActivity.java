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
    QuestionsDao questionsDao = new QuestionsDao();

    public void sendMessage(View view) {
        Intent oldIntent = getIntent();
        int questionsLeft;
        questionsLeft = oldIntent.getIntExtra(MainActivity.QUESTIONS_LEFT, 0);
        ArrayList<String> answers = oldIntent.getStringArrayListExtra(MainActivity.ANSWERS);

        // Get radio button value
        RadioGroup playersGroup = (RadioGroup) findViewById(R.id.radioGroupPlayers);
        if (playersGroup.getCheckedRadioButtonId() != -1) {
            int id = playersGroup.getCheckedRadioButtonId();
            View radioButton = playersGroup.findViewById(id);
            int radioId = playersGroup.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) playersGroup.getChildAt(radioId);
            String selection = (String) btn.getText();

            TextView textView = (TextView) findViewById(R.id.textView);

            String answer = textView.getText() + ": " + selection;
            answers.add(answer);
        }


        Intent intent = new Intent(this, QuestionActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
        intent.putExtra(MainActivity.QUESTIONS_LEFT, questionsLeft - 1);
        intent.putStringArrayListExtra(MainActivity.ANSWERS, answers);

        if (questionsLeft > 0) {
            startActivity(intent);
        } else {
            Intent endIntent = new Intent(this, DisplayMessageActivity.class);
            endIntent.putStringArrayListExtra(MainActivity.ANSWERS, answers);
            startActivity(endIntent);
        }
        finish();
    }

    public void onRadioButtonClicked(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(questionsDao.getQuestion());



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
