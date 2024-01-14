package pl.edu.pb.wi;

import static pl.edu.pb.wi.R.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private TextView questionTextView;

    private Question[] questions = new Question[]{
            new Question(string.q_1,false),
            new Question(string.q_2, true),
            new Question(string.q_3, true),
            new Question(string.q_4, false),
            new Question(string.q_5,true),
            new Question(string.q_6,false)
    };

    private static final String KEY_CURRENT_INDEX = "currentIndex";
    private int currentIndex = 0;
    public static final String KEY_EXTRA_ANSWER = "pl.edu.pb.wi.quiz.correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;
    private boolean answerWasShown;
    String tag = "Cykl życia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button promptButton;
        Button nextButton;
        Button trueButton;
        Button falseButton;
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX, 0);
        }

        Log.d(tag, "Metoda onCreate została wywołana");

        trueButton = findViewById(id.trueButton);
        falseButton = findViewById(id.falseButton);
        nextButton = findViewById(id.nextButton);
        questionTextView = findViewById(id.textView);
        promptButton = findViewById(id.promptButton);

        trueButton.setOnClickListener(v -> checkAnswerCorrectness(true));

        falseButton.setOnClickListener(v -> checkAnswerCorrectness(false));
        nextButton.setOnClickListener(v -> {
            currentIndex = (currentIndex+1)%questions.length;
            answerWasShown = false;
            setNextQuestion();
        });

        promptButton.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, PromptActivity.class);
            boolean correctAnswer = questions[currentIndex].isTrueAnswer();
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
            startActivityForResult(intent, REQUEST_CODE_PROMPT);
        });

        setNextQuestion();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(tag, "Metoda onStart została wywołana");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(  tag, "Metoda onResume została wywołana");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(tag, "Metoda onPause została wywołana");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(tag, "Metoda onStop została wywołana");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(tag, "Metoda onDestroy została wywołana");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.d(tag, "Metoda onSaveInstanceState została wywołana");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }

    private void checkAnswerCorrectness (boolean userAnswer){
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId = 0;
        if (answerWasShown){
            resultMessageId = R.string.answer_was_shown;
        } else {
            if (userAnswer == correctAnswer) {
                resultMessageId = string.correct_answer;
            } else {
                resultMessageId = string.incorrect_answer;
            }
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
    }
    private void setNextQuestion(){
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {return;}
        if (requestCode == REQUEST_CODE_PROMPT){
            if (data==null) {return;}
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }
}