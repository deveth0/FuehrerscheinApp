//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui;

import android.os.Bundle;
import de.dev.eth0.fuehrerschein.ui.fragments.AboutFragment;

/**
 * The about activity
 */
public class AboutActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    getSupportFragmentManager().beginTransaction()
            .replace(android.R.id.content, new AboutFragment())
            .commit();

  }
}
