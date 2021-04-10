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
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.app.Activity;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import android.webkit.WebViewClient;
import android.view.View;
import android.net.Uri;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class RunActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private String des = "";
	private String num_e = "";
	private double code = 0;
	private boolean mode = false;
	private String str = "";
	private String url = "";
	private HashMap<String, Object> map_console = new HashMap<>();
	private boolean console = false;
	
	private ArrayList<HashMap<String, Object>> list_console = new ArrayList<>();
	
	private LinearLayout linear1;
	private ProgressBar prog;
	private WebView webview1;
	private LinearLayout linear_console;
	private ImageView imageview1;
	private HorizontalScrollView hscroll1;
	private ImageView imageview2;
	private ImageView imageview_reload;
	private ImageView imageview_more;
	private TextView textview1;
	private ListView listview1;
	
	private SharedPreferences data;
	private AlertDialog.Builder dialog_error;
	private Calendar cal = Calendar.getInstance();
	private AlertDialog.Builder dua;
	private TimerTask timer;
	private TimerTask clock;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.run);
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
		linear_console = (LinearLayout) findViewById(R.id.linear_console);
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		hscroll1 = (HorizontalScrollView) findViewById(R.id.hscroll1);
		imageview2 = (ImageView) findViewById(R.id.imageview2);
		imageview_reload = (ImageView) findViewById(R.id.imageview_reload);
		imageview_more = (ImageView) findViewById(R.id.imageview_more);
		textview1 = (TextView) findViewById(R.id.textview1);
		listview1 = (ListView) findViewById(R.id.listview1);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		dialog_error = new AlertDialog.Builder(this);
		dua = new AlertDialog.Builder(this);
		
		webview1.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				cal = Calendar.getInstance();
				_WriteError(data.getString("project", ""), "PageLoad", _url.replace("file:///".concat(data.getString("project", "")), "").concat(" | ".concat(String.valueOf((long)(cal.getTimeInMillis())))));
				cal = Calendar.getInstance();
				if (_url.substring((int)(0), (int)(8)).equals("file:///")) {
					if (_url.substring((int)(_url.length() - 1), (int)(_url.length())).equals("#")) {
						if (!FileUtil.isExistFile(_url.substring((int)(8), (int)(_url.length() - 1)))) {
							webview1.setVisibility(View.GONE);
							dialog_error.setMessage(Uri.parse(_url.substring((int)(8), (int)(_url.length()))).getLastPathSegment().concat(" not found!"));
							_WriteError(data.getString("project", ""), "Run Error", _url.substring((int)(8), (int)(_url.length())).concat(" not found!"));
							dialog_error.setPositiveButton("GoBack", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									webview1.goBack();
									webview1.setVisibility(View.VISIBLE);
								}
							});
							dialog_error.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									finish();
								}
							});
							dialog_error.create().show();
						}
					}
					else {
						if (!FileUtil.isExistFile(_url.substring((int)(8), (int)(_url.length())))) {
							webview1.setVisibility(View.GONE);
							dialog_error.setMessage(Uri.parse(_url.substring((int)(8), (int)(_url.length()))).getLastPathSegment().concat(" not found!"));
							_WriteError(data.getString("project", ""), "Run Error", _url.substring((int)(8), (int)(_url.length())).concat(" not found!"));
							dialog_error.setPositiveButton("GoBack", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									webview1.goBack();
									webview1.setVisibility(View.VISIBLE);
								}
							});
							dialog_error.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									finish();
								}
							});
							dialog_error.create().show();
						}
					}
				}
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				imageview1.setImageBitmap(webview1.getFavicon());
				
				
				textview1.setText (webview1.getTitle().toString ());
				cal = Calendar.getInstance();
				_WriteError(data.getString("project", ""), "PageFinished", _url.replace("file:///".concat(data.getString("project", "")), "").concat(" | ".concat(String.valueOf((long)(cal.getTimeInMillis())))));
				super.onPageFinished(_param1, _param2);
			}
		});
		
		linear_console.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
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
		
		imageview_reload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				webview1.reload ();
				_WriteError(data.getString("project", ""), "Reload", "");
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
				LinearLayout mylayout = new LinearLayout(RunActivity.this);
				
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				
				mylayout.setLayoutParams(params); mylayout.setOrientation(LinearLayout.VERTICAL);
				
				final EditText myedittext = new EditText(RunActivity.this);
				myedittext.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
				 
				mylayout.addView(myedittext);
				dua.setView(mylayout);
				myedittext.setHint("Enter URL here");
				myedittext.setText (str);
				dua.setMessage("URL");
				dua.setPositiveButton("Go", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						url = myedittext.getText ().toString ();
						webview1.loadUrl(url);
					}
				});
				dua.create().show();
			}
		});
	}
	private void initializeLogic() {
		mode = false;
		console = false;
		linear_console.setVisibility(View.GONE);
		imageview1.setEnabled(false);
		imageview1.setVisibility(View.GONE);
		listview1.setTranscriptMode (ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listview1.setStackFromBottom (true);
		listview1.setDivider (null);listview1.setDividerHeight (0);
		textview1.setVisibility(View.VISIBLE);
		textview1.setText(Uri.parse(data.getString("project", "").concat("/".concat(data.getString("project_run", "")))).getLastPathSegment());
		webview1.loadUrl("file:///".concat(data.getString("project", "").concat("/".concat(data.getString("project_run", "")))));
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
				
				des =description;
				code = errorCode;
				_WriteError(data.getString("project", ""), "Run Error:".concat(String.valueOf((long)(code))), des);
			}
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) { 
				DownloadManager.Request request = new DownloadManager.Request( Uri.parse(url)); request.setMimeType(mimeType); String cookies = CookieManager.getInstance().getCookie(url); request.addRequestHeader("cookie", cookies); request.addRequestHeader("User-Agent", userAgent); request.setDescription("Downloading file..."); request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType)); request.allowScanningByMediaScanner(); request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName( url, contentDisposition, mimeType)); DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE); 
				dm.enqueue(request);
				 Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show(); }
		});
		map_console = new HashMap<>();
		map_console.put("input", " @log");
		map_console.put("output", FileUtil.readFile(data.getString("project", "").concat("/log.kode")));
		list_console.add(map_console);
		listview1.setAdapter(new Listview1Adapter(list_console));
		timer = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (!list_console.get((int)0).get("output").toString().equals(FileUtil.readFile(data.getString("project", "").concat("/log.kode")))) {
							list_console.clear();
							map_console = new HashMap<>();
							map_console.put("input", " @log");
							map_console.put("output", FileUtil.readFile(data.getString("project", "").concat("/log.kode")));
							list_console.add(map_console);
							((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
						}
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(timer, (int)(150), (int)(150));
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
			webview1.setVisibility(View.VISIBLE);
		}
		else {
			finish();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		_WriteError(data.getString("project", ""), "Exit Build", "");
		_WriteError(data.getString("project", ""), "+", "--------------------------------");
	}
	private void _WriteError (final String _path, final String _type, final String _des) {
		FileUtil.writeFile(_path.concat("/log.kode"), FileUtil.readFile(_path.concat("/log.kode")).concat("\n\n$[".concat(_type.concat("]- ".concat(_des.concat(""))))));
	}
	
	
	private void _PopupMenu () {
		
		PopupMenu popup = new PopupMenu(RunActivity.this, imageview_more);
		Menu menu = popup.getMenu();
		menu.add("Stop Run").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add("View Code").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		menu.add("Debug").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
		menu.add("Show/hide console").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
		menu.add("Show/hide icon").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getTitle().toString()) {
					case "Stop Run":
					_WriteError(data.getString("project", ""), "BuildStop", "");
					if (1 > 0) {
						finish();
					}
					return true;
					case "View Code":
					webview1.loadUrl("view-source:".concat(webview1.getUrl()));
					return true;
					case "Show/hide console":
					if (console) {
						linear_console.setVisibility(View.GONE);
						console = false;
					}
					else {
						linear_console.setVisibility(View.VISIBLE);
						console = true;
					}
					return true;
					case "Show/hide icon":
					if (imageview1.isEnabled()) {
						imageview1.setEnabled(false);
						imageview1.setVisibility(View.GONE);
					}
					else {
						imageview1.setEnabled(true);
						imageview1.setVisibility(View.VISIBLE);
					}
					return true;
					default: return false;
				}
			}
		});
		
		
		popup.show();
	}
	
	
	public class Listview1Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.cus_console, null);
			}
			
			final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
			final TextView textview_input = (TextView) _v.findViewById(R.id.textview_input);
			final TextView textview_output = (TextView) _v.findViewById(R.id.textview_output);
			
			textview_input.setText(_data.get((int)_position).get("input").toString());
			textview_output.setText(_data.get((int)_position).get("output").toString());
			linear2.setBackgroundColor(Color.TRANSPARENT);
			textview_input.setTextColor(0xFF000000);
			textview_output.setTextColor(0xFF000000);
			textview_input.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/console.ttf"), 0);
			textview_output.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/console.ttf"), 0);
			
			return _v;
		}
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
