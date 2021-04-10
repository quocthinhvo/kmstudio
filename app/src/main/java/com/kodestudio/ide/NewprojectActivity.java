package com.kodestudio.ide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.text.*;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.CompoundButton;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;
import com.google.gson.Gson;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class NewprojectActivity extends AppCompatActivity {
	
	
	private Toolbar _toolbar;
	private double n = 0;
	private boolean ok = false;
	private String appInstalledOrNot = "";
	private boolean isAppInstalled = false;
	private HashMap<String, Object> map_type = new HashMap<>();
	private HashMap<String, Object> map_run = new HashMap<>();
	
	private ArrayList<String> list_type = new ArrayList<>();
	private ArrayList<String> list_project = new ArrayList<>();
	
	private ScrollView vscroll1;
	private LinearLayout linear2;
	private LinearLayout linear4;
	private LinearLayout linear5;
	private LinearLayout linear8;
	private Switch switch2;
	private LinearLayout linear_advan;
	private Button button_create;
	private TextView textview2;
	private EditText edittext_pname;
	private TextView textview3;
	private EditText edittext_author;
	private TextView textview4;
	private Spinner spinner2;
	private TextView textview5;
	private Switch switch_index;
	private Switch switch_log;
	private Switch switch_con;
	private Switch switch_readme;
	private Switch switch_pinfo;
	private Switch switch_license;
	private Switch switch_help;
	private TextView textview6;
	private Switch switch_images;
	private Switch switch_font;
	private Switch switch_media;
	private Switch switch_res;
	private Switch switch_db;
	private Switch switch_css;
	private Switch switch_js;
	
	private Calendar cal = Calendar.getInstance();
	private Intent i_project = new Intent();
	private SharedPreferences data;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.newproject);
		initialize(_savedInstanceState);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		}
		else {
			initializeLogic();
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		_toolbar = (Toolbar) findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		vscroll1 = (ScrollView) findViewById(R.id.vscroll1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		linear5 = (LinearLayout) findViewById(R.id.linear5);
		linear8 = (LinearLayout) findViewById(R.id.linear8);
		switch2 = (Switch) findViewById(R.id.switch2);
		linear_advan = (LinearLayout) findViewById(R.id.linear_advan);
		button_create = (Button) findViewById(R.id.button_create);
		textview2 = (TextView) findViewById(R.id.textview2);
		edittext_pname = (EditText) findViewById(R.id.edittext_pname);
		textview3 = (TextView) findViewById(R.id.textview3);
		edittext_author = (EditText) findViewById(R.id.edittext_author);
		textview4 = (TextView) findViewById(R.id.textview4);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		textview5 = (TextView) findViewById(R.id.textview5);
		switch_index = (Switch) findViewById(R.id.switch_index);
		switch_log = (Switch) findViewById(R.id.switch_log);
		switch_con = (Switch) findViewById(R.id.switch_con);
		switch_readme = (Switch) findViewById(R.id.switch_readme);
		switch_pinfo = (Switch) findViewById(R.id.switch_pinfo);
		switch_license = (Switch) findViewById(R.id.switch_license);
		switch_help = (Switch) findViewById(R.id.switch_help);
		textview6 = (TextView) findViewById(R.id.textview6);
		switch_images = (Switch) findViewById(R.id.switch_images);
		switch_font = (Switch) findViewById(R.id.switch_font);
		switch_media = (Switch) findViewById(R.id.switch_media);
		switch_res = (Switch) findViewById(R.id.switch_res);
		switch_db = (Switch) findViewById(R.id.switch_db);
		switch_css = (Switch) findViewById(R.id.switch_css);
		switch_js = (Switch) findViewById(R.id.switch_js);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		
		switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
				final boolean _isChecked = _param2;
				if (_isChecked) {
					linear_advan.setVisibility(View.VISIBLE);
				}
				else {
					linear_advan.setVisibility(View.GONE);
				}
			}
		});
		
		button_create.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!edittext_pname.getText().toString().equals("")) {
					cal = Calendar.getInstance();
					FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString())));
					if (switch_index.isChecked()) {
						FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("index.html")))), "<title>Kode Studio</title>\n<b>This application run in Kode Studio</b>");
					}
					if (switch_log.isChecked()) {
						FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("log.kode")))), "[Create At]-".concat(new SimpleDateFormat("HH:mm").format(cal.getTime()).concat("_".concat(new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime())))));
					}
					if (switch_con.isChecked()) {
						map_type = new HashMap<>();
						if (spinner2.getSelectedItemPosition() == 0) {
							map_type.put("package", "com.kodestudio.ide");
							map_type.put("path", FileUtil.getPackageDataDir(getApplicationContext()));
							map_type.put("port", "null");
							map_type.put("url", "file:///");
						}
						else {
							if (spinner2.getSelectedItemPosition() == 1) {
								map_type.put("package", "com.kodestudio.server");
								map_type.put("path", "storage/emulated/0/.kodestudio");
								map_type.put("port", "8080");
								map_type.put("url", "localhost:");
							}
							else {
								if (spinner2.getSelectedItemPosition() == 2) {
									map_type.put("package", "com.termux");
									map_type.put("path", "null");
									map_type.put("port", "null");
									map_type.put("url", "null");
								}
								else {
									map_type.put("package", list_type.get((int)(spinner2.getSelectedItemPosition())));
									map_type.put("path", "storage/emulated/0/.kodestudio");
									map_type.put("port", "0000");
									map_type.put("url", "file:///");
								}
							}
						}
						if (switch_index.isChecked()) {
							map_type.put("startpage", "index.html");
						}
						else {
							map_type.put("startpage", "index.html");
						}
						FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("manifest.kode")))), new Gson().toJson(map_type));
					}
					if (switch_readme.isChecked()) {
						FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("readme.md")))), "Write your note here...");
					}
					if (switch_pinfo.isChecked()) {
						FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("project.info")))), "[projectname]-".concat(edittext_pname.getText().toString().concat("\n[Author]-".concat(edittext_author.getText().toString().concat("\n[Host]-Localhost".concat(""))))));
					}
					if (switch_license.isChecked()) {
						FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("license.txt")))), "A project using Kode Studio");
					}
					if (switch_help.isChecked()) {
						FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("help.txt")))), "Write help here...");
					}
					if (switch_images.isChecked()) {
						FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("images")))));
					}
					if (switch_font.isChecked()) {
						FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("font")))));
					}
					if (switch_media.isChecked()) {
						FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("media")))));
					}
					if (switch_res.isChecked()) {
						FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("res")))));
					}
					if (switch_db.isChecked()) {
						FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("database")))));
					}
					if (switch_css.isChecked()) {
						FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("css")))));
						FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/css/".concat("style.css")))), "Write web style here");
						FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/css/".concat("color.css")))), "Write web color here");
					}
					if (switch_js.isChecked()) {
						FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString().concat("/".concat("js")))));
					}
					data.edit().putString("project", FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(edittext_pname.getText().toString()))).commit();
					data.edit().putString("author", edittext_author.getText().toString()).commit();
					i_project.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i_project.setClass(getApplicationContext(), ProjectActivity.class);
					startActivity(i_project);
					finish();
				}
				else {
					_Text_error(edittext_pname, "");
				}
			}
		});
		
		edittext_pname.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
	}
	private void initializeLogic() {
		setTitle("New Project");
		list_type.add("com.kodestudio.ide");
		_checkPackageInstall("com.kodestudio.server");
		_checkPackageInstall("com.google.android.webview");
		_checkPackageInstall("com.android.chrome");
		_checkPackageInstall("com.termux");
		_checkPackageInstall("ru.kslabs.ksweb");
		_checkPackageInstall("com.esminis.server.php");
		_checkPackageInstall("com.bans_droid");
		_checkPackageInstall("com.estrongs.android.pop");
		spinner2.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, list_type));
		edittext_author.setText(data.getString("author", ""));
		FileUtil.listDir(FileUtil.getExternalStorageDir().concat("/.kodestudio"), list_project);
		linear_advan.setVisibility(View.GONE);
		_setStyle("#8BC34A", 360, 0, button_create);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	private void _setStyle (final String _color, final double _numb, final double _number, final View _view) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();gd.setColor(Color.parseColor(_color));gd.setCornerRadius((int)_numb);_view.setBackground(gd);
		
		_view.setElevation((int)_number);
	}
	
	
	private void _Text_error (final TextView _text, final String _string_error) {
		_text.setError(_string_error);
	}
	
	
	private void _checkPackageInstall (final String _packagename) {
		appInstalledOrNot = _packagename;
		boolean isAppInstalled = appInstalledOrNot(_packagename);
		if (isAppInstalled) {
			list_type.add(_packagename);
		}
		else {
			
		}
	}
	private boolean appInstalledOrNot(String uri) { 
		
		android.content.pm.PackageManager pm = getPackageManager(); 
		try { 
			
			pm.getPackageInfo(uri, android.content.pm.PackageManager.GET_ACTIVITIES); return true;
		} catch (android.content.pm.PackageManager.NameNotFoundException e) { } 
		
		return false;
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}
