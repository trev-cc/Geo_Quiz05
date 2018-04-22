package com.example.la983452.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.geoquiz.CheatActivity;
import com.bignerdranch.android.geoquiz.Question;

public class QuizActivity extends AppCompatActivity {


    public static final String TAG = "QuizActivity";
    public static final String KEY_INDEX = "index";
    public static final String CHEATER = "cheater";
    public static final String QUESTIONS_CHEATED = "questions_cheated";
    public static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;

    private Button mCheatButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };


    private int[] mCheatingBank = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    public QuizActivity() {
        super();
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                if (mCheatingBank[mCurrentIndex] != 1) {
                    messageResId = R.string.correct_toast;
                } else if (mCheatingBank[mCurrentIndex] == 1) {
                    messageResId = R.string.judgement_toast;
                }
            } else {
                if (mCheatingBank[mCurrentIndex] != 1) {
                    messageResId = R.string.incorrect_toast;
                } else if (mCheatingBank[mCurrentIndex] == 1) {
                    messageResId = R.string.judgement_toast;
                }
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle called");
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });


        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(CHEATER, false);
            mCheatingBank = savedInstanceState.getIntArray(QUESTIONS_CHEATED);
        }
        updateQuestion();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
        if (mIsCheater) {
            mCheatingBank[mCurrentIndex] = 1;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putBoolean(CHEATER, mIsCheater);
        outState.putIntArray(QUESTIONS_CHEATED, mCheatingBank);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart(Bundle called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause(Bundle called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume(Bundle called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop(Bundle called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy(Bundle called");
    }
}
