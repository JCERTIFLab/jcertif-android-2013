package com.jcertif.android.adapters;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcertif.android.R;
import com.jcertif.android.model.Session;
import com.jcertif.android.model.Sponsor;
import com.squareup.picasso.Picasso;

/**
 * 
 * @author Patrick Bashizi
 *
 */
public class SponsorsAdapter extends GenericListAdapter<Sponsor> {

	ViewHolder holder;
	Session session;

	public SponsorsAdapter(Context context, SpeedScrollListener scrollListener,
			List<Sponsor> items) {
		super(context, scrollListener, items);
	}

	@Override
	protected void defineInterpolator() {
		interpolator = new DecelerateInterpolator();
	}

	public View getRowView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_sposor, parent, false);

			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.tv_mail);
			holder.logo = (ImageView) convertView
					.findViewById(R.id.avatar);

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.name.setText(items.get(position).getName());
		holder.name.setText("");
		Picasso.with(context).load(items.get(position).getLogo()).into(holder.logo);

		return convertView;
	}

	public class ViewHolder {
		public TextView name;
		public ImageView logo;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
