//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui;

import android.os.Bundle;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.ui.fragments.QuestionPaperFragment;

/**
 * Activity to display questions of a question paper and present a result afterwards
 *
 */
public class QuestionPaperActivity extends BaseActivity {
  private static final String TAG = QuestionPaperSelectionActivity.class.getName();

  @Override
  protected void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    setContentView(R.layout.activity_question_paper);
    if (pSavedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
              .add(R.id.container, new QuestionPaperFragment())
              .commit();
    }
  }
}
