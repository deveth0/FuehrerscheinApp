//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Question-category
 */
public class QuestionCategory implements QuestionCollection {

  @JsonProperty("id")
  private String mId;
  @JsonProperty("title")
  private String mTitle;
  @JsonProperty("copyright")
  private Copyright mCopyright;
  @JsonProperty("questions")
  private List<Question> mQuestions;
  private Map<String, Question> mQuestionsById;

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
   * @param pTitle
   */
  public void setTitle(String pTitle) {
    this.mTitle = pTitle;
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

  @Override
  public List<Question> getQuestions() {
    return mQuestions;
  }

  /**
   * @param pQuestions
   */
  @JsonManagedReference
  public void setQuestions(List<Question> pQuestions) {
    this.mQuestions = pQuestions;
  }

  /**
   * Builds a map with all questions accessible by their id
   */
  private void createQuestionsMap() {
    mQuestionsById = new HashMap<>();
    if (mQuestions != null) {
      for (Question question : mQuestions) {
        mQuestionsById.put(question.getId(), question);
      }
    }
  }

  /**
   *
   * @param pId
   * @return
   */
  @Override
  public Question getQuestion(String pId) {
    if (mQuestionsById == null) {
      createQuestionsMap();
    }
    return mQuestionsById.get(pId);
  }

  /**
   * @return map with all questions by string
   */
  public Map<String, Question> getQuestionsById() {
    if (mQuestionsById == null) {
      createQuestionsMap();
    }
    return mQuestionsById;
  }

}
