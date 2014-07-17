//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data;

import android.content.res.AssetManager;
import android.util.Log;
import de.dev.eth0.fuehrerschein.FuehrerscheinApplication;
import de.dev.eth0.fuehrerschein.data.database.model.Statistics;
import de.dev.eth0.fuehrerschein.data.model.Question;
import de.dev.eth0.fuehrerschein.data.model.QuestionCategory;
import de.dev.eth0.fuehrerschein.data.model.QuestionCollection;
import de.dev.eth0.fuehrerschein.data.model.QuestionPaper;
import de.dev.eth0.fuehrerschein.data.model.QuestionPaperCategory;
import de.dev.eth0.fuehrerschein.data.reader.QuestionReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Manager for questions.
 */
public class QuestionManager {

  private static final String TAG = QuestionManager.class.getName();
  private final FuehrerscheinApplication mApplication;
  private QuestionCollection mCurrentCollection;
  private List<Question> mCurrentQuestions;
  private int mLastIndex;

  private final Map<String, QuestionCategory> mQuestionCategoryById;
  private Map<String, QuestionPaper> mQuestionPaperById;
  private final Map<String, Question> mQuestionsById;

  /**
   *
   * @param pApplication
   */
  public QuestionManager(FuehrerscheinApplication pApplication) {
    mApplication = pApplication;
    QuestionReader reader = new QuestionReader(mApplication);
    // load all questioncategories & papers
    mQuestionCategoryById = new LinkedHashMap<>();
    AssetManager mngr = mApplication.getAssets();
    try {
      for (String question : mngr.list("questions")) {
        Log.d(TAG, "Loading category " + question);
        QuestionCategory cat = reader.parse(mngr.open("questions/" + question), QuestionCategory.class);
        Log.d(TAG, "cat = " + cat);
        if (cat != null) {
          mQuestionCategoryById.put(cat.getId(), cat);
        }
      }
    }
    catch (IOException ex) {
      Log.e(TAG, "Could not load questions", ex);
    }
    // build questionmap and resolve questions for the questionpapers
    mQuestionsById = new HashMap<>();
    for (QuestionCategory category : mQuestionCategoryById.values()) {
      mQuestionsById.putAll(category.getQuestionsById());
    }
  }

  /**
   * @return next question from the given category
   *
   *
   */
  public Question getNextQuestion() {
    if (mCurrentCollection == null) {
      return null;
    }
    if (mCurrentCollection instanceof QuestionPaper) {
      Log.d(TAG, "collection is a quesitonpaper, return next question");
      return ((QuestionPaper)mCurrentCollection).getNextQuestion();
    }

    if (mCurrentQuestions == null) {
      Log.d(TAG, "no more questions left, shuffeling remaining");
      mCurrentQuestions = new ArrayList<>(mCurrentCollection.getQuestions());
      Collections.shuffle(mCurrentQuestions);
      mLastIndex = 0;
    }
    Log.d(TAG, "Checking collection " + (mCurrentCollection == null ? "null" : mCurrentCollection.getId()));
    // check if there are any question left, otherwise return null;
    Statistics stats = mApplication.getDatabaseHelper().getStatistics(mCurrentCollection.getId());
    if (stats != null && stats.getEntry(5) == mCurrentQuestions.size()) {
      Log.d(TAG, "Number of finished questions: " + stats.getEntry(5));
      return null;
    }
    if (mLastIndex == mCurrentQuestions.size()) {
      Collections.shuffle(mCurrentQuestions);
      mLastIndex = 0;
    }
    Question ret = mCurrentQuestions.get(mLastIndex++);
    if (mApplication.getDatabaseHelper().getAnsweredQuestion(ret).getNumberCorrectAnswers() == 5) {
      return getNextQuestion();
    }
    return ret;
  }

  /**
   * Sets the current question category
   *
   * @param pType
   * @param <T>
   * @param pCategoryId
   */
  public <T extends QuestionCollection> void setCurrentQuestionCategory(String pCategoryId, Class<T> pType) {
    setCurrentQuestionCollection(getQuestionCollection(pCategoryId, pType));
  }

  /**
   * Sets the current question category
   *
   * @param pQuestionCollection
   */
  public void setCurrentQuestionCollection(QuestionCollection pQuestionCollection) {
    mCurrentCollection = pQuestionCollection;
    mCurrentQuestions = null;
  }

  /**
   * @return the currently selected QuestionCategory
   */
  public QuestionCollection getCurrentQuestionCollection() {
    return mCurrentCollection;
  }

  /**
   * Returns the question collection with the given id and type
   *
   * @param <T>
   * @param pId
   * @param pClass
   * @return
   */
  public <T extends QuestionCollection> T getQuestionCollection(String pId, Class<T> pClass) {
    if (pClass.isAssignableFrom(QuestionPaper.class)) {
      return (T)getQuestionPapersById().get(pId);
    }
    if (pClass.isAssignableFrom(QuestionCategory.class)) {
      return (T)mQuestionCategoryById.get(pId);
    }
    return null;
  }

  /**
   * List with all collections of the given type
   *
   * @param <T>
   * @param pClass
   * @return
   */
  public <T extends QuestionCollection> Collection<T> getAllQuestionCollections(Class<T> pClass) {
    if (pClass.isAssignableFrom(QuestionPaper.class)) {
      return (Collection<T>)getQuestionPapersById().values();
    }
    if (pClass.isAssignableFrom(QuestionCategory.class)) {
      return (Collection<T>)mQuestionCategoryById.values();
    }
    return new ArrayList<>();
  }

  public <T extends QuestionCollection> T getRandomQuestionCollection(Class<T> pClass) {
    List<T> all = new ArrayList<>(getAllQuestionCollections(pClass));
    return all.get(new Random().nextInt(all.size()));

  }

  private Map<String, QuestionPaper> getQuestionPapersById() {
    if (mQuestionPaperById == null) {
      loadQuestionPapers();
    }
    return mQuestionPaperById;
  }

  private void loadQuestionPapers() {
    AssetManager mngr = mApplication.getAssets();
    QuestionReader reader = new QuestionReader(mApplication);
    mQuestionPaperById = new LinkedHashMap<>();
    try {
      for (String paper : mngr.list("questionpapers")) {
        Log.d(TAG, "Loading paper " + paper);
        QuestionPaperCategory paperCategory = reader.parse(mngr.open("questionpapers/" + paper), QuestionPaperCategory.class);
        if (paperCategory != null && !paperCategory.getPapers().isEmpty()) {
          for (QuestionPaper pap : paperCategory.getPapers()) {
            mQuestionPaperById.put(pap.getId(), pap);
          }
        }
      }
      for (QuestionPaper pap : mQuestionPaperById.values()) {
        pap.resolveQuestions(mQuestionsById);
      }
    }
    catch (IOException ex) {
      Log.e(TAG, "Could not load questions", ex);
    }
  }
}
