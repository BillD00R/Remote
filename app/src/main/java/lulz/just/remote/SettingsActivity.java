package lulz.just.remote;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;

public class SettingsActivity extends Activity {
	
	
	SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		final EditText addrEdit = (EditText) findViewById(R.id.edit_address);
		final EditText portEdit = (EditText) findViewById(R.id.edit_port);
		mPrefs = PreferenceManager
				.getDefaultSharedPreferences(this.getApplicationContext());
		if(mPrefs.contains(Globals.ADDRESS))
			addrEdit.setText(mPrefs.getString(Globals.ADDRESS, ""));
		if(mPrefs.contains(Globals.PORT))
			portEdit.setText(mPrefs.getString(Globals.PORT, ""));
		
		addrEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String addr = addrEdit.getText().toString();
				Editor edit = mPrefs.edit();
				edit.putString(Globals.ADDRESS, addr);
				edit.apply();
			}
		});
		
		portEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {

				Editor edit = mPrefs.edit();
				edit.putString(Globals.PORT, portEdit.getText().toString());
				edit.apply();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
