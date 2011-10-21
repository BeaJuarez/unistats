package uni.stats;

import java.util.Vector;

import org.alexd.jsonrpc.JSONRPCException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListUniversities extends Activity {
	private Auxiliar aux;
	private Vector<String[]> uniperc;
	private String uni;
	private EventDataSQLHelper eventData;
	ListView list;
	LazyAdapter adapter;
	private boolean internetCon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unis);

		final Auxiliar aux = Auxiliar.getAuxiliar();
		String job = aux.getJob();
		eventData = aux.getEventData();
		TextView jobName = (TextView) findViewById(R.id.listUnis);
		jobName.setText(job);
		internetCon = true;
		eventData.openDataBase();
		uniperc = eventData.getUniversities(job);
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
		list.setClickable(true);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// TODO Auto-generated method stub
				
				String[] a = uniperc.elementAt(arg2);
				String finaluni = (String) a[0];
				String uniID = "";
				eventData.openDataBase();
				uniID = eventData.getUniversityID(finaluni);
				eventData.close();

				Intent editIntent = new Intent(ListUniversities.this,
						College.class);
				aux.setUniversity(finaluni);
				aux.setUniID(uniID);

				JSONRPCHttpClient client = new JSONRPCHttpClient(
						"http://ilineup.co.uk/unistats.ashx");
				client.setConnectionTimeout(90000);
				client.setSoTimeout(90000);

				try {

					String d = client.callString("getDetails", uniID);

					int x = d.length();
					d = d.substring(2, x - 2);
					d = d.replace('"', ' ');
					String[] data = d.split(",");
					aux.setUniData(data);

				} catch (JSONRPCException e) {
					Toast.makeText(ListUniversities.this,
							"Internet connection needed", Toast.LENGTH_SHORT)
							.show();
					internetCon = false;

				}

				if (internetCon) {
					startActivity(editIntent);
				}
			}

		});

	}

}
