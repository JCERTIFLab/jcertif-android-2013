/*
 * Copyright 2013 JCertifLab.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jcertif.android.adapters;

import java.util.List;

import android.R.color;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.jcertif.android.R;
import com.jcertif.android.model.AgendaSession;
import com.jcertif.android.model.Session;

/**
 * 
 * @author Patrick Bashizi
 * 
 */
public class AgendaAdapter extends GenericListAdapter<Session> {

	ViewHolder holder;
	Session session;
	private int selectedIndex = -1;
	private int selectedColor;

	public AgendaAdapter(Context context, SpeedScrollListener scrollListener,
			List<Session> items) {
		super(context, scrollListener, items);
		selectedColor = context.getResources().getColor(
				R.color.pressed_jcertifstyle);
	}

	@Override
	protected void defineInterpolator() {
		interpolator = new DecelerateInterpolator();
	}

	public View getRowView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_session, parent, false);

			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.summary = (TextView) convertView
					.findViewById(R.id.tv_summary);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (selectedIndex != -1 && position == selectedIndex) {
			convertView.setBackgroundColor(selectedColor);
		} else {
			convertView.setBackgroundColor(color.white);
		}

		holder.title.setText(items.get(position).getTitle());
		holder.summary.setText(items.get(position).getSummary());

		return convertView;
	}

	public void setSelectedIndex(int ind) {
		selectedIndex = ind;
		notifyDataSetChanged();
	}

	public class ViewHolder {
		public TextView title;
		public TextView summary;
	}

	@Override
	public View getAnimatedView(int position, View convertView, ViewGroup parent) {
		v = getRowView(position, convertView, parent);

		if (v != null && !positionsMapper.get(position)
				&& position > previousPostition) {
			speed = scrollListener.getSpeed();

			animDuration = (((int) speed) == 0) ? ANIM_DEFAULT_SPEED
					: (long) (1 / speed * 15000);

			if (animDuration > ANIM_DEFAULT_SPEED)
				animDuration = ANIM_DEFAULT_SPEED;

			previousPostition = position;

			if (android.os.Build.VERSION.SDK_INT > 10) {
				v.setTranslationX(0.0F);
				v.setTranslationY(height);
				v.setRotationX(45.0F);
				v.setScaleX(0.7F);
				v.setScaleY(0.55F);

				ViewPropertyAnimator localViewPropertyAnimator = v.animate()
						.rotationX(0.0F).rotationY(0.0F).translationX(0)
						.translationY(0).setDuration(animDuration).scaleX(1.0F)
						.scaleY(1.0F).setInterpolator(interpolator);

				localViewPropertyAnimator.setStartDelay(0).start();
			}
			positionsMapper.put(position, true);
		}
		return v;
	}

}
