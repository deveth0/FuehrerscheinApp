//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Answer
 */
public class Answer {
  @JsonProperty("text")
  private String mText;
  @JsonProperty("correct")
  private boolean mCorrect;

  /**
   * Constructor to set correct to false by default
   */
  public Answer() {
    this.mCorrect = false;
  }

  /**
   * @return
   */
  public String getText() {
    return mText;
  }

  /**
   *
   * @param pText
   */
  public void setText(String pText) {
    this.mText = pText;
  }

  /**
   * @return
   */
  public boolean isCorrect() {
    return mCorrect;
  }

  /**
   *
   * @param pCorrect
   */
  public void setCorrect(boolean pCorrect) {
    this.mCorrect = pCorrect;
  }

}
