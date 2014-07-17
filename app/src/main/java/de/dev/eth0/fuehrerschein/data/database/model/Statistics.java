//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.database.model;

import android.database.Cursor;
import de.dev.eth0.fuehrerschein.data.database.tables.AnsweredQuestionsTable;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper to hold statistics about answered questions
 */
public class Statistics {

  /**
   * name of column used for select
   */
  public static final String NUMBER_OF_QUESTIONS = "NumberOfQuestions";

  /**
   * Create from cursor
   *
   * @param pCursor
   * @return
   */
  public static Statistics fromCursor(Cursor pCursor) {
    Statistics ret = new Statistics();

    pCursor.moveToFirst();
    while (!pCursor.isAfterLast()) {
      int numAnswers = pCursor.getInt(pCursor.getColumnIndex(AnsweredQuestionsTable.COLUMN_NUMBER_ANSWERS));
      int numQuestions = pCursor.getInt(pCursor.getColumnIndex(NUMBER_OF_QUESTIONS));
      ret.addEntry(numAnswers, numQuestions);
      pCursor.moveToNext();
    }
    return ret;
  }

  private final Map<Integer, Integer> mEntries = new HashMap<>();
  private int mNumberOfAnsweredQuestions;

  /**
   *
   * @param pNumberAnswers
   * @param pNumberQuestions
   */
  public void addEntry(Integer pNumberAnswers, Integer pNumberQuestions) {
    mEntries.put(pNumberAnswers, pNumberQuestions);
    mNumberOfAnsweredQuestions += pNumberQuestions;
  }

  /**
   *
   * @return
   */
  public int getNumberOfAnsweredQuestions() {
    return mNumberOfAnsweredQuestions;
  }

  /**
   * Returns the entries
   *
   * @return
   */
  public Map<Integer, Integer> getEntries() {
    return mEntries;
  }

  /**
   *
   * @param pNumberAnswers
   * @return
   */
  public int getEntry(int pNumberAnswers) {
    return mEntries.containsKey(pNumberAnswers) ? mEntries.get(pNumberAnswers) : 0;
  }

}
