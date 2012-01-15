package com.pennapps.spotme;

import java.io.InputStream;

import android.app.Activity;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.location.*;
import android.app.*;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class SpotMeActivity extends Activity {


	Facebook facebook = new Facebook("198313533597169");
	private SharedPreferences mPrefs;
	InputStream is = null;
	StringBuilder sb = null;
	String result = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if(access_token != null)
			facebook.setAccessExpires(expires);
		
		SharedPreferences settings = getSharedPreferences(Settings.PREFS_NAME,0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("facebookToken", facebook.toString());
		editor.commit();
	
		final ToggleButton activateButton = (ToggleButton) this.findViewById(R.id.activateButton);
		activateButton.setTextOn("SpotMe ON");
		activateButton.setTextOff("Activate SpotMe!");
		SharedPreferences pref = getSharedPreferences(Settings.PREFS_NAME,0);
		activateButton.setChecked(pref.getBoolean("activated", false));
		activateButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0){
				Intent activityIntent = new Intent(SpotMeActivity.this, SpotMeService.class);
				if (activateButton.isChecked()){
					startService(activityIntent);
				}else{
					stopService(activityIntent);
				}
			}
		});

		Button settingsButton = (Button) this.findViewById(R.id.settings);
		settingsButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0){
				System.out.println("Settings");
				Intent intent = new Intent(SpotMeActivity.this, Settings.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}

}


