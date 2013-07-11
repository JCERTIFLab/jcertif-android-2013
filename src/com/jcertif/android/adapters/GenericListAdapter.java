package com.jcertif.android.adapters;

import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Interpolator;

import android.widget.BaseAdapter;

/**
 * @author Patrick Bashizi
 * @param <T>
 */

public abstract class GenericListAdapter<T> extends BaseAdapter {
	protected static final long ANIM_DEFAULT_SPEED = 1000L;

	protected Interpolator interpolator;

	protected SparseBooleanArray positionsMapper;
	protected List<T> items;
	protected int size, height, width, previousPostition;
	protected SpeedScrollListener scrollListener;
	protected double speed;
	protected long animDuration;
	protected View v;
	protected Context context;

	protected GenericListAdapter(Context context,
			SpeedScrollListener scrollListener, List<T> items) {
		this.context = context;
		this.scrollListener = scrollListener;
		this.items = items;
		if (items != null)
			size = items.size();

		previousPostition = -1;
		positionsMapper = new SparseBooleanArray(size);
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		width = windowManager.getDefaultDisplay().getWidth();
		height = windowManager.getDefaultDisplay().getHeight();

		defineInterpolator();
	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public Object getItem(int position) {
		return items != null && position < size ? items.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getAnimatedView(position, convertView, parent);
	}

	protected abstract View getAnimatedView(int position, View convertView,
			ViewGroup parent);

	protected abstract void defineInterpolator();

}
