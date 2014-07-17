//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import de.dev.eth0.fuehrerschein.Constants;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.data.model.Question;
import de.dev.eth0.fuehrerschein.data.model.QuestionPaper;
import de.dev.eth0.fuehrerschein.ui.BaseActivity;
import de.dev.eth0.fuehrerschein.ui.QuestionPaperResultActivity.QuestionPaperResult;
import de.dev.eth0.fuehrerschein.ui.StartScreenActivity;
import de.dev.eth0.fuehrerschein.ui.dialogs.QuestionPaperResultDialogBuilder;
import de.dev.eth0.fuehrerschein.ui.list.QuestionPaperResultListAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragement to display a single question
 */
public class QuestionPaperResultListFragment extends AbstractBaseListFragment {

  private static final String TAG = QuestionPaperResultListFragment.class.getName();
  private final List<QuestionPaperResult> mResults = new ArrayList<>();
  private int mNumberCorrectAnswers = 0;
  private QuestionPaperResultListAdapter mAdapter;
  private boolean mSuccess;
  private int mNumberRequiredAnswers;

  @Override
  public void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    mAdapter = new QuestionPaperResultListAdapter((BaseActivity)getActivity());
    Parcelable[] parcels = (Parcelable[])getActivity().getIntent().getParcelableArrayExtra(Constants.EXTRA_EXAM_RESULT);
    if (parcels != null) {
      for (Parcelable parcel : parcels) {
        if (parcel instanceof QuestionPaperResult) {
          QuestionPaperResult result = (QuestionPaperResult)parcel;
          mResults.add(result);
          if (result.isCorrect()) {
            mNumberCorrectAnswers++;
          }
        }
      }
    }
    Log.d(TAG, "Got " + mResults.size() + " results to display");
    mAdapter.replace(mResults);
    setListAdapter(mAdapter);
    QuestionPaper currentPaper = (QuestionPaper)getFuehrerscheinApplication().getQuestionManager().getCurrentQuestionCollection();
    mNumberRequiredAnswers = currentPaper.getCategory().getRequiredAnswers();
    mSuccess = (mNumberCorrectAnswers >= currentPaper.getCategory().getRequiredAnswers());
    getFuehrerscheinApplication().getDatabaseHelper().addAnsweredCollection(currentPaper, mSuccess);
  }

  @Override
  public void onResume() {
    super.onResume();
    if (getFuehrerscheinApplication().getQuestionManager().getCurrentQuestionCollection() == null
            || mAdapter.getCount() == 0) {
      returnToStartScreen();
    }
  }

  @Override
  public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer, Bundle pSavedInstanceState) {
    View rootView = pInflater.inflate(R.layout.fragment_question_paper_result, pContainer, false);
    return rootView;
  }

  @Override
  public void onViewCreated(View pView, Bundle pSavedInstanceState) {
    super.onViewCreated(pView, pSavedInstanceState);

    TextView mInfoText = (TextView)pView.findViewById(R.id.question_paper_result_info);
    mInfoText.setText(getString(
            mSuccess ? R.string.question_paper_result_success : R.string.question_paper_result_failure,
            mNumberCorrectAnswers, mResults.size(), mNumberRequiredAnswers));

    Button finishButton = (Button)pView.findViewById(R.id.single_button);
    finishButton.setText(R.string.finish);
    finishButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View pView) {
        startActivity(new Intent(getActivity(), StartScreenActivity.class));
        getActivity().finish();
      }

    });
  }

  @Override
  public void onListItemClick(ListView pListView, View pView, int pPosition, long pId) {
    QuestionPaperResult entry = mAdapter.getItem(pPosition);
    Question question = getFuehrerscheinApplication().getQuestionManager().getCurrentQuestionCollection().getQuestion(entry.getQuestionId());
    QuestionPaperResultDialogBuilder dialog = new QuestionPaperResultDialogBuilder(getActivity(), question, entry);
    AlertDialog alert = dialog.show();
    alert.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.font_size_normal));
    alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.font_size_normal));
  }

}
