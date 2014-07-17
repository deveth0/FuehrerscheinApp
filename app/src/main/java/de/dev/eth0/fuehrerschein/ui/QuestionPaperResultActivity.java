//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import de.dev.eth0.fuehrerschein.Constants;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.ui.fragments.QuestionPaperResultListFragment;

/**
 * Activity to display results a selected question paper
 *
 */
public class QuestionPaperResultActivity extends BaseActivity {
  private static final String TAG = QuestionPaperResultActivity.class.getName();
  @Override
  protected void onCreate(Bundle pSavedInstanceState) {
    super.onCreate(pSavedInstanceState);
    // if there are no results, go back to start
    if (!getIntent().hasExtra(Constants.EXTRA_EXAM_RESULT)) {
      startActivity(new Intent(this, StartScreenActivity.class));
      finish();
      return;
    }
    setContentView(R.layout.activity_question_paper_result);
    getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, new QuestionPaperResultListFragment())
            .commit();
  }

  /**
   * Class to hold a questionpaper result
   */
  public static class QuestionPaperResult implements Parcelable {

    private final String mQuestionId;
    private final String mAnswerText;
    private final boolean mIsCorrect;

    /**
     *
     */
    public static final Parcelable.Creator<QuestionPaperResult> CREATOR
            = new Parcelable.Creator<QuestionPaperResult>() {
              @Override
      public QuestionPaperResult createFromParcel(Parcel pParcel) {
        return new QuestionPaperResult(pParcel);
              }

              @Override
      public QuestionPaperResult[] newArray(int pSize) {
        return new QuestionPaperResult[pSize];
              }

            };

    /**
     * @param pAnswerText
     * @param pIsCorrect
     * @param pQuestionId
     */
    public QuestionPaperResult(String pQuestionId, String pAnswerText, boolean pIsCorrect) {
      this.mQuestionId = pQuestionId;
      this.mAnswerText = pAnswerText;
      this.mIsCorrect = pIsCorrect;
    }

    /**
     *
     * @param pParcel
     */
    private QuestionPaperResult(Parcel pParcel) {
      this.mQuestionId = pParcel.readString();
      this.mAnswerText = pParcel.readString();
      this.mIsCorrect = pParcel.readInt() == 1;
    }

    /**
     *
     * @return
     */
    public String getQuestionId() {
      return mQuestionId;
    }

    /**
     * @return
     */
    public String getAnswerText() {
      return mAnswerText;
    }

    /**
     *
     * @return
     */
    public boolean isCorrect() {
      return mIsCorrect;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel pParcel, int pFlags) {
      pParcel.writeString(mQuestionId);
      pParcel.writeString(mAnswerText);
      pParcel.writeInt(mIsCorrect ? 1 : 0);
    }

  }
}
