//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.database.model;

import android.database.Cursor;
import de.dev.eth0.fuehrerschein.data.database.tables.AnsweredQuestionsTable;

/**
 * A single entry in the AnsweredQuestions Table
 */
public class AnsweredQuestion {

  /**
   * Create from cursor
   *
   * @param pCursor
   * @return
   */
  public static AnsweredQuestion fromCursor(Cursor pCursor) {
    AnsweredQuestion ret = new AnsweredQuestion();

    pCursor.moveToFirst();
    ret.setQuestionId(pCursor.getString(pCursor.getColumnIndex(AnsweredQuestionsTable.COLUMN_ID)));
    ret.setCategoryId(pCursor.getString(pCursor.getColumnIndex(AnsweredQuestionsTable.COLUMN_CATEGORY)));
    ret.setNumberCorrectAnswers(pCursor.getInt(pCursor.getColumnIndex(AnsweredQuestionsTable.COLUMN_NUMBER_ANSWERS)));
    return ret;
  }

  private String mQuestionId;
  private String mCategoryId;
  private int mNumberCorrectAnswers;

  /**
   * @return
   */
  public String getQuestionId() {
    return mQuestionId;
  }

  /**
   * @param pQuestionId
   */
  public void setQuestionId(String pQuestionId) {
    this.mQuestionId = pQuestionId;
  }

  /**
   *
   * @return
   */
  public String getCategoryId() {
    return mCategoryId;
  }

  /**
   *
   * @param pCategoryId
   */
  public void setCategoryId(String pCategoryId) {
    this.mCategoryId = pCategoryId;
  }

  /**
   *
   * @return
   */
  public int getNumberCorrectAnswers() {
    return mNumberCorrectAnswers;
  }

  /**
   *
   * @param pNumberCorrectAnswers
   */
  public void setNumberCorrectAnswers(int pNumberCorrectAnswers) {
    this.mNumberCorrectAnswers = pNumberCorrectAnswers;
  }

  /**
   * Chances the number of correct answers according to the given value
   *
   * @param pCorrect
   */
  public void addAnswer(boolean pCorrect) {
    if (pCorrect) {
      if (this.mNumberCorrectAnswers < 5) {
        this.mNumberCorrectAnswers++;
      }
    }
    else if (mNumberCorrectAnswers > 0) {
      this.mNumberCorrectAnswers--;
    }
  }
}

