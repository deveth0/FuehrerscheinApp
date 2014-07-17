//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.test;

import android.content.res.AssetManager;
import android.test.AndroidTestCase;
import android.text.TextUtils;
import de.dev.eth0.fuehrerschein.data.model.Answer;
import de.dev.eth0.fuehrerschein.data.model.Question;
import de.dev.eth0.fuehrerschein.data.model.QuestionCategory;
import de.dev.eth0.fuehrerschein.data.model.QuestionPaper;
import de.dev.eth0.fuehrerschein.data.model.QuestionPaperCategory;
import de.dev.eth0.fuehrerschein.data.reader.QuestionReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Reads all questions and questionspapers and checks validity
 */
public class QuestionValidationTest extends AndroidTestCase {

  public void testValidity() throws IOException {
    Set<String> collectionIds = new HashSet<>();
    Set<String> questionIds = new HashSet<>();
    QuestionReader reader = new QuestionReader(getContext());

// Load all Questions
    AssetManager mngr = getContext().getAssets();
    for (String questionCategory : mngr.list("questions")) {
      QuestionCategory cat = reader.parse(mngr.open("questions/" + questionCategory), QuestionCategory.class);
      assertNotNull(questionCategory + " should not return null", cat);
      assertFalse(questionCategory + " has no id", TextUtils.isEmpty(cat.getId()));
      assertFalse(questionCategory + " has no title", TextUtils.isEmpty(cat.getTitle()));
      assertNotNull(questionCategory + " has no questions", cat.getQuestions());
      assertFalse(questionCategory + " has no questions", cat.getQuestions().isEmpty());
      assertTrue(questionCategory + "'s id is not unique", collectionIds.add(cat.getId()));

      for (Question question : cat.getQuestions()) {
        assertNotNull("Question should not be null", question);
        assertFalse(question.getId() + " has no id", TextUtils.isEmpty(question.getId()));
        assertFalse(question.getId() + " has no text", TextUtils.isEmpty(question.getText()));
        assertNotNull(question.getId() + " has no answers", question.getAnswers());
        assertFalse(question.getId() + " has no answers", question.getAnswers().isEmpty());
        assertNotNull(question.getId() + "has no correct answer", question.getCorrectAnswer());
        for (Answer answer : question.getAnswers()) {
          assertFalse(answer + " has no text", TextUtils.isEmpty(answer.getText()));
        }
        if (question.getImg() != null && !question.getImg().isEmpty()) {
          for (String img : question.getImg()) {
            int id = getContext().getResources().getIdentifier(img, "drawable", getContext().getPackageName());
            assertNotSame(img + " could not be resolved to a resource", 0, id);
            assertNotNull(img + " could not be loaded", getContext().getResources().getDrawable(id));
          }
        }
        assertTrue(question.getId() + " (question-id) is not unique", questionIds.add(question.getId()));
      }
    }
    for (String paper : mngr.list("questionpapers")) {
      QuestionPaperCategory papCat = reader.parse(mngr.open("questionpapers/" + paper), QuestionPaperCategory.class);
      assertNotNull(paper + " should not return null", papCat);
      assertFalse(paper + " has no id", TextUtils.isEmpty(papCat.getId()));
      assertFalse(paper + " has no title", TextUtils.isEmpty(papCat.getTitle()));
      assertFalse(paper + " has no description", TextUtils.isEmpty(papCat.getDescription()));
      assertTrue(paper + "'s id is not unique", collectionIds.add(papCat.getId()));
      assertNotNull(paper + " has no papers", papCat.getPapers());
      assertFalse(paper + " has no papers", papCat.getPapers().isEmpty());
      for (QuestionPaper pap : papCat.getPapers()) {
        assertFalse(pap.getId() + " has no id", TextUtils.isEmpty(pap.getId()));
        assertFalse(pap.getId() + " has no title", TextUtils.isEmpty(pap.getTitle()));
        assertTrue(pap.getId() + "'s id is not unique", collectionIds.add(pap.getId()));
        for (Question question : pap.getQuestions()) {
          assertNotNull("Question should not be null", question);
          assertFalse(question.getId() + " has no id", TextUtils.isEmpty(question.getId()));
          assertFalse(question.getId() + " has no text", TextUtils.isEmpty(question.getText()));
          assertNotNull(question.getId() + " has no answers", question.getAnswers());
          assertFalse(question.getId() + " has no answers", question.getAnswers().isEmpty());
          assertNotNull(question.getId() + "has no correct answer", question.getCorrectAnswer());
          for (Answer answer : question.getAnswers()) {
            assertFalse(answer + " has no text", TextUtils.isEmpty(answer.getText()));
          }
          if (question.getImg() != null && !question.getImg().isEmpty()) {
            for (String img : question.getImg()) {
              int id = getContext().getResources().getIdentifier(img, "drawable", getContext().getPackageName());
              assertNotSame(img + " could not be resolved to a resource", 0, id);
              assertNotNull(img + " could not be loaded", getContext().getResources().getDrawable(id));
            }
          }
          assertTrue(question.getId() + " (question-id) is not unique", questionIds.add(question.getId()));
        }
      }
    }
  }
}
