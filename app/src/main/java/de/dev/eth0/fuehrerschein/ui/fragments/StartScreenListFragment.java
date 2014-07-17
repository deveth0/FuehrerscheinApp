//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.dev.eth0.fuehrerschein.ExamMode;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.data.AllCategoriesQuestionCollection;
import de.dev.eth0.fuehrerschein.data.model.QuestionCategory;
import de.dev.eth0.fuehrerschein.ui.BaseActivity;
import de.dev.eth0.fuehrerschein.ui.ExamInfoActivity;
import de.dev.eth0.fuehrerschein.ui.QuestionActivity;
import de.dev.eth0.fuehrerschein.ui.QuestionPaperSelectionActivity;
import de.dev.eth0.fuehrerschein.ui.list.BaseListEntry;
import de.dev.eth0.fuehrerschein.ui.list.StartScreenListAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display a list of available question categories
 */
public class StartScreenListFragment extends AbstractBaseListFragment {

  private static final String TAG = StartScreenListFragment.class.getName();
  private StartScreenListAdapter mAdapter;

  @Override
  public void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    mAdapter = new StartScreenListAdapter((BaseActivity)getActivity());
    setListAdapter(mAdapter);
  }

  @Override
  public void onResume() {
    super.onResume();
    // build entries
    List<BaseListEntry> entries = new ArrayList<>();
    entries.add(new StartScreenListAdapter.HeadlineListEntry(getString(R.string.start_categories_headline)));
    AllCategoriesQuestionCollection all = new AllCategoriesQuestionCollection(getString(R.string.start_all_categories));
    entries.add(new StartScreenListAdapter.QuestionCollectionListEntry(all));
    for (QuestionCategory category : getFuehrerscheinApplication().getQuestionManager().getAllQuestionCollections(QuestionCategory.class)) {
      entries.add(new StartScreenListAdapter.QuestionCollectionListEntry(category));
      all.addQuestions(category.getQuestions());
    }

    entries.add(new StartScreenListAdapter.HeadlineListEntry(getString(R.string.start_test_headline)));
    entries.add(new StartScreenListAdapter.MenuListEntry(getString(R.string.start_test_select_questionpaper), QuestionPaperSelectionActivity.class));
    //entries.add(new StartScreenListAdapter.MenuListEntry(getString(R.string.start_test_random_questions), StartScreenActivity.class));
    entries.add(new StartScreenListAdapter.MenuListEntry(getString(R.string.exam_info_title), ExamInfoActivity.class));
    mAdapter.replace(entries);
  }

  @Override
  public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer,
          Bundle pSavedInstanceState) {
    View rootView = pInflater.inflate(R.layout.fragment_start_screen_list, pContainer, false);
    return rootView;
  }

  @Override
  public void onListItemClick(ListView pListView, View pView, int pPosition, long pId) {
    BaseListEntry entry = mAdapter.getItem(pPosition);
    if (entry instanceof StartScreenListAdapter.QuestionCollectionListEntry) {
      StartScreenListAdapter.QuestionCollectionListEntry category = (StartScreenListAdapter.QuestionCollectionListEntry)entry;
      getFuehrerscheinApplication().setExamMode(ExamMode.TRAINING);
      getFuehrerscheinApplication().getQuestionManager().setCurrentQuestionCollection(category.getQuestionCollection());
      startActivity(new Intent(getActivity(), QuestionActivity.class));
    }
    else if (entry instanceof StartScreenListAdapter.MenuListEntry) {
      StartScreenListAdapter.MenuListEntry menuentry = (StartScreenListAdapter.MenuListEntry)entry;
      startActivity(new Intent(getActivity(), menuentry.getTargetActivity()));
    }
  }
}
