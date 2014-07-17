//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.dev.eth0.fuehrerschein.R;

/**
 * Main-Fragment for start-screen
 */
public class StartScreenMainFragment extends AbstractBaseFragment {

  @Override
  public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer,
          Bundle pSavedInstanceState) {
    View rootView = pInflater.inflate(R.layout.fragment_start_screen, pContainer, false);
    return rootView;
  }

}
