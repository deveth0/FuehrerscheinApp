//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import de.dev.eth0.fuehrerschein.data.model.Answer;

/**
 * Extension of radio buttons to save the answer
 *
 */
public class AnswerRadioButton extends RadioButton {

  private Answer mAnswer;

  /**
   *
   * @param pContext
   */
  public AnswerRadioButton(Context pContext) {
    super(pContext);
  }

  /**
   * @param pContext
   * @param pAttrs
   */
  public AnswerRadioButton(Context pContext, AttributeSet pAttrs) {
    super(pContext, pAttrs);
  }

  /**
   *
   * @param pContext
   * @param pAttrs
   * @param pDefStyle
   */
  public AnswerRadioButton(Context pContext, AttributeSet pAttrs, int pDefStyle) {
    super(pContext, pAttrs, pDefStyle);
  }

  /**
   *
   * @return
   */
  public Answer getAnswer() {
    return mAnswer;
  }

  /**
   *
   * @param pAnswer
   */
  public void setAnswer(Answer pAnswer) {
    this.mAnswer = pAnswer;
    this.setText(pAnswer.getText());
  }

}
