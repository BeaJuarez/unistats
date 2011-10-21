package uni.stats;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;

/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * Applications should handle these error conditions.
 * 
 */

public abstract class BaseRequestListener implements RequestListener {

	public void onFacebookError(FacebookError e, final Object state) {
		e.printStackTrace();
	}

	public void onFileNotFoundException(FileNotFoundException e,
			final Object state) {
		e.printStackTrace();
	}

	public void onIOException(IOException e, final Object state) {
		e.printStackTrace();
	}

	public void onMalformedURLException(MalformedURLException e,
			final Object state) {
		e.printStackTrace();
	}

}
