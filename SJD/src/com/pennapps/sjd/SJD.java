package com.pennapps.sjd;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class SJD extends Activity {
	/** Called when the activity is first created. */
	boolean authorized = false;
	boolean cancel = false;
	Facebook facebook = new Facebook("108489142591609");
	Random random = new Random();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	public void onloginClick(View view){
		if(!authorized)
			facebook.authorize(this, new DialogListener() {
				@Override
				public void onComplete(Bundle values) {
					authorized = true; 
				}

				@Override
				public void onFacebookError(FacebookError error) { }

				@Override
				public void onError(DialogError e) { }

				@Override
				public void onCancel() { cancel = true;}
			});
		
		String key = new String();
		for(int i = 0; i < 15; i++)
			key = key + String.valueOf(random.nextInt(93) + 33);
		try {
			// open myfilename.txt for writing
			OutputStreamWriter out = new OutputStreamWriter(openFileOutput("key.txt",0));
			// write the contents on mySettings to the file
			out.write(key);
			// close the file
			out.close();
		} catch (java.io.IOException e) {
			//do something if an IOException occurs.
		}
		Intent myIntent = new Intent(view.getContext(), AppSelector.class);
        startActivityForResult(myIntent, 0);
		
	}

	public void onsignoutClick(View view){
			try {
				facebook.setAccessToken(facebook.REDIRECT_URI);
				facebook.logout(getApplication());
				
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				setContentView(R.layout.main);
				setContentView(R.layout.main2);

				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				setContentView(R.layout.main);
				setContentView(R.layout.main2);
				e1.printStackTrace();
			}
		authorized = !authorized;
		try {
			// Empties file
			OutputStreamWriter out = new OutputStreamWriter(openFileOutput("key.txt",0));
			out.close();
		} catch (java.io.IOException e) {
			//do something if an IOException occurs.
		}
		
	}

	public void onproceedClick(View view){
		if(authorized){
			setContentView(R.layout.app);
			//			Intent myIntent = new Intent(this, AppSelector.class);
			//            startActivity(myIntent);
		}
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}

}