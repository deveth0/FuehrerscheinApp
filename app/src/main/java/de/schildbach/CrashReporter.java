//$URL$
//$Id$
package de.schildbach;
/*
 * Copyright 2011-2014 the original author or authors.
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

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import de.dev.eth0.fuehrerschein.FuehrerscheinApplication;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;

/**
 * @author Andreas Schildbach
 */
public class CrashReporter {

  private static final String TAG = CrashReporter.class.getName();

  private static final String BACKGROUND_TRACES_FILENAME = "background.trace";
  private static final String CRASH_TRACE_FILENAME = "crash.trace";
  private static final String CRASH_REASON_FILENAME = "crashreason.log";

  private static final long TIME_CREATE_APPLICATION = System.currentTimeMillis();

  private static File backgroundTracesFile;
  private static File crashTraceFile;
  private static File reasonFile;

  public static void init(final File cacheDir) {
    backgroundTracesFile = new File(cacheDir, BACKGROUND_TRACES_FILENAME);
    crashTraceFile = new File(cacheDir, CRASH_TRACE_FILENAME);
    reasonFile = new File(cacheDir, CRASH_REASON_FILENAME);
    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(Thread.getDefaultUncaughtExceptionHandler()));
  }

  public static boolean hasSavedBackgroundTraces() {
    return backgroundTracesFile.exists();
  }

  public static void appendSavedBackgroundTraces(final Appendable report) throws IOException {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(backgroundTracesFile), Charset.forName("UTF-8")));
      copy(reader, report);
    }
    finally {
      if (reader != null) {
        reader.close();
      }

      backgroundTracesFile.delete();
    }
  }

  public static boolean hasReason() {
    return reasonFile.exists();
  }

  public static String getReason() throws IOException {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(reasonFile), Charset.forName("UTF-8")));
      return reader.readLine();
    }
    finally {
      if (reader != null) {
        reader.close();
      }
      reasonFile.delete();
    }
  }


  public static boolean hasSavedCrashTrace() {
    return crashTraceFile.exists();
  }

  public static void appendSavedCrashTrace(final Appendable report) throws IOException {
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(crashTraceFile), Charset.forName("UTF-8")));
      copy(reader, report);
    }
    finally {
      if (reader != null) {
        reader.close();
      }

      crashTraceFile.delete();
    }
  }

  private static void copy(final BufferedReader in, final Appendable out) throws IOException {
    while (true) {
      final String line = in.readLine();
      if (line == null) {
        break;
      }

      out.append(line).append('\n');
    }
  }

  public static void appendDeviceInfo(final Appendable report, final Context context) throws IOException {
    final Resources res = context.getResources();
    final Configuration config = res.getConfiguration();
    final ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

    report.append("Device Model: " + android.os.Build.MODEL + "\n");
    report.append("Android Version: " + android.os.Build.VERSION.RELEASE + "\n");
    report.append("Board: " + android.os.Build.BOARD + "\n");
    report.append("Brand: " + android.os.Build.BRAND + "\n");
    report.append("Device: " + android.os.Build.DEVICE + "\n");
    report.append("Display: " + android.os.Build.DISPLAY + "\n");
    report.append("Finger Print: " + android.os.Build.FINGERPRINT + "\n");
    report.append("Host: " + android.os.Build.HOST + "\n");
    report.append("ID: " + android.os.Build.ID + "\n");
    // report.append("Manufacturer: " + manufacturer + "\n");
    report.append("Product: " + android.os.Build.PRODUCT + "\n");
    report.append("Tags: " + android.os.Build.TAGS + "\n");
    report.append("Time: " + android.os.Build.TIME + "\n");
    report.append("Type: " + android.os.Build.TYPE + "\n");
    report.append("User: " + android.os.Build.USER + "\n");
    report.append("Configuration: " + config + "\n");
    report.append("Screen Layout: size " + (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) + " long "
            + (config.screenLayout & Configuration.SCREENLAYOUT_LONG_MASK) + "\n");
    report.append("Display Metrics: " + res.getDisplayMetrics() + "\n");
    report.append("Memory Class: " + activityManager.getMemoryClass()
            + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? "/" + largeMemoryClass(activityManager) : "") + "\n");
  }

  private static int largeMemoryClass(final ActivityManager activityManager) {
    try {
      return (Integer)ActivityManager.class.getMethod("getLargeMemoryClass").invoke(activityManager);
    }
    catch (final IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException x) {
      throw new RuntimeException(x);
    }
  }

  public static void appendInstalledPackages(final Appendable report, final Context context) throws IOException {
    final PackageManager pm = context.getPackageManager();
    final List<PackageInfo> installedPackages = pm.getInstalledPackages(0);

    // sort by package name
    Collections.sort(installedPackages, new Comparator<PackageInfo>() {
      @Override
      public int compare(final PackageInfo lhs, final PackageInfo rhs) {
        return lhs.packageName.compareTo(rhs.packageName);
      }
    });

    for (final PackageInfo p : installedPackages) {
      report.append(String.format("%s %s (%d) - %tF %tF\n", p.packageName, p.versionName, p.versionCode, p.firstInstallTime, p.lastUpdateTime));
    }
  }

  public static void appendApplicationInfo(final Appendable report, final FuehrerscheinApplication application) throws IOException {
    try {
      final PackageManager pm = application.getPackageManager();
      final PackageInfo pi = pm.getPackageInfo(application.getPackageName(), 0);
      final long now = System.currentTimeMillis();

      report.append("Affects Version: " + pi.versionName + "\n");
      report.append("App Version: " + pi.versionCode + "\n");
      report.append("Package: " + pi.packageName + "\n");
      report.append("Time: " + String.format("%tF %tT %tz", now, now, now) + "\n");
      report.append("Time of launch: "
              + String.format("%tF %tT %tz", TIME_CREATE_APPLICATION, TIME_CREATE_APPLICATION, TIME_CREATE_APPLICATION) + "\n");
      report.append("Time of last update: " + String.format("%tF %tT %tz", pi.lastUpdateTime, pi.lastUpdateTime, pi.lastUpdateTime) + "\n");
      report.append("Time of first install: " + String.format("%tF %tT %tz", pi.firstInstallTime, pi.firstInstallTime, pi.firstInstallTime)
              + "\n");

      report.append("Databases:");
      for (final String db : application.databaseList()) {
        report.append(" " + db);
      }
      report.append("\n");

      final File filesDir = application.getFilesDir();
      report.append("\nContents of FilesDir " + filesDir + ":\n");
      appendDir(report, filesDir, 0);
      final File logDir = application.getDir("log", Context.MODE_PRIVATE);
      report.append("\nContents of LogDir " + logDir + ":\n");
      appendDir(report, logDir, 0);
    }
    catch (final NameNotFoundException x) {
      throw new IOException(x);
    }
  }

  private static void appendDir(final Appendable report, final File file, final int indent) throws IOException {
    for (int i = 0; i < indent; i++) {
      report.append("  - ");
    }

    final Formatter formatter = new Formatter(report);
    formatter.format("%tF %tT %8d  %s\n", file.lastModified(), file.lastModified(), file.length(), file.getName());
    formatter.close();

    if (file.isDirectory()) {
      for (final File f : file.listFiles()) {
        appendDir(report, f, indent + 1);
      }
    }
  }

  public static void saveBackgroundTrace(final Throwable throwable, final PackageInfo packageInfo) {
    synchronized (backgroundTracesFile) {
      PrintWriter writer = null;
      try {
        writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(backgroundTracesFile, true), Charset.forName("UTF-8")));

        final long now = System.currentTimeMillis();
        writer.println(String.format("\n--- collected at %tF %tT %tz on version %s (%d)", now, now, now, packageInfo.versionName,
                packageInfo.versionCode));
        appendTrace(writer, throwable);
      }
      catch (final FileNotFoundException x) {
        Log.e(TAG, "problem writing background trace", x);
      }
      finally {
        if (writer != null) {
          writer.close();
        }
      }
    }
  }

  private static void appendTrace(final PrintWriter writer, final Throwable throwable) {
    throwable.printStackTrace(writer);
    // If the exception was thrown in a background thread inside
    // AsyncTask, then the actual exception can be found with getCause
    Throwable cause = throwable.getCause();
    while (cause != null) {
      writer.println("\nCause:\n");
      cause.printStackTrace(writer);
      cause = cause.getCause();
    }
  }

  private static class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Thread.UncaughtExceptionHandler previousHandler;

    public ExceptionHandler(final Thread.UncaughtExceptionHandler previousHandler) {
      this.previousHandler = previousHandler;
    }

    @Override
    public synchronized void uncaughtException(final Thread t, final Throwable exception) {
      try {
        saveCrashTrace(exception);
        if (exception.getStackTrace() != null && exception.getStackTrace().length > 0 && exception.getStackTrace()[0] != null) {
          saveReason(exception.getStackTrace()[0].toString());
        }
        else {
          saveReason(exception.getLocalizedMessage());
        }
      }
      catch (final IOException x) {
        Log.i(TAG, "problem writing crash trace", x);
      }

      previousHandler.uncaughtException(t, exception);
    }

    private void saveReason(final String reason) throws IOException {
      final PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(reasonFile), Charset.forName("UTF-8")));
      writer.append(reason);
      writer.close();
    }


    private void saveCrashTrace(final Throwable throwable) throws IOException {
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(crashTraceFile), Charset.forName("UTF-8")));
      appendTrace(writer, throwable);
      writer.close();
    }
  }
}
