//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import de.dev.eth0.fuehrerschein.Constants;
import de.dev.eth0.fuehrerschein.ExamMode;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.data.database.model.AnsweredQuestion;
import de.dev.eth0.fuehrerschein.data.model.Answer;
import de.dev.eth0.fuehrerschein.data.model.Copyright;
import de.dev.eth0.fuehrerschein.data.model.Question;
import de.dev.eth0.fuehrerschein.data.model.QuestionCollection;
import de.dev.eth0.fuehrerschein.data.model.QuestionPaper;
import de.dev.eth0.fuehrerschein.ui.QuestionActivity;
import de.dev.eth0.fuehrerschein.ui.dialogs.ImageDetailDialogBuilder;
import de.dev.eth0.fuehrerschein.ui.views.AnswerRadioButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragement to display a single question
 */
public class QuestionFragment extends AbstractBaseFragment implements View.OnClickListener {

  private static final String TAG = QuestionFragment.class.getName();

  private QuestionCollection mCollection;
  private Question mQuestion;
  private AnsweredQuestion mAnsweredQuestion;
  private List<Answer> mShuffledAnswers;
  private Button mSubmitButton;
  private Button mSkipButton;
  private ScrollView mScrollView;
  private RadioGroup mAnswers;
  private AnswerRadioButton mAnswer1;
  private AnswerRadioButton mAnswer2;
  private AnswerRadioButton mAnswer3;
  private AnswerRadioButton mAnswer4;
  private ProgressBar mPaperProgress;
  private int mCorrectAnswerButtonId;

  @Override
  public void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    // if there are no results, go back to start
    if (!getActivity().getIntent().hasExtra(Constants.EXTRA_QUESTION_ID)) {
      // Use the Builder class for convenient dialog construction
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setCancelable(false);
      builder.setMessage(R.string.dialog_question_category_finished_text)
              .setPositiveButton(R.string.dialog_question_category_finished_reset, new DialogInterface.OnClickListener() {
                @Override
        public void onClick(DialogInterface pDialog, int pId) {
          getFuehrerscheinApplication().getDatabaseHelper().resetCollection(
                  getFuehrerscheinApplication().getQuestionManager().getCurrentQuestionCollection());
          QuestionActivity activity = (QuestionActivity)getActivity();
          activity.onQuestionAnswered(null, null);
        }
              })
              .setNegativeButton(R.string.dialog_question_category_finished_back, new DialogInterface.OnClickListener() {
                @Override
        public void onClick(DialogInterface pDialog, int pId) {
                  returnToStartScreen();
                }
              });
      AlertDialog alert = builder.show();
      alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.font_size_normal));
      alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.font_size_normal));
      return;
    }
    String questionId = getActivity().getIntent().getExtras().getString(Constants.EXTRA_QUESTION_ID);
    Log.i(TAG, "Displaying question with id " + questionId);
    mCollection = getFuehrerscheinApplication().getQuestionManager().getCurrentQuestionCollection();
    mQuestion = mCollection.getQuestion(questionId);
    mAnsweredQuestion = getFuehrerscheinApplication().getDatabaseHelper().getAnsweredQuestion(mQuestion);
    mShuffledAnswers = new ArrayList<>(mQuestion.getAnswers());
    Collections.shuffle(mShuffledAnswers);
    getActivity().setTitle(mCollection.getTitle());
  }

  @Override
  public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer, Bundle pSavedInstanceState) {
    View rootView = pInflater.inflate(R.layout.fragment_question, pContainer, false);
    return rootView;
  }

  @Override
  public void onViewCreated(View pView, Bundle pSavedInstanceState) {

    super.onViewCreated(pView, pSavedInstanceState);
    mScrollView = (ScrollView)pView.findViewById(R.id.question_scrollview);
    mAnswers = (RadioGroup)pView.findViewById(R.id.question_answers);
    mSkipButton = (Button)pView.findViewById(R.id.question_button_skip);
    mSubmitButton = (Button)pView.findViewById(R.id.question_button_next);
    if (mQuestion == null) {
      // hide everything...
      mScrollView.setVisibility(View.GONE);
      mSkipButton.setVisibility(View.GONE);
      mSubmitButton.setVisibility(View.GONE);
      return;
    }
    if (mQuestion.getImg() != null && !mQuestion.getImg().isEmpty()) {
      Log.v(TAG, "Question has an image defined: " + mQuestion.getImg().get(0));
      ImageView questionsImage = (ImageView)pView.findViewById(R.id.question_image);
      final int imgId = getResources().getIdentifier(mQuestion.getImg().get(0), "drawable", getActivity().getPackageName());
      final int imgId2 = (mQuestion.getImg().size() > 1)
              ? getResources().getIdentifier(mQuestion.getImg().get(1), "drawable", getActivity().getPackageName())
              : 0;
      
      questionsImage.setImageBitmap(scaleBitmap(BitmapFactory.decodeResource(getResources(), imgId)));
      questionsImage.setVisibility(View.VISIBLE);
      if (mQuestion.getImg().size() > 1) {
        Log.v(TAG, "Question has another image defined: " + mQuestion.getImg().get(1));
        questionsImage = (ImageView)pView.findViewById(R.id.question_image_2);
        questionsImage.setImageBitmap(scaleBitmap(BitmapFactory.decodeResource(getResources(), imgId2)));
        questionsImage.setVisibility(View.VISIBLE);
      }
      View imageWrapper = pView.findViewById(R.id.question_image_wrapper);
      imageWrapper.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View pView) {
          AlertDialog.Builder builder = new ImageDetailDialogBuilder(getActivity(), imgId, imgId2);
          AlertDialog alert = builder.show();
          alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                  getResources().getDimensionPixelSize(R.dimen.font_size_normal));
          alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                  getResources().getDimensionPixelSize(R.dimen.font_size_normal));
        }
      });

    }
    else {
      pView.findViewById(R.id.question_image_wrapper).setVisibility(View.GONE);
    }

    TextView questionText = (TextView)pView.findViewById(R.id.question_text);
    questionText.setText(mQuestion.getText());

    TextView questionInfo = (TextView)pView.findViewById(R.id.question_info);
    questionInfo.setText(getString(R.string.question_info, mQuestion.getId()));

    TextView questionStatistic = (TextView)pView.findViewById(R.id.question_statistic);
    questionStatistic.setText(getString(R.string.question_number_answers, mAnsweredQuestion.getNumberCorrectAnswers()));

    mSubmitButton.setEnabled(false);
    mSubmitButton.setOnClickListener(this);

    if (getFuehrerscheinApplication().getExamMode() != ExamMode.TRAINING) {
      mPaperProgress = (ProgressBar)pView.findViewById(R.id.question_progress);
      mPaperProgress.setVisibility(View.VISIBLE);
      mPaperProgress.setMax(mCollection.getQuestions().size());
      mPaperProgress.setProgress(((QuestionPaper)mCollection).getQuestionIndex() - 1);
      mSubmitButton.setText(R.string.question_next);
      questionStatistic.setVisibility(View.GONE);
      mSkipButton.setVisibility(View.GONE);
    }
    else {
      mSkipButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View pView) {
          QuestionActivity activity = (QuestionActivity)getActivity();
          activity.onQuestionAnswered(mQuestion, null);
        }
      });
    }

    Copyright copyright = mQuestion.getCopyright() == null ? mQuestion.getQuestionCategory().getCopyright() : mQuestion.getCopyright();
    if (copyright != null) {
      TextView questionCopyRight = (TextView)pView.findViewById(R.id.question_copyright);
      questionCopyRight.setText(getString(R.string.question_copyright, copyright.getText()));
      questionCopyRight.setVisibility(View.VISIBLE);
    }

    mAnswers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(RadioGroup pGroup, int pCheckedId) {
        mSubmitButton.setEnabled(true);
      }
    });

    Answer answer = mShuffledAnswers.get(0);
    mAnswer1 = (AnswerRadioButton)pView.findViewById(R.id.question_answer_1);
    mAnswer1.setAnswer(answer);
    if (answer.isCorrect()) {
      mCorrectAnswerButtonId = mAnswer1.getId();
    }
    answer = mShuffledAnswers.get(1);
    mAnswer2 = (AnswerRadioButton)pView.findViewById(R.id.question_answer_2);
    mAnswer2.setAnswer(answer);
    if (answer.isCorrect()) {
      mCorrectAnswerButtonId = mAnswer2.getId();
    }
    answer = mShuffledAnswers.get(2);
    mAnswer3 = (AnswerRadioButton)pView.findViewById(R.id.question_answer_3);
    mAnswer3.setAnswer(answer);
    if (answer.isCorrect()) {
      mCorrectAnswerButtonId = mAnswer3.getId();
    }
    answer = mShuffledAnswers.get(3);
    mAnswer4 = (AnswerRadioButton)pView.findViewById(R.id.question_answer_4);
    mAnswer4.setAnswer(answer);
    if (answer.isCorrect()) {
      mCorrectAnswerButtonId = mAnswer4.getId();
    }
  }

  @Override
  public void onClick(View pView) {
    int selectedId = mAnswers.getCheckedRadioButtonId();
    final AnswerRadioButton selected = (AnswerRadioButton)mAnswers.findViewById(selectedId);
    if (getFuehrerscheinApplication().getExamMode() == ExamMode.TRAINING) {
      // Check if answer was correct and increase or decrease number of correct answers
      Log.d(TAG, selected.getAnswer().isCorrect() ? "The selected answer was correct" : "The selected answer was wrong");
      mAnsweredQuestion.addAnswer(selected.getAnswer().isCorrect());
      getFuehrerscheinApplication().getDatabaseHelper().updateAnsweredQuestion(mAnsweredQuestion);

      // mark correct answer green and scroll to it
      mAnswers.findViewById(mCorrectAnswerButtonId).setBackgroundColor(getResources().getColor(R.color.green));
      for (int i = 0; i < mAnswers.getChildCount(); i++) {
        mAnswers.getChildAt(i).setEnabled(false);
      }
      mSkipButton.setEnabled(false);
      mScrollView.post(new Runnable() {
        @Override
        public void run() {
          mScrollView.smoothScrollTo(0, selected.getTop());
        }
      });

      mSubmitButton.setText(R.string.question_next);
      mSubmitButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View pView) {
          QuestionActivity activity = (QuestionActivity)getActivity();
          activity.onQuestionAnswered(mQuestion, selected.getAnswer());
        }
      });
    }
    else {
      QuestionActivity activity = (QuestionActivity)getActivity();
      activity.onQuestionAnswered(mQuestion, selected.getAnswer());
    }
  }

  private Bitmap scaleBitmap(Bitmap bitmap) {
    int nh = (int)(bitmap.getHeight() * (512.0 / bitmap.getWidth()));
    return Bitmap.createScaledBitmap(bitmap, 512, nh, true);
  }
}
