//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import de.dev.eth0.fuehrerschein.Constants;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.ui.dialogs.HelpDialogFragment;

/**
 * The startscreen
 */
public class StartScreenActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    setContentView(R.layout.activity_start_screen);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu pMenu) {

    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.start_screen, pMenu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem pItem) {
    int id = pItem.getItemId();
    switch (id) {
      case R.id.menu_start_screen_about:
        startActivity(new Intent(this, AboutActivity.class));
        return true;
      case R.id.menu_start_screen_feedback:
        Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", Constants.REPORT_EMAIL, null));
        email.putExtra(Intent.EXTRA_SUBJECT, Constants.FEEDBACK_SUBJECT);
        startActivity(Intent.createChooser(email, null));
        return true;
      case R.id.menu_help:
        HelpDialogFragment.showHelpDialog(getSupportFragmentManager(), "help");
        return true;
      case android.R.id.home:
        return false;
//      case R.id.menu_start_screen_settings:
//        startActivity(new Intent(this, SettingsActivity.class));
//        return true;
    }
    return super.onOptionsItemSelected(pItem);
  }

}
