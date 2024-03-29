package uni.stats;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

public class JSONEntity 
	extends StringEntity
	{
	        public JSONEntity(JSONObject jsonObject) throws UnsupportedEncodingException
	        {
	                super(jsonObject.toString());
	        }

	        @Override
	        public Header getContentType()
	        {
	                return new BasicHeader(HTTP.CONTENT_TYPE, "application/json");
	        }      
	}


