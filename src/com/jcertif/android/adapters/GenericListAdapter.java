package com.jcertif.android.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;

import android.widget.BaseAdapter;


/**
 * 
 * @param <T>
 */

public abstract class GenericListAdapter<T> extends BaseAdapter {

	List<T> list;
	LayoutInflater inflater;
	protected Context context;

	public GenericListAdapter(Context context, List<T> list) {
		super();
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.context=context;
	}

	public int getCount() {

		return list.size();
	}

	public Object getItem(int index) {
		// TODO Auto-generated method stub
		return list.get(index);
	}

	public long getItemId(int index) {
		// TODO Auto-generated method stub
		return list.indexOf(list.get(index));
	}

}
