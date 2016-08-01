package com.lockmotor.base.baseView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by VietHoa on 30/01/16.
 */
public class BaseRealmListAdapter<T extends RealmObject> extends BaseAdapter {

    protected Context mContext;
    protected RealmList<T> mDataArray;

    public BaseRealmListAdapter(Context context, RealmList<T> data) {
        this.mContext = context;
        this.mDataArray = data;
    }

    public void refreshData(RealmList<T> data) {
        mDataArray = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDataArray == null)
            return 0;
        return mDataArray.size();
    }

    @Override
    public T getItem(int i) {
        if (mDataArray == null)
            return null;
        if (i >= mDataArray.size())
            return null;

        return mDataArray.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (mDataArray == null)
            return 0;
        if (i >= mDataArray.size())
            return 0;

        return mDataArray.get(i).hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        return null;
    }
}

