package com.pbak.superlative;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;


public class AddPlayersActivity extends ActionBarActivity {

    private static final String TAG = "AddPlayersActivity";
    private ArrayList<String> players = new ArrayList<String>();
    private QuestionsDao questionsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);
        questionsDao = new QuestionsDao(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_players, menu);
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

    public void addPlayer(View view) {

        Log.i(TAG, "About to addNewPlayer");
        TableLayout playerTable = (TableLayout) findViewById(R.id.add_player_table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        // Extract view
        EditText editText = (EditText) findViewById(R.id.add_player_text);
        String playerName = editText.getText().toString();
        tv.setText(playerName);
        players.add(playerName);
        tr.addView(tv);
        playerTable.addView(tr);

        // Clear view
        editText.setText("");
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtras(getIntent());
        intent.putStringArrayListExtra(MainActivity.PLAYERS_KEY, players);
        Long surveyId = getIntent().getLongExtra(MainActivity.GAME_ID, 0);
        questionsDao.addPlayers(surveyId, players);

        ArrayList<Integer> questionIds = getIntent().getIntegerArrayListExtra(MainActivity.QUESTION_IDS_KEY);

        for (Integer questionId: questionIds) {
            questionsDao.addInitialSurveyAnswers(questionId, players);
        };
        startActivity(intent);
        finish();
    }
}
