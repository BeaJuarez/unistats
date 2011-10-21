package uni.stats;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class College extends Activity {
	private Vector<String[]> uniperc;

	private EventDataSQLHelper eventData;
	private Button shareFb;
	private Button shareTwitter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.college);

		String uni = Auxiliar.getUniversity();
		String[] data = Auxiliar.getUniData();
		if (data[0].length() > 10) {
			TextView email = (TextView) findViewById(R.id.mail);
			email.setText(data[0]);
		}

		TextView web = (TextView) findViewById(R.id.web);
		web.setText(data[1]);

		TextView phone = (TextView) findViewById(R.id.tel);
		phone.setText(data[2]);

		TextView address = (TextView) findViewById(R.id.ad);
		address.setText(data[3]);

		shareFb = (Button) findViewById(R.id.sharefacebook);
		shareFb.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent editIntent = new Intent(College.this,
						ShareFacebook.class);
				startActivity(editIntent);

			}
		});
		shareTwitter = (Button) findViewById(R.id.sharetwitter);
		shareTwitter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent editIntent = new Intent(College.this, ShareTwitter.class);
				startActivity(editIntent);

			}
		});

		TextView uniName = (TextView) findViewById(R.id.uniName);
		uniName.setText(uni);

		final Auxiliar aux = Auxiliar.getAuxiliar();
		String job = aux.getJob();

		eventData = aux.getEventData();
		eventData.openDataBase();
		uniperc = eventData.getSubjects(job, uni);
		eventData.close();

		int sizeArray = uniperc.size();

		Button subjects = (Button) findViewById(R.id.subjects);
		if (sizeArray == 0) {
			subjects.setVisibility(View.INVISIBLE);

		} else {

			subjects.setVisibility(View.VISIBLE);
			subjects.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent editIntent = new Intent(College.this,
							ListSubjects.class);

					startActivity(editIntent);
				}
			});
		}

	}

}
