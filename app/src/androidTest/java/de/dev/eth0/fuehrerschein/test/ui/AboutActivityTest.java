//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.test.ui;

import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;
import de.dev.eth0.fuehrerschein.ui.AboutActivity;
import de.dev.eth0.fuehrerschein.ui.StartScreenActivity;
import junit.framework.Assert;

/**
 *
 */
public class AboutActivityTest extends ActivityInstrumentationTestCase2<StartScreenActivity> {

  private Solo solo;

  /**
   *
   */
  public AboutActivityTest() {
    super(StartScreenActivity.class);
  }

  @Override
  public void setUp() throws Exception {
    solo = new Solo(getInstrumentation(), getActivity());
  }

  @Override
  public void tearDown() throws Exception {
    solo.finishOpenedActivities();
  }

  public void testAbout() {
    try {
      solo.assertCurrentActivity("This should be the StartScreenAcitivity", StartScreenActivity.class);
      solo.pressMenuItem(0);
      //solo.clickOnActionBarItem(R.id.menu_start_screen_about);
      //solo.sendKey(Solo.MENU);
      Assert.assertTrue(solo.searchText("Über"));
      solo.clickOnText("Über");

      solo.assertCurrentActivity("This should be the AboutActivity", AboutActivity.class);
      Assert.assertTrue(solo.searchText("Version"));
      Assert.assertTrue(solo.searchText("Entwickelt von"));
      Assert.assertTrue(solo.searchText("Alexander Muthmann"));
      if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
        solo.clickOnActionBarHomeButton();
      }
      else {
        solo.goBack();
      }
      solo.sleep(500);
      solo.assertCurrentActivity("Should be back on start", StartScreenActivity.class);
    }
    catch (AssertionError ae) {
      solo.takeScreenshot("assertionError");
      throw ae;
    }
  }
}
