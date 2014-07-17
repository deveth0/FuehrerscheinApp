//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.fragments;

import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;
import de.dev.eth0.fuehrerschein.R;

/**
 * Fragment for settings
 */
public class SettingsFragment extends PreferenceFragment {

  @Override
  public void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    addPreferencesFromResource(R.xml.settings);
  }
}
