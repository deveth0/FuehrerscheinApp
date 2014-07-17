//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import de.dev.eth0.fuehrerschein.Constants;
import de.dev.eth0.fuehrerschein.FuehrerscheinApplication;
import de.dev.eth0.fuehrerschein.R;
import de.schildbach.CrashReporter;
import de.schildbach.ReportIssueDialogBuilder;
import java.io.IOException;

/**
 * Base-class for all activities
 */
public class BaseActivity extends ActionBarActivity {

  private static final String TAG = BaseActivity.class.getName();

  private FuehrerscheinApplication mApplication;

  @Override
  protected void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    if (pSavedInstanceState == null) {
      checkAlerts();
    }
    mApplication = (FuehrerscheinApplication)getApplication();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem pItem) {
    int id = pItem.getItemId();
    switch (id) {
      case android.R.id.home:
        this.finish();
        return true;
    }
    return super.onOptionsItemSelected(pItem);
  }

  /**
   * @return the application
   */
  public FuehrerscheinApplication getFuehrerscheinApplication() {
    return mApplication;
  }

  private void checkAlerts() {
    Log.d(TAG, "Check alerts");
    if (CrashReporter.hasSavedCrashTrace()) {
      final StringBuilder stackTrace = new StringBuilder();

      try {
        CrashReporter.appendSavedCrashTrace(stackTrace);
      }
      catch (final IOException x) {
        Log.i(TAG, "problem appending crash info", x);
      }

      final ReportIssueDialogBuilder dialog = new ReportIssueDialogBuilder(this, R.string.report_issue_dialog_title_crash,
              R.string.report_issue_dialog_message_crash) {
                @Override
        protected CharSequence subject() {
          try {
            if (CrashReporter.hasReason()) {
              return CrashReporter.getReason();
            }
          }
          catch (IOException ioe) {
            Log.w(TAG, "Exception", ioe);
          }
          return Constants.REPORT_SUBJECT_CRASH + " " + getFuehrerscheinApplication().getPackageInfo().versionName;
        }

                @Override
                protected CharSequence collectApplicationInfo() throws IOException {
                  final StringBuilder applicationInfo = new StringBuilder();
                  CrashReporter.appendApplicationInfo(applicationInfo, getFuehrerscheinApplication());
                  return applicationInfo;
                }

                @Override
                protected CharSequence collectStackTrace() throws IOException {
                  if (stackTrace.length() > 0) {
                    return stackTrace;
                  }
                  else {
                    return null;
                  }
                }

                @Override
                protected CharSequence collectDeviceInfo() throws IOException {
                  final StringBuilder deviceInfo = new StringBuilder();
                  CrashReporter.appendDeviceInfo(deviceInfo, BaseActivity.this);
                  return deviceInfo;
                }

              };

      AlertDialog alert = dialog.show();
      alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.font_size_normal));
      alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.font_size_normal));
    }
  }

}
