//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * the answered questionpapers table
 */
public final class AnsweredQuestionCollectionsTable extends AbstrastDatabaseTable {

  private static final String TAG = AnsweredQuestionCollectionsTable.class.getName();

  /**
   * table name
   */
  public static final String TABLE_NAME = "answeredQuestionCollection";

  /**
   * was the answer correct?
   */
  public static final String COLUMN_CORRECT = "correct";

  private static final String DATABASE_CREATE = "create table if not exists "
          + TABLE_NAME
          + "("
          + COLUMN_ID + " text primary key,"
          + COLUMN_CORRECT + " integer default 0"
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
