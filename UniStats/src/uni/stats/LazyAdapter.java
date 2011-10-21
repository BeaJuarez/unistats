package uni.stats;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private String[] u;
	private String[] p;
	private static LayoutInflater inflater = null;

	public LazyAdapter(Activity a, String[] uni, String[] per) {
		
		activity = a;
		p = per;
		u = uni;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public int getCount() {
		return u.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.item, null);
		
		TextView text = (TextView) vi.findViewById(R.id.text);
		TextView image = (TextView) vi.findViewById(R.id.image);
		if (position < p.length) {
			text.setText(p[position]);
			image.setText(u[position]);
		}
		return vi;
	}
}