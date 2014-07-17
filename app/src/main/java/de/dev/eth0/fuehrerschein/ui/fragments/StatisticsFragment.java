//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.data.database.model.Statistics;
import de.dev.eth0.fuehrerschein.data.model.QuestionCollection;
import de.dev.eth0.fuehrerschein.ui.views.ProgressbarWithText;

/**
 * Fragement to display statistics for a category
 */
public class StatisticsFragment extends AbstractBaseFragment {

  private static final String TAG = StatisticsFragment.class.getName();

  private QuestionCollection mCategory;

  @Override
  public void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    mCategory = getFuehrerscheinApplication().getQuestionManager().getCurrentQuestionCollection();
    if (mCategory == null) {
      returnToStartScreen();
      return;
    }
    getActivity().setTitle(mCategory.getTitle());
    Log.i(TAG, "Displaying question-category with id " + mCategory.getId());
  }

  @Override
  public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer, Bundle pSavedInstanceState) {
    View rootView = pInflater.inflate(R.layout.fragment_statistics, pContainer, false);
    return rootView;
  }

  @Override
  public void onViewCreated(View pView, Bundle pSavedInstanceState) {
    super.onViewCreated(pView, pSavedInstanceState);
    if (mCategory == null) {
      return;
    }

    TextView categoryInfo = (TextView)pView.findViewById(R.id.statistics_headline);
    categoryInfo.setText(getString(R.string.statistics_headline, mCategory.getTitle()));

    Statistics statistics = getFuehrerscheinApplication().getDatabaseHelper().getStatistics(mCategory.getId());
    ProgressbarWithText progressBar;
    if (statistics != null) {
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_complete_progress);
      progressBar.setProgress(statistics.getNumberOfAnsweredQuestions(), mCategory.getQuestions().size());
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_no_correct_answers);
      progressBar.setProgress(mCategory.getQuestions().size() - statistics.getNumberOfAnsweredQuestions(), mCategory.getQuestions().size());
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_1_correct_answers);
      progressBar.setProgress(statistics.getEntry(1), mCategory.getQuestions().size());
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_2_correct_answers);
      progressBar.setProgress(statistics.getEntry(2), mCategory.getQuestions().size());
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_3_correct_answers);
      progressBar.setProgress(statistics.getEntry(3), mCategory.getQuestions().size());
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_4_correct_answers);
      progressBar.setProgress(statistics.getEntry(4), mCategory.getQuestions().size());
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_5_correct_answers);
      progressBar.setProgress(statistics.getEntry(5), mCategory.getQuestions().size());
    }
    else {
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_complete_progress);
      progressBar.setProgress(0, mCategory.getQuestions().size());
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_no_correct_answers);
      progressBar.setProgress(mCategory.getQuestions().size(), mCategory.getQuestions().size());
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_1_correct_answers);
      progressBar.setProgress(0, mCategory.getQuestions().size());
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_2_correct_answers);
      progressBar.setProgress(0, mCategory.getQuestions().size());
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_3_correct_answers);
      progressBar.setProgress(0, mCategory.getQuestions().size());
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_4_correct_answers);
      progressBar.setProgress(0, mCategory.getQuestions().size());
      progressBar = (ProgressbarWithText)pView.findViewById(R.id.statistics_5_correct_answers);
      progressBar.setProgress(0, mCategory.getQuestions().size());
    }
  }

}
