//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import de.dev.eth0.fuehrerschein.R;

/**
 * Image-detail-dialog
 */
public class ImageDetailDialogBuilder extends AlertDialog.Builder {

  public ImageDetailDialogBuilder(Context pContext, int pImgId, int pImgId2) {
    super(pContext);
    LayoutInflater inflater = LayoutInflater.from(pContext);
    View wrapper = inflater.inflate(R.layout.dialog_image_detail, null);
    ImageView imgView = ((ImageView)wrapper.findViewById(R.id.question_image));
    imgView.setImageBitmap(scaleBitmap(BitmapFactory.decodeResource(pContext.getResources(), pImgId)));
    imgView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    imgView.setVisibility(View.VISIBLE);
    if (pImgId2 != 0) {
      imgView = ((ImageView)wrapper.findViewById(R.id.question_image_2));
      imgView.setImageBitmap(scaleBitmap(BitmapFactory.decodeResource(pContext.getResources(), pImgId2)));
      imgView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
      imgView.setVisibility(View.VISIBLE);
    }
    setView(wrapper);
    setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface pDialog, int pWhich) {
        // just disapear...
      }
    });
  }

  private Bitmap scaleBitmap(Bitmap bitmap) {
    int nh = (int)(bitmap.getHeight() * (512.0 / bitmap.getWidth()));
    return Bitmap.createScaledBitmap(bitmap, 512, nh, true);
  }
}
