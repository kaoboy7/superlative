package com.pbak.superlative;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class ViewResultsActivity extends ActionBarActivity {
    private static final String TAG = "ViewResults";
    private QuestionsDao questionsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);

        Long surveyId = getIntent().getLongExtra(MainActivity.GAME_ID, 0);

        questionsDao = new QuestionsDao(this);

        // 1. Go through each question by id
        ArrayList<Question> questions = questionsDao.getQuestionsAndIdsBySurveyId(surveyId);
        Log.i(TAG, "Going through questions for surveyId=" + surveyId);
        TableLayout resultsTable = (TableLayout) findViewById(R.id.view_results_table);
        for (Question question: questions) {

            // 2. Going through each question and getting id
            Log.i(TAG, "Found question:" + question.toString() + "Question id: " + question.getId());
            HashMap<String, Long> results = questionsDao.getSurveyResultsByQuestionId(question.getId());

            // 3. Display the question
            TableRow trQuestion = new TableRow(this);
            trQuestion.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            TextView tvQuestion = new TextView(this);
            tvQuestion.setText(question.getSurveyQuestion());
            trQuestion.addView(tvQuestion);
            resultsTable.addView(trQuestion);



            for (String key: results.keySet()) {

                // 4. Display each person and # of votes
                Log.i(TAG, "Found key=" + key);
                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                TextView tv = new TextView(this);
                tv.setText(key + ": " + results.get(key));
                tr.addView(tv);
                resultsTable.addView(tr);
            }


        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_results, menu);
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
