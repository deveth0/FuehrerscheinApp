//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import de.dev.eth0.fuehrerschein.FuehrerscheinApplication;
import de.dev.eth0.fuehrerschein.ui.StartScreenActivity;

/**
 * superclass for list fragments
 */
public class AbstractBaseListFragment extends ListFragment {

  private FuehrerscheinApplication mApplication;

  @Override
  public void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    mApplication = (FuehrerscheinApplication)getActivity().getApplication();
  }

  /**
   *
   * @return the FuehrerscheinApplication
   */
  public FuehrerscheinApplication getFuehrerscheinApplication() {
    return mApplication;
  }

  /**
   * Finishes the current activity and returns to startscreen. should be called to avoid NPEs
   */
  protected void returnToStartScreen() {
    startActivity(new Intent(getActivity(), StartScreenActivity.class));
    getActivity().finish();
  }
}
