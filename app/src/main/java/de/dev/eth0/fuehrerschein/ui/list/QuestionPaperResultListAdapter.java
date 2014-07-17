//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.list;

import android.view.View;
import android.widget.TextView;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.ui.BaseActivity;
import de.dev.eth0.fuehrerschein.ui.QuestionPaperResultActivity;

/**
 * List adapter to display the results of a question paper
 */
public class QuestionPaperResultListAdapter extends AbstractListAdapter<QuestionPaperResultActivity.QuestionPaperResult> {

  private static final String TAG = QuestionPaperResultListAdapter.class.getName();

  public QuestionPaperResultListAdapter(BaseActivity pActivity) {
    super(pActivity);
  }

  @Override
  public int getRowLayout(int pPosition) {
    return R.layout.row_question_result;
  }

  @Override
  public void bindView(View pRow, QuestionPaperResultActivity.QuestionPaperResult pEntry) {
    TextView title = (TextView)pRow.findViewById(R.id.row_question_result_question);
    title.setText(getActivity().getString(R.string.question_info, pEntry.getQuestionId()));
    title.setCompoundDrawablesWithIntrinsicBounds(null, null,
            getActivity().getResources().getDrawable(
                    pEntry.isCorrect() ? R.drawable.ok : R.drawable.wrong),
            null);

  }


}
