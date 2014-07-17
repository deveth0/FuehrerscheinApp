//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Copyright
 */
public class Copyright {
  @JsonProperty("text")
  private String mText;
  @JsonProperty("src")
  private String mSrc;

  /**
   *
   * @return the text
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
   *
   * @return
   */
  public String getSrc() {
    return mSrc;
  }

  /**
   *
   * @param pSrc
   */
  public void setSrc(String pSrc) {
    this.mSrc = pSrc;
  }

}
