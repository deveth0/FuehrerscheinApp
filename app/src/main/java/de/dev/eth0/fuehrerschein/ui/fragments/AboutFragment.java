//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v4.preference.PreferenceFragment;
import de.dev.eth0.fuehrerschein.FuehrerscheinApplication;
import de.dev.eth0.fuehrerschein.R;


/**
 * Fragment for settings
 */
public class AboutFragment extends PreferenceFragment {
  private static final String KEY_ABOUT_VERSION = "about_version";
  private static final String KEY_ABOUT_AUTHOR = "about_author";
  private static final String KEY_ABOUT_AUTHOR_TWITTER = "about_author_twitter";
  private static final String KEY_ABOUT_COPYRIGHT_EXTERNAL = "about_external_copyright";
  //private static final String KEY_ABOUT_AUTHOR_LICENSE = "about_license";

  @Override
  public void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    addPreferencesFromResource(R.xml.about);
    findPreference(KEY_ABOUT_VERSION).setSummary(((FuehrerscheinApplication)getActivity().getApplication()).applicationVersionName());
  }
  @Override
  public boolean onPreferenceTreeClick(PreferenceScreen pPreferenceScreen, Preference pPreference) {
    String key = pPreference.getKey();
    if (KEY_ABOUT_AUTHOR_TWITTER.equals(key)) {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.about_author_twitter_url))));
      return true;
    }
    else if (KEY_ABOUT_AUTHOR.equals(key)) {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.about_author_url))));
      return true;
    }
    else if (KEY_ABOUT_COPYRIGHT_EXTERNAL.equals(key)) {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.about_external_copyright_url))));
      return true;
    }
//TODO: add if page is available
//    else if (KEY_ABOUT_AUTHOR_LICENSE.equals(key)) {
//      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.AUTHOR_SOURCE_URL)));
//      return true;
//    }
    return false;
  }
}
