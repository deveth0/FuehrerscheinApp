//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import de.dev.eth0.fuehrerschein.R;
import de.dev.eth0.fuehrerschein.data.model.Question;
import de.dev.eth0.fuehrerschein.ui.QuestionPaperResultActivity;

/**
 * Questionpaper result builder
 */
public class QuestionPaperResultDialogBuilder extends AlertDialog.Builder {

  /**
   *
   * @param pContext
   * @param pQuestion
   * @param pResult
   */
  public QuestionPaperResultDialogBuilder(Context pContext, Question pQuestion, QuestionPaperResultActivity.QuestionPaperResult pResult) {
    super(pContext);
    LayoutInflater inflater = LayoutInflater.from(pContext);
    View view = inflater.inflate(R.layout.dialog_question_paper_result, null);
    if (pQuestion.getImg() != null && !pQuestion.getImg().isEmpty()) {
      ImageView questionsImage = (ImageView)view.findViewById(R.id.question_image);
      questionsImage.setImageBitmap(
              scaleImage(pContext.getResources(), pContext.getResources().getIdentifier(pQuestion.getImg().get(0), "drawable", pContext.getPackageName())));
      questionsImage.setVisibility(View.VISIBLE);
      if (pQuestion.getImg().size() > 1) {
        questionsImage = (ImageView)view.findViewById(R.id.question_image_2);
        questionsImage.setImageBitmap(
                scaleImage(pContext.getResources(), pContext.getResources().getIdentifier(pQuestion.getImg().get(1), "drawable", pContext.getPackageName())));
        questionsImage.setVisibility(View.VISIBLE);
      }
    }
    else {
      view.findViewById(R.id.question_image_wrapper).setVisibility(View.GONE);
    }
    ((TextView)view.findViewById(R.id.dialog_question_paper_result_question_text)).setText(pQuestion.getText());
    ((TextView)view.findViewById(R.id.dialog_question_paper_result_correct_answer_text)).setText(pQuestion.getCorrectAnswer().getText());
    TextView chosenAnswer = (TextView)view.findViewById(R.id.dialog_question_paper_result_chosen_answer_text);
    if (pResult.isCorrect()) {
      chosenAnswer.setVisibility(View.GONE);
      ((TextView)view.findViewById(R.id.dialog_question_paper_result_chosen_answer_headline)).setVisibility(View.GONE);
    }
    else {
      chosenAnswer.setText(pResult.getAnswerText());
    }

    setInverseBackgroundForced(true);
    setTitle(pContext.getString(R.string.question_info, pQuestion.getId()));
    setView(view);
    setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface pDialog, int pWhich) {
        // just disappear...
      }
    });
  }

  private Bitmap scaleBitmap(Bitmap bitmap) {
    int nh = (int)(bitmap.getHeight() * (512.0 / bitmap.getWidth()));
    return Bitmap.createScaledBitmap(bitmap, 512, nh, true);
  }

  private Bitmap scaleImage(Resources resources, int identifier) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
