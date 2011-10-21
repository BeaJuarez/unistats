package uni.stats;

import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ListSubjects extends Activity {
	private Auxiliar aux;
	private Vector<String[]> uniperc;

	private EventDataSQLHelper eventData;
	ListView list;
	LazyAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.unis);

		final Auxiliar aux = Auxiliar.getAuxiliar();
		String job = aux.getJob();
		String uniN = aux.getUniversity();
		eventData = aux.getEventData();

		TextView unijName = (TextView) findViewById(R.id.listUnis);
		unijName.setText(uniN);
		TextView subN = (TextView) findViewById(R.id.uni0);
		subN.setText("   Subject");
		TextView subPer = (TextView) findViewById(R.id.perc0);
		subPer.setText("%   ");
		eventData.openDataBase();
		uniperc = eventData.getSubjects(job, uniN);
		eventData.close();

		int sizeArray = uniperc.size();
		String[] uni = new String[uniperc.size()];
		String[] per = new String[uniperc.size()];

		for (int i = 0; i < sizeArray; i++) {
			String[] a = uniperc.elementAt(i);
			uni[i] = (String) a[0];
			per[i] = (String) a[1];

		}
		list = (ListView) findViewById(R.id.list);

		adapter = new LazyAdapter(this, uni, per);
		list.setAdapter(adapter);

	}

}
