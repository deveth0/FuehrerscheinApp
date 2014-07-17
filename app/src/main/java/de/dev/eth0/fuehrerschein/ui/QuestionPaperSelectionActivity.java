//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui;

import android.os.Bundle;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.ui.fragments.QuestionPaperSelectionListFragment;

/**
 * Activity which displays all available Questionpapers
 */
public class QuestionPaperSelectionActivity extends BaseActivity {

  private static final String TAG = QuestionPaperSelectionActivity.class.getName();
  @Override
  protected void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    setContentView(R.layout.activity_question_paper_selection);
    if (pSavedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
              .add(R.id.container, new QuestionPaperSelectionListFragment())
              .commit();
    }
  }
}
