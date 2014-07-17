//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import de.dev.eth0.fuehrerschein.data.AllCategoriesQuestionCollection;
import de.dev.eth0.fuehrerschein.data.database.model.AnsweredQuestion;
import de.dev.eth0.fuehrerschein.data.database.model.Statistics;
import de.dev.eth0.fuehrerschein.data.database.tables.AnsweredQuestionCollectionsTable;
import de.dev.eth0.fuehrerschein.data.database.tables.AnsweredQuestionsTable;
import de.dev.eth0.fuehrerschein.data.model.Question;
import de.dev.eth0.fuehrerschein.data.model.QuestionCollection;

/**
 * Database helper to access the database
 */
public class DatabaseHelper extends SQLiteOpenHelper {

  private static final String TAG = DatabaseHelper.class.getName();

  private static final String DATABASE_NAME = "sbf-binnen.db";
  private static final int DATABASE_VERSION = 3;

  public DatabaseHelper(Context pContext) {
    super(pContext, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase pDatabase) {
    Log.d(TAG, "onCreate");
    AnsweredQuestionsTable.onCreate(pDatabase);
    AnsweredQuestionCollectionsTable.onCreate(pDatabase);
  }

  @Override
  public void onUpgrade(SQLiteDatabase pDatabase, int pOldVersion, int pNewVersion) {
    Log.d(TAG, "onUpgrade");
    AnsweredQuestionsTable.onUpgrade(pDatabase, pOldVersion, pNewVersion);
    AnsweredQuestionCollectionsTable.onUpgrade(pDatabase, pOldVersion, pNewVersion);
  }

  /**
   *
   * @param pQuestion
   * @return
   */
  public AnsweredQuestion getAnsweredQuestion(Question pQuestion) {
    Cursor cursor = getReadableDatabase().query(AnsweredQuestionsTable.TABLE_NAME,
            null, AnsweredQuestionsTable.COLUMN_ID + " =?",
            new String[]{pQuestion.getId()}, null, null, null);
    if (cursor != null && cursor.moveToFirst() && cursor.getColumnCount() > 0) {
      Log.i(TAG, "AnsweredQuestion for Question " + pQuestion.getId() + " of category " + pQuestion.getQuestionCategory().getId() + " is already in database");
      return AnsweredQuestion.fromCursor(cursor);
    }
    return addAnsweredQuestion(pQuestion);
  }

  /**
   *
   * @param pQuestion
   * @return
   */
  public AnsweredQuestion addAnsweredQuestion(Question pQuestion) {
    Log.v(TAG, "addAnsweredQuestion(" + pQuestion.getId() + ", " + pQuestion.getQuestionCategory().getId() + ")");
    ContentValues content = new ContentValues();
    content.put(AnsweredQuestionsTable.COLUMN_ID, pQuestion.getId());
    content.put(AnsweredQuestionsTable.COLUMN_CATEGORY, pQuestion.getQuestionCategory().getId());
    SQLiteDatabase db = getWritableDatabase();
    db.insert(AnsweredQuestionsTable.TABLE_NAME, null, content);
    return getAnsweredQuestion(pQuestion);
  }

  /**
   * Saves the given question in database
   *
   * @param pQuestion
   * @return success
   */
  public boolean updateAnsweredQuestion(AnsweredQuestion pQuestion) {
    Log.d(TAG, "updateAnsweredQuestion(" + pQuestion.getQuestionId() + ")");
    ContentValues content = new ContentValues();
    content.put(AnsweredQuestionsTable.COLUMN_CATEGORY, pQuestion.getCategoryId());
    content.put(AnsweredQuestionsTable.COLUMN_NUMBER_ANSWERS, pQuestion.getNumberCorrectAnswers());

    return getWritableDatabase().update(AnsweredQuestionsTable.TABLE_NAME,
            content,
            AnsweredQuestionsTable.COLUMN_ID + " =?",
            new String[]{pQuestion.getQuestionId()})
            == 1;
  }

  /**
   * Reads the database and builds up a statistics object
   *
   * @param pCategoryId
   * @return
   */
  public Statistics getStatistics(String pCategoryId) {
    Cursor cursor;
    if (TextUtils.equals(pCategoryId, AllCategoriesQuestionCollection.ALL_CATEGORIES_ID)) {
      cursor = getReadableDatabase().query(AnsweredQuestionsTable.TABLE_NAME,
              new String[]{AnsweredQuestionsTable.COLUMN_NUMBER_ANSWERS, "count(*) as " + Statistics.NUMBER_OF_QUESTIONS},
              AnsweredQuestionsTable.COLUMN_NUMBER_ANSWERS + " <> 0",
              null,
              AnsweredQuestionsTable.COLUMN_NUMBER_ANSWERS,
              null,
              null);
    }
    else {
      cursor = getReadableDatabase().query(AnsweredQuestionsTable.TABLE_NAME,
              new String[]{AnsweredQuestionsTable.COLUMN_NUMBER_ANSWERS, "count(*) as " + Statistics.NUMBER_OF_QUESTIONS},
              AnsweredQuestionsTable.COLUMN_CATEGORY + " =? and " + AnsweredQuestionsTable.COLUMN_NUMBER_ANSWERS + " <> 0",
              new String[]{pCategoryId},
              AnsweredQuestionsTable.COLUMN_NUMBER_ANSWERS,
              null,
              null);
    }
    Log.d(TAG, "categoryid: " + pCategoryId + ", cursor == null: " + (cursor == null || !cursor.moveToFirst()));
    return (cursor != null && cursor.moveToFirst()) ? Statistics.fromCursor(cursor) : null;
  }

  /**
   * resets the statistic of the given collection
   *
   * @param pCurrentQuesitonCategory
   */
  public void resetCollection(QuestionCollection pCurrentQuesitonCategory) {
    if (pCurrentQuesitonCategory != null) {
      getWritableDatabase().delete(AnsweredQuestionsTable.TABLE_NAME, AnsweredQuestionsTable.COLUMN_CATEGORY + " =?",
              new String[]{pCurrentQuesitonCategory.getId()});
    }
  }
  /**
   * adds the given collection to db
   *
   * @param pCollection
   * @param pCorrect
   */
  public void addAnsweredCollection(QuestionCollection pCollection, boolean pCorrect) {
    Log.v(TAG, "addAnsweredCollection(" + pCollection.getId() + ")");
    ContentValues content = new ContentValues();
    content.put(AnsweredQuestionCollectionsTable.COLUMN_ID, pCollection.getId());
    content.put(AnsweredQuestionCollectionsTable.COLUMN_CORRECT, pCorrect ? 1 : 0);
    SQLiteDatabase db = getWritableDatabase();
    if (db.update(AnsweredQuestionCollectionsTable.TABLE_NAME, content, AnsweredQuestionCollectionsTable.COLUMN_ID + " =?",
            new String[]{pCollection.getId()}) < 1) {
      db.insert(AnsweredQuestionCollectionsTable.TABLE_NAME, null, content);
    }
  }
  /**
   * returns if the given collection was answered correctly
   * @return
   * @param pCollection
   */
  public boolean isCollectionAnswered(QuestionCollection pCollection) {
    Cursor cursor = getReadableDatabase().query(AnsweredQuestionCollectionsTable.TABLE_NAME,
            null, AnsweredQuestionCollectionsTable.COLUMN_ID + " =?",
            new String[]{pCollection.getId()}, null, null, null);
    if (cursor != null && cursor.moveToFirst() && cursor.getColumnCount() > 0) {
      return cursor.getInt(cursor.getColumnIndex(AnsweredQuestionCollectionsTable.COLUMN_CORRECT)) == 1;
    }
    return false;
  }

}
