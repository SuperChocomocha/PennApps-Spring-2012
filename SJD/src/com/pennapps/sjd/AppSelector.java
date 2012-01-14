package com.pennapps.sjd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AppSelector extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app);
	}
	
	public void onmusicClick(View view){
		Intent myIntent = new Intent(view.getContext(), music.class);
        startActivityForResult(myIntent, 0);
	}

	public void onfoodClick(View view){
		Intent myIntent = new Intent(view.getContext(), food.class);
        startActivityForResult(myIntent, 0);
	}

	public void onmoviesClick(View view){
		Intent myIntent = new Intent(view.getContext(), movies.class);
        startActivityForResult(myIntent, 0);
	}
	
	public void ontvClick(View view){
		Intent myIntent = new Intent(view.getContext(), tv.class);
        startActivityForResult(myIntent, 0);
	}
	
}
