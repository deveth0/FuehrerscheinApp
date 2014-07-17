//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui;

import android.os.Bundle;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.ui.fragments.ExamInfoFragment;

/**
 * Activity for info about the exam
 */
public class ExamInfoActivity extends BaseActivity {
  private static final String TAG = QuestionActivity.class.getName();

  @Override
  protected void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    setContentView(R.layout.activity_exam_info);
    if (pSavedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
              .add(R.id.container, new ExamInfoFragment())
              .commit();
    }
  }
}
