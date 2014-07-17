//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A question paper
 */
public class QuestionPaper implements QuestionCollection {

  @JsonProperty("id")
  private String mId;
  @JsonProperty("title")
  private String mTitle;
  @JsonProperty("copyright")
  private Copyright mCopyright;
  @JsonProperty("questions")
  private List<String> mQuestionIds;
  private QuestionPaperCategory mQuestionPaperCategory;
  private final Map<String, Question> mQuestionsById = new LinkedHashMap<>();

  private int mLastQuestionPosition = 0;

  @Override
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

  @Override
  public String getTitle() {
    return mTitle;
  }

  /**
   *
   * @return
   */
  public QuestionPaperCategory getCategory() {
    return mQuestionPaperCategory;
  }

  /**
   *
   * @param pQuestionPaperCategory
   */
  @JsonBackReference
  public void setCategory(QuestionPaperCategory pQuestionPaperCategory) {
    this.mQuestionPaperCategory = pQuestionPaperCategory;
  }

  /**
   * @param pTitle
   */
  public void setTitle(String pTitle) {
    this.mTitle = pTitle;
  }


  /**
   * @param pQuestions
   */
  public void setQuestions(List<String> pQuestions) {
    this.mQuestionIds = pQuestions;
  }

  @Override
  public Copyright getCopyright() {
    return mCopyright;
  }

  /**
   * @param pCopyright
   */
  public void setCopyright(Copyright pCopyright) {
    this.mCopyright = pCopyright;
  }

  /**
   * @return
   */
  public List<String> getQuesitonIds() {
    return mQuestionIds;
  }

  @Override
  public List<Question> getQuestions() {
    return new ArrayList<>(mQuestionsById.values());
  }

  @Override
  public Question getQuestion(String pQuestionId) {
    return mQuestionsById.get(pQuestionId);
  }

  /**
   * resets the finished questions
   */
  public void resetQuestion() {
    mLastQuestionPosition = 0;
  }

  /**
   * @return next question to use
   */
  public Question getNextQuestion() {
    // no question left, reset
    if (mLastQuestionPosition >= mQuestionIds.size()) {
      resetQuestion();
      return null;
    }
    return mQuestionsById.get(mQuestionIds.get(mLastQuestionPosition++));
  }

  /**
   * resolves the questionId with the given map
   *
   * @param pQuestionsById
   */
  public void resolveQuestions(Map<String, Question> pQuestionsById) {
    for (String questionId : mQuestionIds) {
      mQuestionsById.put(questionId, pQuestionsById.get(questionId));
    }
  }

  /**
   *
   * @return index of current question
   */
  public int getQuestionIndex() {
    return mLastQuestionPosition;
  }

}
