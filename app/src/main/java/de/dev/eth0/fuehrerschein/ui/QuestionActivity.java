//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import de.dev.eth0.fuehrerschein.Constants;
import de.dev.eth0.fuehrerschein.ExamMode;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.data.model.Answer;
import de.dev.eth0.fuehrerschein.data.model.Question;
import de.dev.eth0.fuehrerschein.ui.dialogs.HelpDialogFragment;
import de.dev.eth0.fuehrerschein.ui.fragments.QuestionFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity which displays questions. It uses fragments for each single question
 */
public class QuestionActivity extends BaseActivity {

  private static final String TAG = QuestionActivity.class.getName();

  /**
   * Holding the answered questions, only used for exams
   */
  private List<QuestionPaperResultActivity.QuestionPaperResult> mAnsweredQuestions;

  @Override
  protected void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    setContentView(R.layout.activity_question);
    mAnsweredQuestions = new ArrayList<>();
    if (pSavedInstanceState == null) {
      // show first question
      onQuestionAnswered(null, null);
    }
  }

  /**
   * Called when a question has been answered. Displays the next question
   *
   * @param pAnsweredQuestion
   * @param pChosenAnswer
   */
  public void onQuestionAnswered(Question pAnsweredQuestion, Answer pChosenAnswer) {
    getSupportFragmentManager().popBackStack();
    Question nextQuestion = getFuehrerscheinApplication().getQuestionManager().getNextQuestion();
    if (getFuehrerscheinApplication().getExamMode() == ExamMode.QUESTION_PAPER) {
      // save last answer
      if (pAnsweredQuestion != null && pChosenAnswer != null) {
        QuestionPaperResultActivity.QuestionPaperResult result = new QuestionPaperResultActivity.QuestionPaperResult(
                pAnsweredQuestion.getId(), pChosenAnswer.getText(), pChosenAnswer.isCorrect());
        mAnsweredQuestions.add(result);
      }
      if (nextQuestion == null) {
        Log.d(TAG, "This was the last answer, starting result activity");
        Intent intent = new Intent(this, QuestionPaperResultActivity.class);
        intent.putExtra(Constants.EXTRA_EXAM_RESULT, mAnsweredQuestions.toArray(new Parcelable[0]));
        startActivity(intent);
        this.finish();
        return;
      }
    }
    if (nextQuestion != null) {
      Log.i(TAG, "Using question-id " + nextQuestion.getId());
      getIntent().putExtra(Constants.EXTRA_QUESTION_ID, nextQuestion.getId());
    }
    else {
      getIntent().removeExtra(Constants.EXTRA_QUESTION_ID);
    }
    getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, new QuestionFragment())
            .commit();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu pMenu) {
    if (getFuehrerscheinApplication().getExamMode() != ExamMode.QUESTION_PAPER) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.questions, pMenu);
      return true;
    }
    return false;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem pItem) {
    int id = pItem.getItemId();
    switch (id) {
      case R.id.menu_questions_statistics:
        startActivity(new Intent(this, StatisticsActivity.class));
        return true;
      case R.id.menu_help:
        HelpDialogFragment.showHelpDialog(getSupportFragmentManager(), "help_questions");
        return true;
    }
    return super.onOptionsItemSelected(pItem);
  }

}
