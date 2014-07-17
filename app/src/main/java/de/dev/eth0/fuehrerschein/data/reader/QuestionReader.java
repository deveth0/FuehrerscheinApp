//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.data.reader;

import android.content.Context;
import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Reader for Questions.json
 */
public class QuestionReader {

  private static final String TAG = QuestionReader.class.getName();
  private final Context mContext;

  /**
   *
   * @param pContext
   */
  public QuestionReader(Context pContext) {
    this.mContext = pContext;
  }

  /**
   * Loads the given inputstream as json parsable
   *
   * @param <T>
   * @param pInputStream
   * @param pClass
   * @return
   */
  public <T> T parse(InputStream pInputStream, Class<T> pClass) {
    ObjectMapper mapper = new ObjectMapper();
    Reader reader = new InputStreamReader(pInputStream);
    try {
      return mapper.readValue(reader, pClass);
    }
    catch (IOException ex) {
      Log.w(TAG, "Could not read category from inputstream", ex);
    }
    return null;
  }

}
