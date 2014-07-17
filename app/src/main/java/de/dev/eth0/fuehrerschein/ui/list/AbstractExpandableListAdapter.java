//$URL$
//$Id$
package de.dev.eth0.fuehrerschein.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import de.dev.eth0.fuehrerschein.ui.BaseActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Expandable list
 *
 * @param <T> the header type
 * @param <U> the content type
 */
public abstract class AbstractExpandableListAdapter<T, U> extends BaseExpandableListAdapter {

  private final BaseActivity mActivity;
  private final LayoutInflater mInflater;
  private final List<T> mListDataHeader = new ArrayList<>();
  private final Map<T, List<U>> mListData = new HashMap<>();
  private boolean mShowEmptyText = false;
  /**
   *
   * @param pActivity
   */
  public AbstractExpandableListAdapter(BaseActivity pActivity) {
    this.mActivity = pActivity;
    mInflater = LayoutInflater.from(pActivity);
  }

  /**
   *
   */
  public void clear() {
    mListDataHeader.clear();
    mListData.clear();
    notifyDataSetChanged();
  }
  /**
   *
   * @param pEntries
   */
  public void replace(Map<T, List<U>> pEntries) {
    this.mListDataHeader.clear();
    this.mListDataHeader.addAll(pEntries.keySet());
    this.mListData.clear();
    this.mListData.putAll(pEntries);
    mShowEmptyText = true;
    notifyDataSetChanged();
  }

  @Override
  public boolean isEmpty() {
    return mShowEmptyText && super.isEmpty();
  }

  @Override
  public int getChildrenCount(int pGroupPosition) {
    return mListData.get(mListDataHeader.get(pGroupPosition)
    ).size();
  }

  @Override
  public int getGroupCount() {
    return mListDataHeader.size();
  }

  @Override
  public U getChild(int pGroupPosition, int pChildPosition) {
    return mListData.get(mListDataHeader.get(pGroupPosition)
    ).get(pChildPosition);
  }

  @Override
  public long getChildId(int pGroupPosition, int pChildPosition) {
    return getChild(pGroupPosition, pChildPosition).hashCode();
  }

  @Override
  public T getGroup(int pPosition) {
    return mListDataHeader.get(pPosition);
  }

  @Override
  public long getGroupId(int pPosition) {
    return mListDataHeader.get(pPosition).hashCode();
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public boolean isChildSelectable(int pGroupPosition, int pChildPosition) {
    return true;
  }

  @Override
  public View getGroupView(int pGroupPosition, boolean pIsExpandable, View pGroupView, ViewGroup pParent) {
    if (pGroupView == null) {
      pGroupView = mInflater.inflate(getGroupLayout(), null);
    }
    T tx = getGroup(pGroupPosition);
    bindGroupView(pGroupView, tx);
    return pGroupView;
  }

  @Override
  public View getChildView(int pGroupPosition, final int pChildPosition, boolean pIsLastChild, View pChildView, ViewGroup pParent) {
    if (pChildView == null) {
      pChildView = mInflater.inflate(getChildLayout(), null);
    }
    U tx = getChild(pGroupPosition, pChildPosition);
    bindChildView(pChildView, tx);
    return pChildView;
  }

  /**
   * @return the layout-id for a group
   */
  public abstract int getGroupLayout();
  /**
   * @return the layout-id for a child
   */
  public abstract int getChildLayout();

  /**
   * Binds the group-view
   *
   * @param pGroup
   * @param pEntry
   */
  public abstract void bindGroupView(View pGroup, final T pEntry);

  /**
   * Binds the child-view
   *
   * @param pChild
   * @param pEntry
   */
  public abstract void bindChildView(View pChild, final U pEntry);

}
