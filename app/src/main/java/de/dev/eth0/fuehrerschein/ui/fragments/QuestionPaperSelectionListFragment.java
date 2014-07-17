//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import de.dev.eth0.fuehrerschein.Constants;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.data.model.QuestionPaper;
import de.dev.eth0.fuehrerschein.ui.BaseActivity;
import de.dev.eth0.fuehrerschein.ui.QuestionPaperActivity;
import de.dev.eth0.fuehrerschein.ui.list.BaseListEntry;
import de.dev.eth0.fuehrerschein.ui.list.QuestionPaperSelectionExpandableListAdapter;
import de.dev.eth0.fuehrerschein.util.NaturalOrderComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Fragment to display a list of available question papers
 */
public class QuestionPaperSelectionListFragment extends AbstractBaseFragment {

  private static final String TAG = QuestionPaperSelectionListFragment.class.getName();
  private QuestionPaperSelectionExpandableListAdapter mAdapter;

  @Override
  public void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    mAdapter = new QuestionPaperSelectionExpandableListAdapter((BaseActivity)getActivity());
    // build entries
    //TODO: add "Random entry"
    //test1.add(new QuestionPaperSelectionExpandableListAdapter.RandomQuestionPaperListEntry(getString(R.string.question_paper_selection_random)));
    Map<String, List<BaseListEntry>> tmpEntries = new LinkedHashMap<>();

    for (QuestionPaper paper : getFuehrerscheinApplication().getQuestionManager().getAllQuestionCollections(QuestionPaper.class)) {
      List<BaseListEntry> categoryList = tmpEntries.get(paper.getCategory().getTitle());
      if (categoryList == null) {
        categoryList = new ArrayList<>();
        tmpEntries.put(paper.getCategory().getTitle(), categoryList);
      }
      boolean correct = getFuehrerscheinApplication().getDatabaseHelper().isCollectionAnswered(paper);
      categoryList.add(new QuestionPaperSelectionExpandableListAdapter.QuestionPaperListEntry(paper, correct));
    }
    // order entries
    Comparator comp = new NaturalOrderComparator();
    for (Entry<String, List<BaseListEntry>> entry : tmpEntries.entrySet()) {
      Collections.sort(entry.getValue(), comp);
    }
    mAdapter.replace(tmpEntries);
  }

  @Override
  public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer,
          Bundle pSavedInstanceState) {
    View rootView = pInflater.inflate(R.layout.fragment_question_paper_selection, pContainer, false);
    ExpandableListView list = (ExpandableListView)rootView.findViewById(R.id.question_paper_expandable_list);
    list.setAdapter(mAdapter);
    list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

      @Override
      public boolean onChildClick(ExpandableListView pParent, View pChild, int pGroupPosition, int pChildPosition, long pId) {
        BaseListEntry entry = mAdapter.getChild(pGroupPosition, pChildPosition);
        Intent intent = new Intent(getActivity(), QuestionPaperActivity.class);

        if (entry instanceof QuestionPaperSelectionExpandableListAdapter.RandomQuestionPaperListEntry) {
          QuestionPaper paper = getFuehrerscheinApplication().getQuestionManager().getRandomQuestionCollection(QuestionPaper.class);
          intent.putExtra(Constants.EXTRA_QUESTION_PAPER_ID, paper.getId());
        }
        else if (entry instanceof QuestionPaperSelectionExpandableListAdapter.QuestionPaperListEntry) {
          QuestionPaperSelectionExpandableListAdapter.QuestionPaperListEntry paperEntry
                  = (QuestionPaperSelectionExpandableListAdapter.QuestionPaperListEntry)entry;
          intent.putExtra(Constants.EXTRA_QUESTION_PAPER_ID, paperEntry.getQuestionPaper().getId());
        }
        if (intent.hasExtra(Constants.EXTRA_QUESTION_PAPER_ID)) {
          startActivity(intent);
          return true;
        }
        return false;
      }
    });
    return rootView;
  }

}
