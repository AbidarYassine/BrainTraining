package com.example.trainbrain;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnStr;
    TextView sum;
    TextView score;
    TextView second;
    TextView resultanswer;
    int scoreInt = 0;
    int numberQuestion = 0;
    ArrayList<Integer> answers = new ArrayList<>();
    int locationCorrectAnswer;
    Button[] buttons = new Button[4];
    CountDownTimer downTimer;
    MediaPlayer mediaPlayer;
    EditText oper;
    int randa = 0;
    int randb = 0;
    TextView textView;
    long bestScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStr = findViewById(R.id.btnStr);
        sum = findViewById(R.id.sum);
        resultanswer = findViewById(R.id.resultanswer);
        second = findViewById(R.id.second);
        score = findViewById(R.id.score);
        oper = findViewById(R.id.oper);
        textView = findViewById(R.id.bestScore);
        oper.setText("+");
        oper.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Toast.makeText(MainActivity.this, "Only Operation '+' and '*' valid", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("+") || s.toString().equals("*") || s.toString().equals("-")) {
                    generateQuestion();
                } else {
                    Toast.makeText(MainActivity.this, "Only Operation '+' and '*' and '-' valid", Toast.LENGTH_SHORT).show();
//                    System.exit(0);
                }
            }
        });
        generateQuestion();

    }

    public void Start(View view) {
        scoreInt = 0;
        numberQuestion = 0;
        answers.clear();
        generateQuestion();
        oper.setVisibility(View.INVISIBLE);
        score.setText(Integer.toString(scoreInt) + "/" + Integer.toString(numberQuestion));
//        mediaPlayer Med create(getApplicationContext(),R.raw.aud0);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.aud0);
        mediaPlayer.start();
        if (btnStr.getText().equals("Start With Time")) {
        } else {
            downTimer.cancel();
            second.setText("30");
            resultanswer.setText(" ");
        }
        startTime();

        btnStr.setVisibility(View.INVISIBLE);
    }

    public void startTime() {
        downTimer = new CountDownTimer(31000, 1100) {
            @Override
            public void onTick(long millisUntilFinished) {
                long as = millisUntilFinished / 1000;
                second.setText(Integer.toString((int) as));
            }

            @Override
            public void onFinish() {
                second.setText("0");
                resultanswer.setText("Score is  " + Integer.toString(scoreInt) + "/" + Integer.toString(numberQuestion));
                long score = (100 * scoreInt) / numberQuestion;
                if (score >= bestScore) {
                    bestScore = score;
                }
                textView.setText("Best Score Is :" + bestScore + " %");
                btnStr.setVisibility(View.VISIBLE);
                btnStr.setText("Restart");
                mediaPlayer.stop();
                oper.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void chooseAnser(View view) {
    }

    @Override
    public void onClick(View v) {
        String answe = String.valueOf(answers.get(locationCorrectAnswer));
        Button btn = (Button) v;
        String curentVal = btn.getText().toString();
        numberQuestion++;

        resultanswer.setVisibility(View.VISIBLE);
        if (curentVal.equalsIgnoreCase(answe)) {
            scoreInt++;
            resultanswer.setText("Correct! 👌😎");

        } else {
            resultanswer.setText("Incorrect 🤣🤦‍♂");
        }
        score.setText(Integer.toString(scoreInt) + "/" + Integer.toString(numberQuestion));
        generateQuestion();

//        Toast.makeText(this, , Toast.LENGTH_SHORT).show();
    }

    public void generateQuestion() {
        answers.clear();
        Random random = new Random();
        randa = random.nextInt(21);
        randb = random.nextInt(21);
        locationCorrectAnswer = random.nextInt(4); // 0 1 2 3
        sum.setText(Integer.toString(randa) + oper.getText().toString() + String.valueOf(randb));
        int incorrectAnswe;
        for (int i = 0; i < 4; i++) {
            if (i == locationCorrectAnswer) {
                if (oper.getText().toString().equals("*")) {
                    answers.add(randa * randb);
                } else if (oper.getText().toString().equals("+")) {
                    answers.add(randa + randb);
                } else if (oper.getText().toString().equals("-")) {
                    answers.add(randa - randb);
                }

            } else {
                incorrectAnswe = random.nextInt(41);
                while (incorrectAnswe == randa + randb || incorrectAnswe == randb * randa || incorrectAnswe == randa - randb) {
                    incorrectAnswe = random.nextInt(41);
                }
                answers.add(incorrectAnswe);
            }
        }
        for (int j = 0; j < 4; j++) {
            String idbutn = "btn" + j;
            int resID = getResources().getIdentifier(idbutn, "id", getPackageName());
            buttons[j] = findViewById(resID);
            buttons[j].setText(Integer.toString(answers.get(j)));
            buttons[j].setOnClickListener(this);
        }

    }
}