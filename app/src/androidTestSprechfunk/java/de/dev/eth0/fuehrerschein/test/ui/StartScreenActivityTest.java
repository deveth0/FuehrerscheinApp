//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.test.ui;

import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.ui.AboutActivity;
import de.dev.eth0.fuehrerschein.ui.StartScreenActivity;
import junit.framework.Assert;

/**
 *
 */
public class StartScreenActivityTest extends ActivityInstrumentationTestCase2<StartScreenActivity> {

  private Solo solo;

  /**
   *
   */
  public StartScreenActivityTest() {
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

  public void testQuestionsParsed() {
    solo.assertCurrentActivity("wrong activity", StartScreenActivity.class);
    Assert.assertTrue(solo.searchText(solo.getString(R.string.start_categories_headline)));
    Assert.assertTrue(solo.searchText("1. Binnenschiffahrtsfunk"));
    Assert.assertTrue(solo.searchText("2. Funkeinrichtungen und Schiffsfunkstellen"));
    Assert.assertTrue(solo.searchText("3. Verkehrskreise"));
    Assert.assertTrue(solo.searchText("4. Sprechfunk"));
    Assert.assertTrue(solo.searchText("5. Betriebsverfahren und Rangfolgen"));
    Assert.assertTrue(solo.searchText("SBF-Binnen Test"));
    Assert.assertTrue(solo.searchText("0 von 3 Fragen abgeschlossen"));
    Assert.assertTrue(solo.searchText(solo.getString(R.string.start_test_headline)));
  }

}
