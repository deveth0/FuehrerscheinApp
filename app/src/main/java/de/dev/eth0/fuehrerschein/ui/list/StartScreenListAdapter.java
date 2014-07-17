//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.list;

import android.view.View;
import android.widget.TextView;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.data.database.model.Statistics;
import de.dev.eth0.fuehrerschein.data.model.QuestionCollection;
import de.dev.eth0.fuehrerschein.ui.BaseActivity;
import de.dev.eth0.fuehrerschein.ui.list.StartScreenListAdapter.QuestionCollectionListEntry;

/**
 * List adapter to display all available question categories, menu items and foo
 */
public class StartScreenListAdapter extends AbstractListAdapter<BaseListEntry> {

  private static final String TAG = StartScreenListAdapter.class.getName();
  private static final int TYPE_HEADLINE = 0;
  private static final int TYPE_QUESTION_CATEGORY = TYPE_HEADLINE + 1;
  private static final int TYPE_MENU_ITEM = TYPE_QUESTION_CATEGORY + 1;

  public StartScreenListAdapter(BaseActivity pActivity) {
    super(pActivity);
  }

  @Override
  public int getItemViewType(int pPosition) {
    BaseListEntry entry = getItem(pPosition);
    if (entry instanceof QuestionCollectionListEntry) {
      return TYPE_QUESTION_CATEGORY;
    }
    else if (entry instanceof MenuListEntry) {
      return TYPE_MENU_ITEM;
    }
    return TYPE_HEADLINE;
  }

  @Override
  public int getRowLayout(int pPosition) {
    switch (getItemViewType(pPosition)) {
      case TYPE_QUESTION_CATEGORY:
        return R.layout.row_question_category;
      case TYPE_MENU_ITEM:
        return R.layout.row_menu_item;
      default:
        return R.layout.row_headline;
    }
  }

  @Override
  public void bindView(View pRow, BaseListEntry pEntry) {
    if (pEntry instanceof QuestionCollectionListEntry) {
      QuestionCollectionListEntry categoryEntry = (QuestionCollectionListEntry)pEntry;
      TextView categoryTitle = (TextView)pRow.findViewById(R.id.row_question_category_title);
      categoryTitle.setText(pEntry.getTitle());
      if (categoryEntry.getQuestionCollection() != null) {
        Statistics statistics = getActivity().getFuehrerscheinApplication().getDatabaseHelper().getStatistics(categoryEntry.getQuestionCollection().getId());
        TextView categoryOpen = (TextView)pRow.findViewById(R.id.row_question_category_open);
      categoryOpen.setText(getActivity().getString(
                R.string.row_question_category_open,
                statistics != null ? statistics.getEntry(5) : 0,
                categoryEntry.getQuestionCollection().getQuestions().size()));
      }
    }
    else if (pEntry instanceof MenuListEntry) {
      TextView title = (TextView)pRow.findViewById(R.id.row_menu_item_title);
      title.setText(pEntry.getTitle());
    }
    else if (pEntry instanceof HeadlineListEntry) {
      TextView title = (TextView)pRow.findViewById(R.id.row_headline_title);
      title.setText(pEntry.getTitle());
    }
  }

  /**
   * A simple headline
   */
  public static class HeadlineListEntry extends BaseListEntry {

    public HeadlineListEntry(String pTitle) {
      super(pTitle);
    }
  }

  /**
   * Menu List entry
   */
  public static class MenuListEntry extends BaseListEntry {

    private final Class mTargetActivity;

    /**
     *
     * @param pTitle
     * @param pTargetActivity
     */
    public MenuListEntry(String pTitle, Class pTargetActivity) {
      super(pTitle);
      mTargetActivity = pTargetActivity;
    }

    public Class getTargetActivity() {
      return mTargetActivity;
    }

  }

  /**
   * Extends QuestionCategory by a reference to the file-Id
   */
  public static class QuestionCollectionListEntry extends BaseListEntry {

    private final QuestionCollection mQuestionCategory;

    /**
     * @param pQuestionCollection
     */
    public QuestionCollectionListEntry(QuestionCollection pQuestionCollection) {
      super(pQuestionCollection.getTitle());
      this.mQuestionCategory = pQuestionCollection;
    }

    /**
     *
     * @return
     */
    public QuestionCollection getQuestionCollection() {
      return mQuestionCategory;
    }

  }


}
