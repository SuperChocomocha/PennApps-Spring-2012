package com.pennapps.spotme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class Settings extends Activity{
	protected static final String PREFS_NAME = "MyPrefsFile";
	protected static boolean showContact;
	protected static boolean showRecent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		bool
		
		final CheckBox name = (CheckBox) this.findViewById(R.id.showname);
		name.setOnClickListener(new OnClickListener(){
		    public void onClick(View v) {
		        // Perform action on clicks, depending on whether it's now checked
		    	if(SpotMeService.showName)name.setChecked(SpotMeService.showName);
		        if (((CheckBox) v).isChecked()) {
		            Toast.makeText(Settings.this, "Name Public", Toast.LENGTH_SHORT).show();
		        } else {
		            Toast.makeText(Settings.this, "Name Private", Toast.LENGTH_SHORT).show();
		        }
		        SpotMeService.showName = name.isChecked();
		    }
		});
		
		final CheckBox contact = (CheckBox) this.findViewById(R.id.showcontact);
		contact.setOnClickListener(new OnClickListener(){
		    public void onClick(View v) {
		        // Perform action on clicks, depending on whether it's now checked
		        if (((CheckBox) v).isChecked()) {
		            Toast.makeText(Settings.this, "Contact Public", Toast.LENGTH_SHORT).show();
		        } else {
		            Toast.makeText(Settings.this, "Contact Private", Toast.LENGTH_SHORT).show();
		        }
		        showContact = contact.isChecked();
		    }
		});
		
		final CheckBox recent = (CheckBox) this.findViewById(R.id.showrecent);
		recent.setOnClickListener(new OnClickListener(){
		    public void onClick(View v) {
		        // Perform action on clicks, depending on whether it's now checked
		        if (((CheckBox) v).isChecked()) {
		            Toast.makeText(Settings.this, "Recent Songs Public", Toast.LENGTH_SHORT).show();
		        } else {
		            Toast.makeText(Settings.this, "Recent Songs Private", Toast.LENGTH_SHORT).show();
		        }
		        showContact = recent.isChecked();
		    }
		});
	}
		
	}
	
