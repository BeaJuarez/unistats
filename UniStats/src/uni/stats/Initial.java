package uni.stats;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class Initial extends Activity {

	EventDataSQLHelper eventsData;
	Vector<String> jobs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_initial);
		final Auxiliar aux = new Auxiliar(this);
		eventsData = aux.getEventData();
		eventsData.openDataBase();
		jobs = eventsData.getJobs();
		eventsData.close();

		final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_jobs);
		ArrayAdapter<String> adaptern = new ArrayAdapter<String>(this,
				R.layout.list_item, jobs);

		textView.setAdapter(adaptern);

		Button go = (Button) findViewById(R.id.buttonGo);
		go.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				boolean exists = false;
				String k = textView.getText().toString();
				for (int i = 0; i < jobs.size(); i++) {
					if (k.equals(jobs.elementAt(i))) {
						exists = true;
					}
				}

				if (exists) {
					aux.setJob(textView.getText().toString());

					Intent editIntent = new Intent(Initial.this,
							ListUniversities.class);
					startActivity(editIntent);
				} else {
					if (textView.getText().toString().equals("")) {
						Toast.makeText(Initial.this, "Please, type a job",
								Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(Initial.this,
								"Please, type a correct job",
								Toast.LENGTH_SHORT).show();
						textView.setText("");
					}

				}
			}
		});
	}

}