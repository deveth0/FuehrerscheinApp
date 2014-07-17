//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Question
 */
public class Question {

  @JsonProperty("id")
  private String mId;
  @JsonProperty("text")
  private String mText;
  @JsonProperty("img")
  private List<String> mImg;
  @JsonProperty("answers")
  private List<Answer> mAnswers;
  @JsonProperty("copyright")
  private Copyright mCopyright;
  private QuestionCategory mQuestionCategory;

  /**
   *
   * @return
   */
  public String getId() {
    return mId;
  }

  /**
   * @param pId
   */
  public void setId(String pId) {
    this.mId = pId;
  }

  /**
   *
   * @return
   */
  public List<String> getImg() {
    return mImg;
  }

  /**
   *
   * @param pImg
   */
  public void setImg(List<String> pImg) {
    this.mImg = pImg;
  }

  /**
   *
   * @return
   */
  public String getText() {
    return mText;
  }

  /**
   * @param pText
   */
  public void setText(String pText) {
    this.mText = pText;
  }

  /**
   *
   * @return
   */
  public List<Answer> getAnswers() {
    return mAnswers;
  }

  /**
   * @param pAnwers
   */
  public void setAnswers(List<Answer> pAnwers) {
    this.mAnswers = pAnwers;
  }

  /**
   *
   * @return
   */
  public Copyright getCopyright() {
    return mCopyright;
  }

  /**
   *
   * @param pCopyright
   */
  public void setCopyright(Copyright pCopyright) {
    this.mCopyright = pCopyright;
  }

  public QuestionCategory getQuestionCategory() {
    return mQuestionCategory;
  }

  @JsonBackReference
  public void setQuestionCategory(QuestionCategory pQuestionCategory) {
    this.mQuestionCategory = pQuestionCategory;
  }

  /**
   * Returns the correct answer for this question
   *
   * @return
   */
  public Answer getCorrectAnswer() {
    for (Answer answer : mAnswers) {
      if (answer.isCorrect()) {
        return answer;
      }
    }
    return null;
  }

}
