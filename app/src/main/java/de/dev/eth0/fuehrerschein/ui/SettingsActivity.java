//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui;

import android.os.Bundle;
import de.dev.eth0.fuehrerschein.ui.fragments.SettingsFragment;
/**
 * Settings activity
 */
public class SettingsActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    getSupportFragmentManager().beginTransaction()
            .replace(android.R.id.content, new SettingsFragment())
            .commit();

  }
}
