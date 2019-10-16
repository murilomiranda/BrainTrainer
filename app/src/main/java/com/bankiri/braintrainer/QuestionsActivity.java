package com.bankiri.braintrainer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionsActivity extends AppCompatActivity {
    TextView timerTextView;
    TextView questionTextView;
    TextView rateTextView;
    TextView feedbackTextView;
    CountDownTimer countDownTimer;
    GridLayout gridLayout;
    Button button;
    int correctAnswer;
    int numberOfQuestions = 0;
    int numberOfRightAnswer = 0;

    public void timerView(int seconds) {
        int minute = seconds/60;
        seconds -= (minute*60);
        String secondString = Integer.toString(seconds);

        if(seconds <= 9) {
            secondString = "0" + secondString;
        }

        timerTextView.setText(String.format("%d:%s", minute, secondString));
    }

    public void countDown() {
        countDownTimer = new CountDownTimer(60000 + 1000, 1000) {
            @Override
            public void onTick(long l) {
                timerView((int) l/1000);
            }

            @Override
            public void onFinish() {
                button = findViewById(R.id.playAgainButton);

                button.setVisibility(View.VISIBLE);
                feedbackTextView.setText("Done!");
                setGridLayout(false);
            }
        }.start();
    }

    public void setGridLayout(boolean value) {
        gridLayout = findViewById(R.id.answerGrid);

        int count = gridLayout.getChildCount();
        for(int i = 0 ; i < count ; i++){
            TextView child = (TextView) gridLayout.getChildAt(i);
            child.setEnabled(value);
        }
    }

    public void playAgainButton(View view) {
        button = findViewById(R.id.playAgainButton);

        button.setVisibility(View.INVISIBLE);
        feedbackTextView.setVisibility(View.INVISIBLE);
        rateTextView.setText("0/0");
        setGridLayout(true);
        countDown();
        questions();
        numberOfQuestions = 0;
        numberOfRightAnswer = 0;
    }

    public void questions() {
        Random rand = new Random();
        int randomNumberA = rand.nextInt(20)+1;
        int randomNumberB = rand.nextInt(20)+1;

        correctAnswer = randomNumberA + randomNumberB;

        questionTextView.setText(String.format("%d + %d = ", randomNumberA, randomNumberB));

        List<Integer> answers = new ArrayList<>();
        answers.add(correctAnswer);

        // generating random answers
        int numberOfAnswers = 1;
        while(numberOfAnswers < 4) {
            randomNumberA = rand.nextInt(100)+1;
            if(!answers.contains(randomNumberA)){
                answers.add(randomNumberA);
                numberOfAnswers++;
            }
        }

        Collections.shuffle(answers);

        gridLayout = findViewById(R.id.answerGrid);

        int count = gridLayout.getChildCount();
        for(int i = 0 ; i < count ; i++){
            TextView child = (TextView) gridLayout.getChildAt(i);
            child.setText(Integer.toString(answers.get(i)));
        }
    }

    public void verifyAnswer(View view) {
        TextView answer = (TextView) view;

        int tagged = Integer.parseInt(answer.getText().toString());

        if (tagged == correctAnswer){
            numberOfRightAnswer++;
            feedbackTextView.setText("Correct!");
        }else {
            feedbackTextView.setText("Wrong :(");
        }
        feedbackTextView.setVisibility(View.VISIBLE);
        numberOfQuestions++;
        questions();
        rateTextView.setText(String.format("%d/%d", numberOfRightAnswer, numberOfQuestions));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);

        // set the timer up
        timerTextView = findViewById(R.id.timerTextView);

        countDown();


        // set the questions up
        questionTextView = findViewById(R.id.questionTextView);
        questions();

        // set the feedback up
        feedbackTextView = findViewById(R.id.feedbackTextView);

        //set the right answers view up
        rateTextView = findViewById(R.id.rateTextView);
        rateTextView.setText("0/0");
    }
}
