//$URL$
//$Id$
package de.dev.eth0.fuehrerschein;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import de.dev.eth0.fuehrerschein.data.QuestionManager;
import de.dev.eth0.fuehrerschein.data.database.DatabaseHelper;
import de.schildbach.CrashReporter;

/**
 * the Application class
 */
public class FuehrerscheinApplication extends Application {

  private static final String TAG = FuehrerscheinApplication.class.getName();

  private QuestionManager mQuestionManager;
  private ExamMode mExamMode = ExamMode.TRAINING;
  private DatabaseHelper mDatabaseHelper;
  private PackageInfo mPackageInfo;

  @Override
  public void onCreate() {
    super.onCreate();
    try {
      mPackageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
      CrashReporter.init(getCacheDir());
      new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(final Thread pThread, final Throwable pThrowable) {
          Log.i(TAG, "uncaught exception", pThrowable);
          CrashReporter.saveBackgroundTrace(pThrowable, mPackageInfo);
        }
      };
    }
    catch (final NameNotFoundException x) {
      throw new RuntimeException(x);
    }
  }


  /**
   *
   * @return database helper with lazy init
   */
  public DatabaseHelper getDatabaseHelper() {
    if (mDatabaseHelper == null) {
      mDatabaseHelper = new DatabaseHelper(this);
    }
    return mDatabaseHelper;
  }

  /**
   *
   * @return questionmanager with lazy init
   */
  public QuestionManager getQuestionManager() {
    if (mQuestionManager == null) {
      mQuestionManager = new QuestionManager(this);
    }
    return mQuestionManager;
  }

  /**
   * @return the current exam mode
   */
  public ExamMode getExamMode() {
    return mExamMode;
  }

  /**
   * sets the exam mode
   *
   * @param pExamMode
   */
  public void setExamMode(ExamMode pExamMode) {
    this.mExamMode = pExamMode;
  }


  /**
   * @return the application version name
   */
  public String applicationVersionName() {
    return getPackageInfo().versionName;
  }

  /**
   *
   * @return application version code
   */
  public int applicationVersionCode() {
    return getPackageInfo().versionCode;
  }

  /**
   *
   * @return
   */
  public PackageInfo getPackageInfo() {
    return mPackageInfo;
  }


}
