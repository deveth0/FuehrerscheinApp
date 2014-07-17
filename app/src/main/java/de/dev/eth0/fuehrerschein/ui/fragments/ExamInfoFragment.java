//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.dev.eth0.fuehrerschein.R;

/**
 * Fragement to display information about the exam
 */
public class ExamInfoFragment extends AbstractBaseFragment {

  private static final String TAG = ExamInfoFragment.class.getName();


  @Override
  public View onCreateView(LayoutInflater pInflater, ViewGroup pContainer, Bundle pSavedInstanceState) {
    View rootView = pInflater.inflate(R.layout.fragment_exam_info, pContainer, false);
    return rootView;
  }

}
