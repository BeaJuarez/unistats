package uni.stats;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/** Helper to the database, manages versions and creation */
public class EventDataSQLHelper extends SQLiteOpenHelper {
	private static String DB_PATH = "/data/data/uni.stats/databases/";
	private static String DB_NAME = "JobType";
	private SQLiteDatabase myDataBase;
	private final Initial myContext;

	// Table name
	public static final String TABLE = "Institution_Job";
	public static final String TABLESUBJECTS = "Course_JobType";
	// Columns
	public static final String JOBTYPE = "JobType";
	public static final String INSTITUTION = "Institution";
	public static final String STUDENTPERCENTAGE = "StudentPercentage";

	public EventDataSQLHelper(Initial contexto) {

		super(contexto, DB_NAME, null, 1);
		this.myContext = contexto;
		createDataBase();
	}

	public void createDataBase() {

		boolean dbExist = checkDatabase();

		if (dbExist) {

		} else {
			this.getWritableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying the database");
			}
		}
	}

	private boolean checkDatabase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			e.printStackTrace();
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();

		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public Vector<String> getJobs() {
		Vector<String> jobs = new Vector();
		String job = "";

		Cursor cursor = myDataBase.query(true, EventDataSQLHelper.TABLE,
				new String[] { "JobType" }, null, null, null, null, null, null);
		myContext.startManagingCursor(cursor);
		while (cursor.moveToNext()) {
			job = cursor.getString(0);
			jobs.add(job);
		}

		return jobs;

	}

	public Vector<String[]> getUniversities(String job) {

		Vector<String[]> universities = new Vector();

		Cursor cursor = myDataBase.query(false, EventDataSQLHelper.TABLE,
				new String[] { "Institution", "StudentPercentage" },
				"JobType = '" + job + "'", null, null, null, null, null);

		myContext.startManagingCursor(cursor);

		while (cursor.moveToNext()) {
			String[] university = new String[2];
			university[0] = cursor.getString(0);
			university[1] = cursor.getString(1);
			universities.add(university);
		}
		return universities;

	}

	public Vector<String[]> getSubjects(String job, String uni) {

		Vector<String[]> subjects = new Vector();

		Cursor cursor = myDataBase.query(false,
				EventDataSQLHelper.TABLESUBJECTS, new String[] { "Subject",
						"StudentPercentage" }, "JobType = '" + job
						+ "' and Institution ='" + uni + "'", null, null, null,
				null, null);

		myContext.startManagingCursor(cursor);

		while (cursor.moveToNext()) {
			String[] subject = new String[2];
			subject[0] = cursor.getString(0);
			subject[1] = cursor.getString(1);
			subjects.add(subject);
		}
		return subjects;

	}

	public String getUniversityID(String finaluni) {
		String id = "";
		Cursor cursor = myDataBase.query(false, EventDataSQLHelper.TABLE, null,
				" Institution ='" + finaluni + "'", null, null, null, null,
				null);

		myContext.startManagingCursor(cursor);

		if (cursor.moveToNext()) {
			id = cursor.getString(1);
		}
		return id;
	}

}