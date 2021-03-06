package org.azavea.otm;

import org.azavea.otm.data.User;
import org.azavea.otm.rest.RestClient;
import org.azavea.otm.ui.MainMapActivity;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class Download extends Activity
{
	private LoginManager loginManager = App.getLoginManager();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void showMap(View view) {
    	// Create intent for map-view activity and switch
    	Intent intent = new Intent(view.getContext(), MainMapActivity.class);
    	startActivity(intent);    			
    }
    
    public void testAsync(View view) {
    	RestClient rc = new RestClient();
    	rc.get("/getasdf", new RequestParams(), new JsonHttpResponseHandler() {
    		@Override
    		public void onSuccess(JSONObject resp) {
    			Log.d("Anything", resp.toString());
    		}
			@Override
			public void onFailure(Throwable e) {
				Log.e("Anything", "auth", e);
			}
		    @Override
		    public void onFinish() {
		    	 Log.e("Anything", "finish");
		     }    		
    	});
    }
    
    public void testAuth(View view) {
    	RestClient rc = new RestClient();
    	rc.getWithAuthentication(this, "/login", "administrator", 
				"123456", new RequestParams(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject resp) {
				Log.e("Anything", resp.toString());
			}
			
			@Override
			public void onFailure(Throwable e) {
				Log.e("Anything", "auth", e);
			}
		     @Override
		    public void onFinish() {
		    	 Log.e("Anything", "finish");
		     }
		});
    }

    public void showRequestGeneratorUi(View view) {
    	Intent intent = new Intent(view.getContext(), RequestGeneratorUi.class);
    	startActivity(intent);
    }
}
