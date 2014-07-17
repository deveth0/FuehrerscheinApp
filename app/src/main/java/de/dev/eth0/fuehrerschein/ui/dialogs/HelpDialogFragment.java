//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.webkit.WebView;

/**
 * Help dialog fragment
 */
public final class HelpDialogFragment extends DialogFragment {

  private static final String FRAGMENT_TAG = HelpDialogFragment.class.getName();
  private static final String KEY_PAGE = "page";
  private Activity mActivity;

  public static void showHelpDialog(final FragmentManager pFragmentManager, final String pPageName) {
    HelpDialogFragment help = new HelpDialogFragment();
    Bundle bundle = new Bundle();
    bundle.putString(KEY_PAGE, pPageName);
    help.setArguments(bundle);
    help.show(pFragmentManager, FRAGMENT_TAG);
  }

  @Override
  public void onAttach(final Activity pActivity) {
    super.onAttach(pActivity);
    mActivity = pActivity;
  }

  @Override
  public Dialog onCreateDialog(Bundle pSavedInstanceState) {
    String page = getArguments().getString(KEY_PAGE);

    WebView webView = new WebView(mActivity);
    webView.loadUrl("file:///android_asset/" + page + ".html");

    Dialog dialog = new Dialog(mActivity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(webView);
    dialog.setCanceledOnTouchOutside(true);

    return dialog;
  }
}
