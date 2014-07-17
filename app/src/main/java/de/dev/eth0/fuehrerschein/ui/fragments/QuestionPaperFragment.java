//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import de.dev.eth0.fuehrerschein.Constants;
import de.dev.eth0.fuehrerschein.ExamMode;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.data.model.QuestionPaper;
import de.dev.eth0.fuehrerschein.ui.QuestionActivity;

/**
 * Fragement to display statistics for a category
 */
public class QuestionPaperFragment extends AbstractBaseFragment {

  private static final String TAG = QuestionPaperFragment.class.getName();

  private QuestionPaper mQuestionPaper;

  @Override
  public void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    String questionPaperId = getActivity().getIntent().getExtras().getString(Constants.EXTRA_QUESTION_PAPER_ID);
    mQuestionPaper = getFuehrerscheinApplication().getQuestionManager().getQuestionCollection(questionPaperId, QuestionPaper.class);
    Log.i(TAG, "Displaying question-paper with id " + mQuestionPaper.getId());
    getActivity().setTitle(mQuestionPaper.getTitle());
  }

  @Override
  public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer, Bundle pSavedInstanceState) {
    View rootView = pInflater.inflate(R.layout.fragment_question_paper, pContainer, false);
    return rootView;
  }

  @Override
  public void onViewCreated(View pView, Bundle pSavedInstanceState) {

    super.onViewCreated(pView, pSavedInstanceState);

    Button startButton = (Button)pView.findViewById(R.id.single_button);
    startButton.setText(R.string.question_paper_button_text);
    startButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View pView) {
        mQuestionPaper.resetQuestion();
        getFuehrerscheinApplication().getQuestionManager().setCurrentQuestionCollection(mQuestionPaper);
        getFuehrerscheinApplication().setExamMode(ExamMode.QUESTION_PAPER);
        startActivity(new Intent(getActivity(), QuestionActivity.class));
      }
    });

    ((TextView)pView.findViewById(R.id.question_paper_category_headline)).setText(mQuestionPaper.getCategory().getTitle());

    TextView text = (TextView)pView.findViewById(R.id.question_paper_category_text);
    text.setText(mQuestionPaper.getCategory().getDescription());
  }

}
