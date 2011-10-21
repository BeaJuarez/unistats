package uni.stats;

public class Auxiliar {

	private static String job;
	private static String university;
	private static String subject;
	private static Auxiliar aux;
	private static EventDataSQLHelper eventData;
	private static String uniID;
private static String [] uniData;
	public Auxiliar(Initial ini) {
		aux = this;
		job = "";
		university = "";
		eventData = new EventDataSQLHelper(ini);
		uniID="";
		uniData= null;

	}

	public static void setJob(String j) {
		job = j;
	}

	public static String getJob() {
		return job;
	}
	
	public static String [] getUniData(){
		return uniData;
	}
	public static void setUniData(String [] u){
		uniData=u;
	}
	public static void setUniID(String j) {
		uniID = j;
	}

	public static String getUniID() {
		return uniID;
	}

	public static void setUniversity(String uni) {
		university = uni;
	}

	public static String getUniversity() {
		return university;
	}

	public static void setSubject(String s) {
		subject = s;
	}

	public static String getSubject() {
		return subject;
	}

	public static Auxiliar getAuxiliar() {
		return aux;
	}

	public static EventDataSQLHelper getEventData() {
		return eventData;
	}

}
