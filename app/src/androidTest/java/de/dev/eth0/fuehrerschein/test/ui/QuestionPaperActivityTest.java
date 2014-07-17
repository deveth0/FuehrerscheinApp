//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.test.ui;

import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.RadioButton;
import com.robotium.solo.Solo;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.ui.QuestionActivity;
import de.dev.eth0.fuehrerschein.ui.QuestionPaperActivity;
import de.dev.eth0.fuehrerschein.ui.QuestionPaperResultActivity;
import de.dev.eth0.fuehrerschein.ui.QuestionPaperSelectionActivity;
import de.dev.eth0.fuehrerschein.ui.StartScreenActivity;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;

/**
 * Tests a run of a questionpaper
 *
 */
public class QuestionPaperActivityTest extends ActivityInstrumentationTestCase2<StartScreenActivity> {

  private Solo solo;

  /**
   *
   */
  public QuestionPaperActivityTest() {
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

  /**
   * tests some basic stuff and the navigation inside questionpapers
   */
  public void testSelectQuestionPaperAndNavigateBack() {
    try {
      solo.assertCurrentActivity("wrong activity", StartScreenActivity.class);
      solo.clickOnText(solo.getString(R.string.start_test_select_questionpaper));
      solo.assertCurrentActivity("This should be the QuestionPaperSelectionActivity", QuestionPaperSelectionActivity.class);
      // check question view
      Assert.assertTrue(solo.searchText("Test"));
      solo.clickOnText("Test");
      Assert.assertTrue(solo.searchText("Test Fragebogen 1"));
      solo.clickOnText("Test Fragebogen 1");
      solo.assertCurrentActivity("This should be the QuestionPaperActivity", QuestionPaperActivity.class);
      solo.clickOnButton(solo.getString(R.string.question_paper_button_text));
      solo.assertCurrentActivity("This should be the first question", QuestionActivity.class);
      View button = solo.getView(R.id.question_button_next);
      Assert.assertFalse(button.isEnabled());
      Assert.assertTrue(solo.searchButton(solo.getString(R.string.question_next)));
      RadioButton rb = (RadioButton)solo.getView(R.id.question_answer_1);
      solo.clickOnView(rb);
      // wait for button be enabled
      solo.sleep(500);
      Assert.assertTrue(button.isEnabled());

      // open next question
      solo.clickOnButton(solo.getString(R.string.question_next));
      Assert.assertTrue(solo.searchText("Frage Nr. t2"));

      // go back to home via actionbar button
      if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
        solo.clickOnActionBarHomeButton();
      }
      else {
        solo.goBack();
      }
      solo.sleep(500);
      solo.assertCurrentActivity("We should be back on the questionpaper screen now", QuestionPaperActivity.class);
      // it should start from the first question again
      solo.clickOnButton(solo.getString(R.string.question_paper_button_text));
      Assert.assertTrue(solo.searchText("Frage Nr. t1"));
    }
    catch (AssertionFailedError afe) {
      solo.takeScreenshot();
      throw afe;
    }
  }

  /**
   * Tests to answer the test-questions and display the Questionpaperresult view
   */
  public void testGetQuestionPaperResults() {
    try {
      solo.assertCurrentActivity("wrong activity", StartScreenActivity.class);
      // navigate to first question
      solo.clickOnText(solo.getString(R.string.start_test_select_questionpaper));
      solo.assertCurrentActivity("This should be the QuestionPaperSelectionActivity", QuestionPaperSelectionActivity.class);
      Assert.assertTrue(solo.searchText("Test"));
      solo.clickOnText("Test");
      Assert.assertTrue(solo.searchText("Test Fragebogen 1"));
      solo.clickOnText("Test Fragebogen 1");
      solo.assertCurrentActivity("This should be the QuestionPaperActivity", QuestionPaperActivity.class);
      solo.clickOnButton(solo.getString(R.string.question_paper_button_text));
      for (int i = 0; i < 3; i++) {
        solo.assertCurrentActivity("This should be the first question", QuestionActivity.class);
        Assert.assertTrue(solo.searchText("Frage Nr. t" + (i + 1)));
        RadioButton rb = (RadioButton)solo.getView(R.id.question_answer_1);
        solo.clickOnView(rb);
        solo.sleep(500);
        solo.clickOnButton(solo.getString(R.string.question_next));
      }

      solo.assertCurrentActivity("This should be the result activity", QuestionPaperResultActivity.class);

      Assert.assertTrue(solo.searchText("Frage Nr. t1"));
      Assert.assertTrue(solo.searchText("Frage Nr. t2"));
      Assert.assertTrue(solo.searchText("Frage Nr. t3"));
      Assert.assertTrue(solo.searchButton(solo.getString(R.string.finish)));
      // TODO: test click on question
      solo.clickOnButton(solo.getString(R.string.finish));
    }
    catch (AssertionFailedError afe) {
      solo.takeScreenshot();
      throw afe;
    }
  }

}
