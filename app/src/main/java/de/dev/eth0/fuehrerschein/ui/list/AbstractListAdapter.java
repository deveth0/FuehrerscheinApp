//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import de.dev.eth0.fuehrerschein.ui.BaseActivity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * List adapter
 *
 * @author amuthmann
 * @param <T>
 */
public abstract class AbstractListAdapter<T> extends BaseAdapter {

  private final BaseActivity mActivity;
  private final LayoutInflater mInflater;
  private final List<T> mEntries = new ArrayList<>();
  private boolean mShowEmptyText = false;

  /**
   *
   * @param pActivity
   */
  public AbstractListAdapter(BaseActivity pActivity) {
    this.mActivity = pActivity;
    mInflater = LayoutInflater.from(pActivity);
  }

  /**
   *
   * @return
   */
  public BaseActivity getActivity() {
    return mActivity;
  }

  /**
   * removes all entries
   */
  public void clear() {
    mEntries.clear();
    notifyDataSetChanged();
  }

  /**
   *
   * @param pEntries
   */
  public void replace(Collection<T> pEntries) {
    this.mEntries.clear();
    this.mEntries.addAll(pEntries);
    mShowEmptyText = true;
    notifyDataSetChanged();
  }

  @Override
  public boolean isEmpty() {
    return mShowEmptyText && super.isEmpty();
  }

  @Override
  public int getCount() {
    return mEntries.size();
  }

  @Override
  public T getItem(int pPosition) {
    return mEntries.get(pPosition);
  }

  @Override
  public long getItemId(int pPosition) {
    return mEntries.get(pPosition).hashCode();
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public View getView(int pPosition, View pRow, ViewGroup pViewGroup) {
    pRow = mInflater.inflate(getRowLayout(pPosition), null);
    T tx = getItem(pPosition);
    bindView(pRow, tx);
    return pRow;
  }

  /**
   * @param pPosition
   * @return the row layout to use
   */
  public abstract int getRowLayout(int pPosition);

  /**
   * Bind the view for the given row
   *
   * @param pRow
   * @param pEntry
   */
  public abstract void bindView(View pRow, T pEntry);
}
