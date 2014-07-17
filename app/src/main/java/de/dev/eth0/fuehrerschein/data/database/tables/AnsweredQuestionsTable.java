//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * the answered questions table
 */
public final class AnsweredQuestionsTable extends AbstrastDatabaseTable {

  private static final String TAG = AnsweredQuestionsTable.class.getName();

  /**
   * table name
   */
  public static final String TABLE_NAME = "answeredQuestions";
  /**
   * column for category
   */
  public static final String COLUMN_CATEGORY = "category";
  /**
   * column for number of answered questions
   */
  public static final String COLUMN_NUMBER_ANSWERS = "numberAnswers";

  private static final String DATABASE_CREATE = "create table if not exists "
          + TABLE_NAME
          + "("
          + COLUMN_ID + " text primary key, "
          + COLUMN_CATEGORY + " text not null, "
          + COLUMN_NUMBER_ANSWERS + " integer default 0"
          + ");";

  /**
   *
   * @param pDatabase
   */
  public static void onCreate(SQLiteDatabase pDatabase) {
    Log.i(TAG, "Creating database.");
    pDatabase.execSQL(DATABASE_CREATE);
  }

  /**
   * @param pDatabase
   * @param pNewVersion
   * @param pOldVersion
   */
  public static void onUpgrade(SQLiteDatabase pDatabase, int pOldVersion, int pNewVersion) {
    // ensure database exists
    Log.i(TAG, "onUpgrade");
    pDatabase.execSQL(DATABASE_CREATE);
    // do nothing
  }

}
