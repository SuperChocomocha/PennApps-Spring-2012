package com.pennapps.spotme;

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
   
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        facebook.authorize(this, new String[] { "email", "offline_access", "publish_checkins" },

        	      new DialogListener() {
        	           
        	           public void onComplete(Bundle values) {}

        	           
        	           public void onFacebookError(FacebookError error) {}

        	           
        	           public void onError(DialogError e) {}

        	           
        	           public void onCancel() {}
        	      }
        	);
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
        
        Button settings = (Button) this.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener(){
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
    /*
    private boolean isSpotMeRunning(){
    	ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.pennapps.spotme".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }*/
    
}


