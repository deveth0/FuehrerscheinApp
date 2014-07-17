//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.test.ui;

import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;
import de.dev.eth0.fuehrerschein.R;
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
    Assert.assertTrue(solo.searchText("Basisfragen"));
    Assert.assertTrue(solo.searchText("Spezifische Fragen Binnen"));
    Assert.assertTrue(solo.searchText("Spezifische Fragen Segeln"));
    Assert.assertTrue(solo.searchText("SBF-Binnen Test"));
    Assert.assertTrue(solo.searchText("0 von 3 Fragen abgeschlossen"));
    Assert.assertTrue(solo.searchText(solo.getString(R.string.start_test_headline)));
  }

}
