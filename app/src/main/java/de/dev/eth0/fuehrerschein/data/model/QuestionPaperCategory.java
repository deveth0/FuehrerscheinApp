//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * a category of quesitonpapers
 *
 * @author amuthmann
 */
public class QuestionPaperCategory {

  @JsonProperty("id")
  private String mId;

  @JsonProperty("title")
  private String mTitle;

  @JsonProperty("description")
  private String mDescription;

  @JsonProperty("requiredAnswers")
  private int mRequiredAnswers;

  @JsonProperty("papers")
  private List<QuestionPaper> mQuestionPapers;

  /**
   *
   * @return
   */
  public String getId() {
    return mId;
  }

  /**
   *
   * @param pId
   */
  public void setId(String pId) {
    this.mId = pId;
  }

  /**
   *
   * @return
   */
  public String getTitle() {
    return mTitle;
  }

  /**
   *
   * @param pTitle
   */
  public void setTitle(String pTitle) {
    this.mTitle = pTitle;
  }

  /**
   *
   * @return
   */
  public String getDescription() {
    return mDescription;
  }

  /**
   *
   * @param pDescription
   */
  public void setDescription(String pDescription) {
    this.mDescription = pDescription;
  }

  /**
   *
   * @return
   */
  public List<QuestionPaper> getPapers() {
    return mQuestionPapers;
  }

  /**
   *
   * @param pQuestionPapers
   */
  @JsonManagedReference
  public void setPapers(List<QuestionPaper> pQuestionPapers) {
    this.mQuestionPapers = pQuestionPapers;
  }

  /**
   *
   * @return
   */
  public int getRequiredAnswers() {
    return mRequiredAnswers;
  }

  /**
   *
   * @param pRequiredAnswers
   */
  public void setRequiredAnswers(int pRequiredAnswers) {
    this.mRequiredAnswers = pRequiredAnswers;
  }


}
