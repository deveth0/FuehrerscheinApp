//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data;

import de.dev.eth0.fuehrerschein.data.model.Copyright;
import de.dev.eth0.fuehrerschein.data.model.Question;
import de.dev.eth0.fuehrerschein.data.model.QuestionCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * class used to display all questions from all categories
 */
public class AllCategoriesQuestionCollection implements QuestionCollection {
  /**
   * ID
   */
  public static final String ALL_CATEGORIES_ID = "allCategories";

  private final String mTitle;
  private final Map<String, Question> mQuestions = new LinkedHashMap<>();

  /**
   *
   * @param pTitle
   */
  public AllCategoriesQuestionCollection(String pTitle) {
    this.mTitle = pTitle;
  }

  /**
   * Adds all questions to the collection
   *
   * @param pQuestions
   */
  public void addQuestions(Collection<Question> pQuestions) {
    for (Question question : pQuestions) {
      mQuestions.put(question.getId(), question);
    }
  }

  @Override
  public String getId() {
    return ALL_CATEGORIES_ID;
  }

  @Override
  public String getTitle() {
    return mTitle;
  }

  @Override
  public Copyright getCopyright() {
    return null;
  }

  @Override
  public List<Question> getQuestions() {
    return new ArrayList<>(mQuestions.values());
  }

  @Override
  public Question getQuestion(String pQuestionId) {
    return mQuestions.get(pQuestionId);
  }


}
