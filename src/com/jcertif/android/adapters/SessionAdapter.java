package com.jcertif.android.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcertif.android.R;
import com.jcertif.android.model.Session;

public class SessionAdapter extends GenericListAdapter<Session> {

	ViewHolder holder;
	Session session;

	public SessionAdapter(Context context, List<Session> list) {
		super(context, list);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_session, null);
			holder.title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.summary = (TextView) convertView
					.findViewById(R.id.tv_summary);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		session = list.get(position);
		holder.title.setText(session.getTitle());
		holder.summary.setText(session.getSummary());
		return convertView;
	}

	public class ViewHolder {
		public TextView title;
		public TextView summary;
	}

}
