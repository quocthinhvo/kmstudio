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
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.Activity;
import android.content.SharedPreferences;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.graphics.Typeface;
import java.text.DecimalFormat;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class ConsoleActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private String Str = "";
	private HashMap<String, Object> mapv = new HashMap<>();
	private double n = 0;
	private double m = 0;
	private String main = "";
	private String path = "";
	private String tg = "";
	private String str_port = "";
	
	private ArrayList<HashMap<String, Object>> listmap_console = new ArrayList<>();
	private ArrayList<String> list_cmd = new ArrayList<>();
	
	private ListView listview1;
	private LinearLayout linear1;
	private EditText edittext1;
	private ImageView imageview1;
	
	private SharedPreferences data;
	private TimerTask timer;
	private Intent i_run = new Intent();
	private Intent i_browser = new Intent();
	private RequestNetwork webcall;
	private RequestNetwork.RequestListener _webcall_request_listener;
	private RequestNetwork clone;
	private RequestNetwork.RequestListener _clone_request_listener;
	private AlertDialog.Builder dia_local;
	private Intent i_editor = new Intent();
	private Intent i_viewcode = new Intent();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.console);
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
		
		listview1 = (ListView) findViewById(R.id.listview1);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		edittext1 = (EditText) findViewById(R.id.edittext1);
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		webcall = new RequestNetwork(this);
		clone = new RequestNetwork(this);
		dia_local = new AlertDialog.Builder(this);
		
		linear1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!edittext1.getText().toString().equals("")) {
					list_cmd.clear();
					main = "";
					Str = edittext1.getText().toString();
					edittext1.setText("");
					_GetCMD(Str);
					main = list_cmd.get((int)(0));
					switch (main)
					{
						case "help":
						_Command(main, "+cls: clear screen.\n\n+get \"plugin\" \"path\": download and install plugin to your project.\n\n+clear: clear all project.\n\n+create \"filepath\": create file.\n\n+run \"type run\" \"start file\": run your project.\n\n+open \"file path\": open the file.\n\n+export \"kode/zip\" \"path output\": export your project.\n\n+import \"file export path\" \"path output\": import file, plugin to your project.\n\n+delete \"path\": remove folder, file or plugin in project.\n\n+copy \"path start\" \"path end\": copy file or folder in SD Card to your project.\n\n+cut \"path start\" \"path end\": move file or folder in SD Card to your project.\n\n+clone-html \"url page\" \"path\": get HTML source in the page.\n\n+darkmode \"on/off\": turn on/off darkmode. After using this command, please restart Kode Studio.\n\n+length \"file path\": get length file.\n\n+release \"kodeapp/webapp\" \"path\": build release your project at Kode Application or Web Application.");
						break;
						case "cls":
						listmap_console.clear();
						listview1.setAdapter(new Listview1Adapter(listmap_console));
						break;
						case "create":
						_CreateFile(Str, list_cmd);
						break;
						case "run":
						_Run(Str, list_cmd);
						break;
						case "open":
						_Open(Str, list_cmd);
						break;
						case "export":
						_Export(Str, list_cmd);
						break;
						case "import":
						_Import(Str, list_cmd);
						break;
						case "delete":
						_Delete(Str, list_cmd);
						break;
						case "copy":
						_Copy(Str, list_cmd);
						break;
						case "cut":
						_Cut(Str, list_cmd);
						break;
						case "host-status":
						_host_status(Str, list_cmd);
						break;
						case "clone-html":
						_CloneHTML(Str, list_cmd);
						break;
						case "darkmode":
						_DarkMode(Str, list_cmd);
						break;
						case "length":
						_Length(Str, list_cmd);
						break;
						case "release":
						_Release(Str, list_cmd);
						break;
						default: 
						_Command(Str, "Command not found. Try @help for get all command can use.");
						break;
					}
				}
			}
		});
		
		_webcall_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _response = _param2;
				_Command("[host-status]", "Tag: ".concat(_tag.concat("Status: ".concat(_response))));
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				_Command("[host-status]", "Tag: ".concat(_tag.concat("\nMessage: ".concat(_message))));
			}
		};
		
		_clone_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _response = _param2;
				FileUtil.writeFile(data.getString("project", "").concat("/".concat(_tag)), _response);
				_Command("[Clone] complete", "");
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				_Command("[Clone]", "Tag: ".concat(_tag.concat("\nMessage Error: ".concat(_message))));
			}
		};
	}
	private void initializeLogic() {
		edittext1.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/console.ttf"), 0);
		setTitle("Console");
		listview1.setTranscriptMode (ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listview1.setStackFromBottom (true);
		listview1.setDivider (null);listview1.setDividerHeight (0);
		_Scroll(false, listview1);
		if (FileUtil.isExistFile(data.getString("project", "").concat("/log.kode"))) {
			mapv = new HashMap<>();
			mapv.put("input", " @log");
			mapv.put("output", FileUtil.readFile(data.getString("project", "").concat("/log.kode")));
			listmap_console.add(mapv);
			listview1.setAdapter(new Listview1Adapter(listmap_console));
		}
		else {
			FileUtil.writeFile(data.getString("project", "").concat("/log.kode"), "----");
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	private void _Command (final String _input, final String _output) {
		mapv = new HashMap<>();
		mapv.put("input", "$".concat(_input));
		mapv.put("output", _output);
		listmap_console.add(mapv);
		listview1.setAdapter(new Listview1Adapter(listmap_console));
	}
	
	
	private void _Zip (final String _source_path, final String _final_path) {
		try {
			java.util.zip.ZipOutputStream os = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(_final_path));
					zip(os, _source_path, null);
					os.close();
		}
		
		catch(java.io.IOException e) {}
	}
	private void zip(java.util.zip.ZipOutputStream os, String filePath, String name) throws java.io.IOException
		{
				java.io.File file = new java.io.File(filePath);
				java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry((name != null ? name + java.io.File.separator : "") + file.getName() + (file.isDirectory() ? java.io.File.separator : ""));
				os.putNextEntry(entry);
				
				if(file.isFile()) {
						java.io.InputStream is = new java.io.FileInputStream(file);
						int size = is.available();
						byte[] buff = new byte[size];
						int len = is.read(buff);
						os.write(buff, 0, len);
						return;
				}
				
				java.io.File[] fileArr = file.listFiles();
				for(java.io.File subFile : fileArr) {
						zip(os, subFile.getAbsolutePath(), entry.getName());
				}
	}
	
	
	private void _Unzip (final String _zip_file_path, final String _destDir) {
		try
		{
			java.io.File outdir = new java.io.File(_destDir);
			java.util.zip.ZipInputStream zin = new java.util.zip.ZipInputStream(new java.io.FileInputStream(_zip_file_path));
			java.util.zip.ZipEntry entry;
			String name, dir;
			while ((entry = zin.getNextEntry()) != null)
			{
				name = entry.getName();
				if(entry.isDirectory())
				{
					mkdirs(outdir, name);
					continue;
				}
				
				/* this part is necessary because file entry can come before
* directory entry where is file located
* i.e.:
* /foo/foo.txt
* /foo/
*/
				
				dir = dirpart(name);
				if(dir != null)
				mkdirs(outdir, dir);
				
				extractFile(zin, outdir, name);
			}
			zin.close();
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
		}
	}
	private static void extractFile(java.util.zip.ZipInputStream in, java.io.File outdir, String name) throws java.io.IOException
	{
		byte[] buffer = new byte[4096];
		java.io.BufferedOutputStream out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(new java.io.File(outdir, name)));
		int count = -1;
		while ((count = in.read(buffer)) != -1)
		out.write(buffer, 0, count);
		out.close();
	}
	
	private static void mkdirs(java.io.File outdir, String path)
	{
		java.io.File d = new java.io.File(outdir, path);
		if(!d.exists())
		d.mkdirs();
	}
	
	private static String dirpart(String name)
	{
		int s = name.lastIndexOf(java.io.File.separatorChar);
		return s == -1 ? null : name.substring(0, s);
	}
	
	
	private void _CreateFile (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 2) {
			FileUtil.writeFile(data.getString("project", "").concat("/".concat(_cmd.get((int)(1)))), "Null");
			_Command(_input, "File created");
		}
		else {
			_Command(_input, "Error");
		}
	}
	
	
	private void _Run (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 3) {
			if (_cmd.get((int)(1)).equals("normal")) {
				if (FileUtil.isExistFile(data.getString("project", "").concat("/".concat(_cmd.get((int)(2)))))) {
					if (!_cmd.get((int)(2)).equals(" ")) {
						_Command(_input, "Start build");
						data.edit().putString("project_run", _cmd.get((int)(2))).commit();
						i_run.setClass(getApplicationContext(), RunActivity.class);
						startActivity(i_run);
					}
					else {
						_Command(_input, "File start not found");
					}
				}
				else {
					_Command(_input, "File start not found");
				}
			}
			else {
				if (_cmd.get((int)(1)).equals("browser")) {
					_Command(_input, "Run in browser complete");
					i_browser.setAction(Intent.ACTION_VIEW);
					i_browser.setData(Uri.parse("file:///".concat(data.getString("project", "")).concat("/".concat(_cmd.get((int)(2))))));
					startActivity(i_browser);
				}
				else {
					if (_cmd.get((int)(1)).equals("localhost")) {
						data.edit().putString("port", _cmd.get((int)(2))).commit();
						i_browser.setClass(getApplicationContext(), BrowserActivity.class);
						startActivity(i_browser);
					}
					else {
						_Command(_input, "Can't run project with this type");
					}
				}
			}
		}
		else {
			_Command(_input, "Error");
		}
	}
	
	
	private void _Open (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 3) {
			if (FileUtil.isExistFile(data.getString("project", "").concat("/".concat(_cmd.get((int)(2)))))) {
				if (_cmd.get((int)(1)).equals("console")) {
					_Command(_input, FileUtil.readFile(data.getString("project", "").concat("/".concat(_cmd.get((int)(2))))));
				}
				else {
					if (_cmd.get((int)(1)).equals("view")) {
						data.edit().putString("file_url", "view-source:file:///".concat(data.getString("project", "").concat("/".concat(_cmd.get((int)(2)))))).commit();
						i_viewcode.setClass(getApplicationContext(), ViewCodeActivity.class);
						startActivity(i_viewcode);
					}
					else {
						if (_cmd.get((int)(1)).equals("editor")) {
							data.edit().putString("file_path", data.getString("project", "").concat("/".concat(_cmd.get((int)(2))))).commit();
							i_editor.setClass(getApplicationContext(), ProjectCodeActivity.class);
							startActivity(i_editor);
						}
						else {
							_Command(_input, "Type open not found");
						}
					}
				}
			}
			else {
				_Command(_input, "file not found");
			}
		}
		else {
			_Command(_input, "Error");
		}
	}
	
	
	private void _Export (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 3) {
			if (FileUtil.isExistFile(_cmd.get((int)(2)))) {
				tg = _cmd.get((int)(2)).concat("/".concat(Uri.parse(data.getString("project", "")).getLastPathSegment().concat(".zip")));
				if (_cmd.get((int)(1)).equals("kode")) {
					_Zip(data.getString("project", ""), tg);
					FileUtil.copyFile(tg, tg.replace(".zip", ".kode"));
					FileUtil.deleteFile(tg);
					_Command(_input, "Export complete at ".concat(tg.replace(".zip", ".kode").concat("")));
				}
				else {
					if (_cmd.get((int)(1)).equals("zip")) {
						_Zip(data.getString("project", ""), tg);
						_Command(_input, "Export complete at ".concat(tg.concat("")));
					}
					else {
						_Command(_input, "Type not found");
					}
				}
			}
			else {
				_Command(_input, "Path not found");
			}
		}
		else {
			_Command(_input, "Error");
		}
	}
	
	
	private void _Import (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 3) {
			if (FileUtil.isExistFile(_cmd.get((int)(1)))) {
				if (_cmd.get((int)(1)).endsWith(".kode")) {
					FileUtil.copyFile(_cmd.get((int)(1)), _cmd.get((int)(1)).replace(".kode", ".zip"));
					_Unzip(_cmd.get((int)(1)).replace(".kode", ".zip"), data.getString("project", "").concat("/".concat(_cmd.get((int)(2)))));
					_Command(_input, "Import compete!");
					FileUtil.deleteFile(_cmd.get((int)(1)).replace(".kode", ".zip"));
				}
				else {
					if (_cmd.get((int)(1)).endsWith(".zip")) {
						_Unzip(_cmd.get((int)(1)), data.getString("project", "").concat("/".concat(_cmd.get((int)(2)))));
						_Command(_input, "Import compete!");
					}
					else {
						_Command(_input, "Error");
					}
				}
			}
			else {
				_Command(_input, "File not found!");
			}
		}
		else {
			_Command(_input, "Error");
		}
	}
	
	
	private void _Delete (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 2) {
			if (FileUtil.isDirectory(data.getString("project", "").concat("/".concat(_cmd.get((int)(1))))) || FileUtil.isFile(data.getString("project", "").concat("/".concat(_cmd.get((int)(1)))))) {
				FileUtil.deleteFile(data.getString("project", "").concat("/".concat(_cmd.get((int)(1)))));
				_Command(_input, "Deleted");
			}
			else {
				_Command(_input, "This path not found!");
			}
		}
		else {
			_Command(_input, "Error");
		}
	}
	
	
	private void _Copy (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 3) {
			if (FileUtil.isDirectory(_cmd.get((int)(1))) || true) {
				FileUtil.copyFile(_cmd.get((int)(1)), data.getString("project", "").concat("/".concat(_cmd.get((int)(2)))));
				_Command(_input, "Copy complete");
			}
			else {
				_Command(_input, "This path not found!");
			}
		}
		else {
			_Command(_input, "Error");
		}
	}
	
	
	private void _Cut (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 3) {
			if (FileUtil.isDirectory(_cmd.get((int)(1))) || FileUtil.isFile(_cmd.get((int)(1)))) {
				FileUtil.moveFile(_cmd.get((int)(1)), data.getString("project", "").concat("/".concat(_cmd.get((int)(2)))));
				_Command(_input, "Move complete");
			}
			else {
				_Command(_input, "This path not found!");
			}
		}
		else {
			_Command(_input, "Error");
		}
	}
	
	
	private void _host_status (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 3) {
			webcall.startRequestNetwork(RequestNetworkController.GET, _cmd.get((int)(1)), _cmd.get((int)(2)), _webcall_request_listener);
			_Command(_input, "Getting...");
		}
		else {
			_Command(_input, "Error");
		}
	}
	
	
	private void _GetCMD (final String _input) {
		n = 0;
		m = 0;
		while(true) {
			if (n < _input.length()) {
				if (_input.substring((int)(n), (int)(n + 1)).equals(" ")) {
					list_cmd.add(_input.substring((int)(m), (int)(n)));
					m = n + 1;
				}
				if ((n == (_input.length() - 1)) && !_input.substring((int)(_input.length() - 1), (int)(_input.length())).equals(" ")) {
					list_cmd.add(_input.substring((int)(m), (int)(_input.length())));
				}
			}
			else {
				break;
			}
			n++;
		}
	}
	
	
	private void _Scroll (final boolean _seb, final ListView _sdisa) {
		_sdisa.setHorizontalScrollBarEnabled(_seb);
		_sdisa.setVerticalScrollBarEnabled(_seb);
	}
	
	
	private void _CloneHTML (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 3) {
			clone.startRequestNetwork(RequestNetworkController.GET, _cmd.get((int)(1)), _cmd.get((int)(2)), _clone_request_listener);
			_Command(_input, "loading..");
		}
		else {
			_Command(_input, "Error");
		}
	}
	
	
	private void _DarkMode (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 2) {
			if (_cmd.get((int)(1)).equals("on")) {
				data.edit().putString("darkmode", "true").commit();
				_Command(_input, "Darkmode turn on");
			}
			else {
				if (_cmd.get((int)(1)).equals("off")) {
					_Command(_input, "Darkmode turn off");
					data.edit().putString("darkmode", "false").commit();
				}
				else {
					_Command(_input, "Error");
				}
			}
		}
		else {
			_Command(_input, "Error");
		}
	}
	
	
	private void _Length (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 2) {
			if (FileUtil.isExistFile(data.getString("project", "").concat("/".concat(_cmd.get((int)(1)))))) {
				if (FileUtil.isDirectory(data.getString("project", "").concat("/".concat(_cmd.get((int)(1))))) || FileUtil.isFile(data.getString("project", "").concat("/".concat(_cmd.get((int)(1)))))) {
					_Command(_input, Uri.parse(data.getString("project", "").concat("/".concat(_cmd.get((int)(1))))).getLastPathSegment().concat(": ".concat(String.valueOf(FileUtil.getFileLength(data.getString("project", "").concat("/".concat(_cmd.get((int)(1)))))).concat(" bytes"))));
				}
				else {
					
				}
			}
			else {
				_Command(_input, "Path not found!");
			}
		}
		else {
			_Command(_input, "Error");
		}
	}
	
	
	private void _Release (final String _input, final ArrayList<String> _cmd) {
		if (_cmd.size() == 3) {
			if (_cmd.get((int)(1)).equals("kodeapp")) {
				if (FileUtil.isExistFile(data.getString("project", "").concat("/type.st"))) {
					if (FileUtil.readFile(data.getString("project", "").concat("/type.st")).substring((int)(0), (int)(5)).equals("type$")) {
						if (FileUtil.readFile(data.getString("project", "").concat("/type.st")).substring((int)(5), (int)(FileUtil.readFile(data.getString("project", "").concat("/type.st")).length())).equals("normal")) {
							if (FileUtil.isExistFile(data.getString("project", "").concat("/run.st"))) {
								if (FileUtil.readFile(data.getString("project", "").concat("/run.st")).substring((int)(0), (int)(4)).equals("run$")) {
									if (FileUtil.isExistFile(data.getString("project", "").concat("/".concat("")))) {
										_Zip(data.getString("project", ""), list_cmd.get((int)(2)).concat("/".concat(Uri.parse(data.getString("project", "")).getLastPathSegment().concat(".zip"))));
										FileUtil.copyFile(list_cmd.get((int)(2)).concat("/".concat(Uri.parse(data.getString("project", "")).getLastPathSegment().concat(".zip"))), list_cmd.get((int)(2)).concat("/".concat(Uri.parse(data.getString("project", "")).getLastPathSegment().concat(".zip"))).replace(".zip", ".kodeapp"));
										FileUtil.deleteFile(list_cmd.get((int)(2)).concat("/".concat(Uri.parse(data.getString("project", "")).getLastPathSegment().concat(".zip"))));
										_Command(_input, "Release complete");
									}
									else {
										
									}
								}
								else {
									
								}
							}
							else {
								
							}
						}
						else {
							_Command(_input, "Error at type.st: Specialy normal");
						}
					}
					else {
						_Command(_input, "Error at type.st");
					}
				}
				else {
					_Command(_input, "File type.st not found!");
				}
			}
			else {
				if (_cmd.get((int)(1)).equals("webapp")) {
					
				}
				else {
					_Command(_input, "Unknow release type. Try using \"kodeapp\" or \"webapp\"");
				}
			}
		}
		else {
			_Command(_input, "Error");
		}
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
			
			textview_output.setClickable(true);
			android.text.util.Linkify.addLinks(textview_output, android.text.util.Linkify.ALL);
			textview_output.setLinkTextColor(Color.parseColor("#009688"));
			textview_output.setLinksClickable(true);
			
			textview_input.setClickable(true);
			android.text.util.Linkify.addLinks(textview_input, android.text.util.Linkify.ALL);
			textview_input.setLinkTextColor(Color.parseColor("#009688"));
			textview_input.setLinksClickable(true);
			textview_input.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/console.ttf"), 1);
			textview_output.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/console.ttf"), 0);
			textview_input.setText(listmap_console.get((int)_position).get("input").toString());
			textview_output.setText(listmap_console.get((int)_position).get("output").toString());
			if (listmap_console.get((int)_position).get("output").toString().equals("Error") || listmap_console.get((int)_position).get("output").toString().equals("Command not found. Try @help for get all command can use.")) {
				textview_output.setTextColor(0xFFFF1744);
			}
			else {
				textview_output.setTextColor(0xFFFFFFFF);
			}
			
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
