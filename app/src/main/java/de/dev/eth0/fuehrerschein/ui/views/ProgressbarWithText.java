//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.dev.eth0.fuehrerschein.R;

/**
 * Progressbar with text
 *
 */
public class ProgressbarWithText extends LinearLayout {

  private View mView;
  private ProgressBar mProgressbar;
  private TextView mTextView;

  /**
   *
   * @param pContext
   */
  public ProgressbarWithText(Context pContext) {
    super(pContext);
    inflate(pContext);

  }

  /**
   *
   * @param pContext
   * @param pAttrs
   */
  public ProgressbarWithText(Context pContext, AttributeSet pAttrs) {
    super(pContext, pAttrs);
    inflate(pContext);
  }

  private void inflate(Context pContext) {
    LayoutInflater inflater = (LayoutInflater)pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mView = inflater.inflate(R.layout.view_progressbar_with_text, this, true);
    mTextView = (TextView)mView.findViewById(R.id.view_progressbar_with_text_text);
    mProgressbar = (ProgressBar)mView.findViewById(R.id.view_progressbar_with_text_progressbar);
  }

  /**
   *
   * @param pProgress
   * @param pMax
   */
  public void setProgress(int pProgress, int pMax) {
    mProgressbar.setMax(pMax);
    mProgressbar.setProgress(pProgress);
    mTextView.setText(pProgress + "/" + pMax);
  }


}
