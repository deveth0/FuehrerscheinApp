//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.model;

import java.util.List;

/**
 * Interface for questioncollections
 */
public interface QuestionCollection {

  /**
   *
   * @return
   */
  String getId();

  /**
   *
   * @return
   */
  String getTitle();

  /**
   *
   * @return
   */
  Copyright getCopyright();

  /**
   *
   * @return
   */
  List<Question> getQuestions();

  /**
   *
   * @param pQuestionId
   * @return
   */
  Question getQuestion(String pQuestionId);

}
