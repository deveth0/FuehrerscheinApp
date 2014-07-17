//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.list;

/**
 * Abstract class which can be used as base for ListEntries
 */
public class BaseListEntry {

  private final String mTitle;

  /**
   *
   * @param pTitle
   */
  public BaseListEntry(String pTitle) {
    this.mTitle = pTitle;
  }

  /**
   *
   * @return
   */
  public String getTitle() {
    return mTitle;
  }

  @Override
  public String toString() {
    return mTitle;
  }

}
