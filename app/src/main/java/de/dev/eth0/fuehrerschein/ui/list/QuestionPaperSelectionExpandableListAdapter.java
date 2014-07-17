//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.list;

import android.view.View;
import android.widget.TextView;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.data.model.QuestionPaper;
import de.dev.eth0.fuehrerschein.ui.BaseActivity;

/**
 * List adapter to display all available question categories, menu items and foo
 */
public class QuestionPaperSelectionExpandableListAdapter extends AbstractExpandableListAdapter<String, BaseListEntry> {

  private static final String TAG = QuestionPaperSelectionExpandableListAdapter.class.getName();
  private static final int TYPE_RANDOM_QUESTION_PAPER = 0;
  private static final int TYPE_QUESTION_PAPER = TYPE_RANDOM_QUESTION_PAPER + 1;
  private final BaseActivity mActivity;

  public QuestionPaperSelectionExpandableListAdapter(BaseActivity pActivity) {
    super(pActivity);
    mActivity = pActivity;
  }

  @Override
  public int getGroupLayout() {
    return R.layout.row_question_paper_selection_group;
  }

  @Override
  public int getChildLayout() {
    return R.layout.row_question_paper_selection_item;
  }

  @Override
  public void bindGroupView(View pGroup, String pEntry) {
    TextView header = (TextView)pGroup.findViewById(R.id.row_question_paper_selection_group_title);
    header.setText(pEntry);
  }

  @Override
  public void bindChildView(View pChild, BaseListEntry pEntry) {
    TextView child = (TextView)pChild.findViewById(R.id.row_question_paper_selection_item_title);
    child.setText(pEntry.getTitle());
    if (pEntry instanceof QuestionPaperListEntry) {
      if (((QuestionPaperListEntry)pEntry).isCorrect()) {
        pChild.findViewById(R.id.row_question_paper_selection_item_correct).setVisibility(View.VISIBLE);
      }
      else {
        pChild.findViewById(R.id.row_question_paper_selection_item_correct).setVisibility(View.GONE);
      }
    }
  }

  @Override
  public int getChildType(int pGroupPosition, int pChildPosition) {
    BaseListEntry entry = getChild(pGroupPosition, pChildPosition);
    if (entry instanceof RandomQuestionPaperListEntry) {
      return TYPE_RANDOM_QUESTION_PAPER;
    }
    return TYPE_QUESTION_PAPER;
  }

  @Override
  public int getChildTypeCount() {
    return 2;
  }

  /**
   * Entry to select a random question pape
   */
  public static class RandomQuestionPaperListEntry extends BaseListEntry {

    public RandomQuestionPaperListEntry(String pTitle) {
      super(pTitle);
    }
  }

  /**
   * Item to select a QuestionPaper
   */
  public static class QuestionPaperListEntry extends BaseListEntry {

    private final QuestionPaper mQuestionPaper;
    private final boolean mCorrect;

    /**
     * @param pQuestionPaper
     * @param pCorrect
     */
    public QuestionPaperListEntry(QuestionPaper pQuestionPaper, boolean pCorrect) {
      super(pQuestionPaper.getTitle());
      this.mQuestionPaper = pQuestionPaper;
      this.mCorrect = pCorrect;
    }

    /**
     *
     * @return
     */
    public QuestionPaper getQuestionPaper() {
      return mQuestionPaper;
    }

    /**
     * @return
     */
    public boolean isCorrect() {
      return mCorrect;
    }

  }

}
