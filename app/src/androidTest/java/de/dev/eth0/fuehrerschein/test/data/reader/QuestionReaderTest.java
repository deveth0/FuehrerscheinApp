//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.test.data.reader;

import android.test.AndroidTestCase;
import de.dev.eth0.fuehrerschein.data.model.Question;
import de.dev.eth0.fuehrerschein.data.model.QuestionCategory;
import de.dev.eth0.fuehrerschein.data.reader.QuestionReader;
import java.io.IOException;

public class QuestionReaderTest extends AndroidTestCase {

  public void testReadQuestionCategory() throws IOException {
    QuestionReader reader = new QuestionReader(getContext());
    QuestionCategory category = reader.parse(getContext().getAssets().open("questions/4_questions_test.json"), QuestionCategory.class);
    assertNotNull("Category has not been loaded", category);
    assertEquals("Category title has not been loaded", "SBF-Binnen Test", category.getTitle());
    assertNotNull("Copyright has not been loaded", category.getCopyright());
    assertEquals("Copyright title has not been loaded", "Testfragen", category.getCopyright().getText());
    assertEquals("Copyright src has not been loaded", "https://dev-eth0.de", category.getCopyright().getSrc());
    assertNotNull("Questions haven't been loaded (== null)", category.getQuestions());
    assertFalse("No questions have been loaded (isEmpty)", category.getQuestions().isEmpty());
    assertEquals("Not enough questions have been loaded", 3, category.getQuestions().size());

    Question q1 = category.getQuestions().get(0);
    assertEquals("Question id does not match", "t1", q1.getId());
    assertEquals("Question text does not match", "Frage 1?", q1.getText());
    assertEquals("Not enough answers", 4, q1.getAnswers().size());
    assertTrue("Answer 1 is not correct", q1.getAnswers().get(0).isCorrect());
    assertEquals("Answer 1 text does not match", "Richtig.",
            q1.getAnswers().get(0).getText());
    assertFalse("Answer 2 should not be correct", q1.getAnswers().get(1).isCorrect());
    assertEquals("Answer 2 text does not match", "Falsch.",
            q1.getAnswers().get(1).getText());

    Question q2 = category.getQuestions().get(1);
    assertEquals("Quesiton id", "t2", q2.getId());
    assertTrue("Question img", !q2.getImg().isEmpty());
    assertEquals("Question img", "basis_5", q2.getImg().get(0));

    Question q3 = category.getQuestions().get(2);
    assertEquals("Quesiton id", "t3", q3.getId());
    assertTrue("Question img", !q3.getImg().isEmpty());
    assertEquals("Question img", "basis_23", q3.getImg().get(0));
    assertEquals("Question img", "basis_23_2", q3.getImg().get(1));
  }
}
