//$URL$
//$Id$
package de.schildbach;

/*
 * Copyright 2013-2014 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import de.dev.eth0.fuehrerschein.Constants;
import de.dev.eth0.fuehrerschein.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Andreas Schildbach
 */
public abstract class ReportIssueDialogBuilder extends AlertDialog.Builder implements OnClickListener {

  private static final String TAG = ReportIssueDialogBuilder.class.getName();
  private final Context context;

  private final EditText viewDescription;
  private final CheckBox viewCollectDeviceInfo;
  private final CheckBox viewCollectApplicationLog;


  public ReportIssueDialogBuilder(final Context context, final int titleResId, final int messageResId) {
    super(context);

    this.context = context;

    final LayoutInflater inflater = LayoutInflater.from(context);
    final View view = inflater.inflate(R.layout.dialog_report_issue, null);

    ((TextView)view.findViewById(R.id.report_issue_dialog_message)).setText(messageResId);

    viewDescription = (EditText)view.findViewById(R.id.report_issue_dialog_description);

    viewCollectDeviceInfo = (CheckBox)view.findViewById(R.id.report_issue_dialog_collect_device_info);
    viewCollectApplicationLog = (CheckBox)view.findViewById(R.id.report_issue_dialog_collect_application_log);

    setInverseBackgroundForced(true);
    setTitle(titleResId);
    setView(view);
    setPositiveButton(R.string.report_issue_dialog_report, this);
    setNegativeButton(R.string.report_issue_dialog_cancel, null);
  }

  @Override
  public void onClick(final DialogInterface dialog, final int which) {
    final StringBuilder text = new StringBuilder();
    final ArrayList<Uri> attachments = new ArrayList<>();
    final File cacheDir = context.getCacheDir();

    text.append(viewDescription.getText()).append('\n');

    try {
      text.append("\n\n\n=== application info ===\n\n");

      final CharSequence applicationInfo = collectApplicationInfo();

      text.append(applicationInfo);
    }
    catch (final IOException x) {
      text.append(x.toString()).append('\n');
    }

    try {
      final CharSequence stackTrace = collectStackTrace();

      if (stackTrace != null) {
        text.append("\n\n\n=== stack trace ===\n\n");
        text.append(stackTrace);
      }
    }
    catch (final IOException x) {
      text.append("\n\n\n=== stack trace ===\n\n");
      text.append(x.toString()).append('\n');
    }

    if (viewCollectDeviceInfo.isChecked()) {
      try {
        text.append("\n\n\n=== device info ===\n\n");

        final CharSequence deviceInfo = collectDeviceInfo();

        text.append(deviceInfo);
      }
      catch (final IOException x) {
        text.append(x.toString()).append('\n');
      }
    }

    if (viewCollectApplicationLog.isChecked()) {
      try {
        final File logDir = context.getDir("log", Context.MODE_PRIVATE);

        for (final File logFile : logDir.listFiles()) {
          final String logFileName = logFile.getName();
          final File file;
          if (logFileName.endsWith(".log.gz")) {
            file = File.createTempFile(logFileName.substring(0, logFileName.length() - 6), ".log.gz", cacheDir);
          }
          else if (logFileName.endsWith(".log")) {
            file = File.createTempFile(logFileName.substring(0, logFileName.length() - 3), ".log", cacheDir);
          }
          else {
            continue;
          }

          final InputStream is = new FileInputStream(logFile);
          final OutputStream os = new FileOutputStream(file);

          copy(is, os);

          os.close();
          is.close();

          chmod(file, 0777);

          attachments.add(Uri.fromFile(file));
        }
      }
      catch (final IOException x) {
        Log.i(TAG, "problem writing attachment", x);
      }
    }

    if (CrashReporter.hasSavedBackgroundTraces()) {
      text.append("\n\n\n=== saved exceptions ===\n\n");

      try {
        CrashReporter.appendSavedBackgroundTraces(text);
      }
      catch (final IOException x) {
        text.append(x.toString()).append('\n');
      }
    }

    text.append("\n\nPUT ADDITIONAL COMMENTS TO THE TOP. DOWN HERE NOBODY WILL NOTICE.");

    startSend(subject(), text, attachments);
  }

  private void startSend(final CharSequence subject, final CharSequence text, final ArrayList<Uri> attachments) {
    final Intent intent;

    if (attachments.isEmpty()) {
      intent = new Intent(Intent.ACTION_SEND);
      intent.setType("message/rfc822");
    }
    else if (attachments.size() == 1) {
      intent = new Intent(Intent.ACTION_SEND);
      intent.setType("text/plain");
      intent.putExtra(Intent.EXTRA_STREAM, attachments.get(0));
    }
    else {
      intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
      intent.setType("text/plain");
      intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);
    }

    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.REPORT_EMAIL});
    if (subject != null) {
      intent.putExtra(Intent.EXTRA_SUBJECT, subject);
    }
    intent.putExtra(Intent.EXTRA_TEXT, text);

    context.startActivity(Intent.createChooser(intent, context.getString(R.string.report_issue_dialog_mail_intent_chooser)));
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public void chmod(final File path, final int mode) {
    try {
      final Class fileUtils = Class.forName("android.os.FileUtils");
      final Method setPermissions = fileUtils.getMethod("setPermissions", String.class, int.class, int.class, int.class);
      setPermissions.invoke(null, path.getAbsolutePath(), mode, -1, -1);
    }
    catch (final ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException x) {
      Log.e(TAG, "chmod failed", x);
    }
  }

  public static final long copy(final InputStream is, final OutputStream os) throws IOException {
    final byte[] buffer = new byte[1024];
    long count = 0;
    int n = 0;
    while (-1 != (n = is.read(buffer))) {
      os.write(buffer, 0, n);
      count += n;
    }
    return count;
  }
  protected abstract CharSequence subject();

  protected abstract CharSequence collectApplicationInfo() throws IOException;

  protected abstract CharSequence collectStackTrace() throws IOException;

  protected abstract CharSequence collectDeviceInfo() throws IOException;

}
