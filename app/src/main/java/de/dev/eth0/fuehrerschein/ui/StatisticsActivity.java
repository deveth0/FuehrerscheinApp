//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.ui.dialogs.HelpDialogFragment;
import de.dev.eth0.fuehrerschein.ui.fragments.StatisticsFragment;

/**
 * Activity for statistics about the current question category
 */
public class StatisticsActivity extends BaseActivity {

  private static final String TAG = QuestionActivity.class.getName();

  @Override
  protected void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    setContentView(R.layout.activity_statistics);
    if (pSavedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
              .add(R.id.container, new StatisticsFragment())
              .commit();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu pMenu) {
    super.onCreateOptionsMenu(pMenu);
    getMenuInflater().inflate(R.menu.help, pMenu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem pItem) {
    switch (pItem.getItemId()) {
      case R.id.menu_help:
        HelpDialogFragment.showHelpDialog(getSupportFragmentManager(), "help_statistics");
        return true;
    }
    return super.onOptionsItemSelected(pItem);
  }
}
