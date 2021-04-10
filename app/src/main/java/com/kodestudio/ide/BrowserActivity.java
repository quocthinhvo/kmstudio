package com.kodestudio.ide;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.app.Activity;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.WebViewClient;
import android.view.View;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class BrowserActivity extends AppCompatActivity {
	
	
	private String str_url = "";
	private String str = "";
	private boolean mode = false;
	
	private LinearLayout linear1;
	private ProgressBar prog;
	private WebView webview1;
	private ImageView imageview;
	private HorizontalScrollView hscroll2;
	private ImageView imageview2;
	private ImageView imageview1;
	private ImageView imageview_more;
	private TextView textview1;
	
	private SharedPreferences data;
	private AlertDialog.Builder dua;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.browser);
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
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		prog = (ProgressBar) findViewById(R.id.prog);
		webview1 = (WebView) findViewById(R.id.webview1);
		webview1.getSettings().setJavaScriptEnabled(true);
		webview1.getSettings().setSupportZoom(true);
		imageview = (ImageView) findViewById(R.id.imageview);
		hscroll2 = (HorizontalScrollView) findViewById(R.id.hscroll2);
		imageview2 = (ImageView) findViewById(R.id.imageview2);
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		imageview_more = (ImageView) findViewById(R.id.imageview_more);
		textview1 = (TextView) findViewById(R.id.textview1);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		dua = new AlertDialog.Builder(this);
		
		webview1.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				_WriteError(data.getString("project", ""), "StartLoad", _url);
				textview1.setText(webview1.getUrl());
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				_WriteError(data.getString("project", ""), "PageFinished", _url);
				imageview.setImageBitmap(webview1.getFavicon());
				
				
				textview1.setText (webview1.getTitle().toString ());
				super.onPageFinished(_param1, _param2);
			}
		});
		
		imageview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (mode) {
					webview1.getSettings().setLoadWithOverviewMode(false);
					 webview1.getSettings().setUseWideViewPort(false); 
					
					webview1.reload ();
					imageview2.setImageResource(R.drawable.ic_developer_mode_black);
					mode = false;
				}
				else {
					webview1.getSettings().setLoadWithOverviewMode(true);
					 webview1.getSettings().setUseWideViewPort(true); 
					
					webview1.reload ();
					imageview2.setImageResource(R.drawable.ic_desktop_windows_black);
					mode = true;
				}
			}
		});
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				webview1.reload ();
			}
		});
		
		imageview_more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_PopupMenu();
			}
		});
		
		textview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				str = webview1.getUrl();
				LinearLayout mylayout = new LinearLayout(BrowserActivity.this);
				
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				
				mylayout.setLayoutParams(params); mylayout.setOrientation(LinearLayout.VERTICAL);
				
				final EditText myedittext = new EditText(BrowserActivity.this);
				myedittext.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
				 
				mylayout.addView(myedittext);
				dua.setView(mylayout);
				myedittext.setHint("Enter URL here");
				myedittext.setText (str);
				dua.setMessage("Add your url?");
				dua.setPositiveButton("Go", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						str_url = myedittext.getText ().toString ();
						webview1.loadUrl(str_url);
					}
				});
				dua.create().show();
			}
		});
	}
	private void initializeLogic() {
		mode = false;
		webview1.loadUrl("http://localhost:".concat(data.getString("port", "").concat("/".concat(Uri.parse(data.getString("project", "")).getLastPathSegment()))));
		final WebSettings webSettings = webview1.getSettings(); 
		webview1.getSettings().setBuiltInZoomControls(true);
		webview1.getSettings().setDisplayZoomControls(false);
		webview1.getSettings().setJavaScriptEnabled(true);
		final String newUserAgent; newUserAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"; webSettings.setUserAgentString(newUserAgent);
		
		webview1.setWebChromeClient(new WebChromeClient() {
			@Override public void onProgressChanged(WebView view, int newProgress) {
				prog.setProgress(newProgress);
				
				if (newProgress == 100)
				{
					
					prog.setVisibility(View.GONE);
				}
				
				else 
				{
					prog.setVisibility(View.VISIBLE);
				}}
			public void onReceivedError(WebView webview1, int errorCode,String description, String failingUrl) 
			{
				
				//des =description;
				//code = errorCode;
			}});
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (webview1.canGoBack()) {
			webview1.goBack();
		}
		else {
			_WriteError(data.getString("project", ""), "ExitBuild", "");
			finish();
		}
	}
	private void _WriteError (final String _path, final String _type, final String _des) {
		FileUtil.writeFile(_path.concat("/log.kode"), FileUtil.readFile(_path.concat("/log.kode")).concat("\n\n$[".concat(_type.concat("]- ".concat(_des.concat(""))))));
	}
	
	
	private void _PopupMenu () {
		
		PopupMenu popup = new PopupMenu(BrowserActivity.this, imageview_more);
		Menu menu = popup.getMenu();
		menu.add("Aan").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add("Gabriel").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getTitle().toString()) {
					case "Aan":
					SketchwareUtil.showMessage(getApplicationContext(), "Add here what you want. ");
					return true;
					case "Gabriel":
					SketchwareUtil.showMessage(getApplicationContext(), "Add here what you want. ");
					return true;
					default: return false;
				}
			}
		});
		
		
		popup.show();
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
