package com.kodestudio.ide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.ScrollView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.content.ClipData;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.AdapterView;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class MainActivity extends AppCompatActivity {
	
	public final int REQ_CD_PICK_PROJECT = 101;
	private Timer _timer = new Timer();
	
	private Toolbar _toolbar;
	private FloatingActionButton _fab;
	private DrawerLayout _drawer;
	private double n = 0;
	private String path = "";
	private String string_filename = "";
	
	private ArrayList<String> list_path = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> listmap_path = new ArrayList<>();
	private ArrayList<String> list_tg = new ArrayList<>();
	
	private ScrollView vscroll2;
	private ListView listview1;
	private LinearLayout linear1;
	private ImageView imageview2;
	private TextView textview1;
	private LinearLayout linear2;
	private Button button1;
	private LinearLayout linear3;
	private Button button2;
	private LinearLayout linear4;
	private Button button5;
	private LinearLayout linear6;
	private Button button3;
	private LinearLayout linear5;
	private Button button4;
	
	private SharedPreferences data;
	private Intent i_code = new Intent();
	private AlertDialog.Builder dialog;
	private Intent i_file = new Intent();
	private AlertDialog.Builder dua;
	private Intent i_project = new Intent();
	private AlertDialog.Builder dialog_remove;
	private Calendar time = Calendar.getInstance();
	private Intent i_newproject = new Intent();
	private Intent pick_project = new Intent(Intent.ACTION_GET_CONTENT);
	private AlertDialog.Builder dia_type;
	private TimerTask timer;
	private AlertDialog.Builder dia_add;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
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
		_fab = (FloatingActionButton) findViewById(R.id._fab);
		
		_drawer = (DrawerLayout) findViewById(R.id._drawer);ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(MainActivity.this, _drawer, _toolbar, R.string.app_name, R.string.app_name);
		_drawer.addDrawerListener(_toggle);
		_toggle.syncState();
		
		LinearLayout _nav_view = (LinearLayout) findViewById(R.id._nav_view);
		
		vscroll2 = (ScrollView) findViewById(R.id.vscroll2);
		listview1 = (ListView) findViewById(R.id.listview1);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		imageview2 = (ImageView) findViewById(R.id.imageview2);
		textview1 = (TextView) findViewById(R.id.textview1);
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		button1 = (Button) findViewById(R.id.button1);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		button2 = (Button) findViewById(R.id.button2);
		linear4 = (LinearLayout) findViewById(R.id.linear4);
		button5 = (Button) findViewById(R.id.button5);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		button3 = (Button) findViewById(R.id.button3);
		linear5 = (LinearLayout) findViewById(R.id.linear5);
		button4 = (Button) findViewById(R.id.button4);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		dialog = new AlertDialog.Builder(this);
		dua = new AlertDialog.Builder(this);
		dialog_remove = new AlertDialog.Builder(this);
		pick_project.setType("*/*");
		pick_project.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		dia_type = new AlertDialog.Builder(this);
		dia_add = new AlertDialog.Builder(this);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (FileUtil.isFile(listmap_path.get((int)_position).get("data").toString())) {
					data.edit().putString("file_path", listmap_path.get((int)_position).get("data").toString()).commit();
					i_code.setClass(getApplicationContext(), CodeActivity.class);
					startActivity(i_code);
				}
				else {
					if (FileUtil.isExistFile(listmap_path.get((int)_position).get("data").toString().concat("/log.kode"))) {
						data.edit().putString("project", listmap_path.get((int)_position).get("data").toString()).commit();
						i_project.setClass(getApplicationContext(), ProjectActivity.class);
						startActivity(i_project);
					}
					else {
						dia_add.setMessage("\"log.kode\" not found in this project");
						dia_add.setPositiveButton("Add log.kode", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								FileUtil.writeFile(listmap_path.get((int)_position).get("data").toString().concat("/log.kode"), "Log.kode");
								data.edit().putString("project", listmap_path.get((int)_position).get("data").toString()).commit();
								i_project.setClass(getApplicationContext(), ProjectActivity.class);
								startActivity(i_project);
							}
						});
						dia_add.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								
							}
						});
						dia_add.create().show();
					}
				}
			}
		});
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				dialog_remove.setMessage(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment());
				dialog_remove.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						FileUtil.deleteFile(listmap_path.get((int)_position).get("data").toString());
						SketchwareUtil.showMessage(getApplicationContext(), "Deleted");
						list_path.clear();
						listmap_path.clear();
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
						FileUtil.listDir(FileUtil.getExternalStorageDir().concat("/".concat(".kodestudio")), list_path);
						path = FileUtil.getExternalStorageDir().concat("/".concat(".kodestudio"));
						n = 0;
						while(true) {
							if (n < list_path.size()) {
								{
									HashMap<String, Object> _item = new HashMap<>();
									_item.put("data", list_path.get((int)(n)));
									listmap_path.add(_item);
								}
								
								listview1.setAdapter(new Listview1Adapter(listmap_path));
							}
							else {
								break;
							}
							n = n + 1;
						}
					}
				});
				if (FileUtil.isDirectory(listmap_path.get((int)_position).get("data").toString())) {
					dialog_remove.setNegativeButton("Export", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							dia_type.setMessage(".kode or .zip?");
							dia_type.setPositiveButton(".kode", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									_Zip(listmap_path.get((int)_position).get("data").toString(), FileUtil.getExternalStorageDir().concat("/KodeStudio/Export/".concat(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment().concat(".zip"))));
									FileUtil.copyFile(FileUtil.getExternalStorageDir().concat("/KodeStudio/Export/".concat(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment().concat(".zip"))), FileUtil.getExternalStorageDir().concat("/KodeStudio/Export/".concat(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment().concat(".kode"))));
									FileUtil.deleteFile(FileUtil.getExternalStorageDir().concat("/KodeStudio/Export/".concat(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment().concat(".zip"))));
									SketchwareUtil.showMessage(getApplicationContext(), "Exported ");
								}
							});
							dia_type.setNegativeButton(".zip", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									_Zip(listmap_path.get((int)_position).get("data").toString(), FileUtil.getExternalStorageDir().concat("/KodeStudio/Export/".concat(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment().concat(".zip"))));
									SketchwareUtil.showMessage(getApplicationContext(), "Exported ");
								}
							});
							dia_type.create().show();
						}
					});
				}
				dialog_remove.create().show();
				return true;
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				LinearLayout mylayout = new LinearLayout(MainActivity.this);
				
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				
				mylayout.setLayoutParams(params); mylayout.setOrientation(LinearLayout.VERTICAL);
				
				final EditText myedittext = new EditText(MainActivity.this);
				myedittext.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
				 
				mylayout.addView(myedittext);
				dua.setView(mylayout);
				myedittext.setHint("Enter File Name");
				dua.setMessage("Create new file...");
				dua.setPositiveButton("Create", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						string_filename = myedittext.getText().toString ();
						FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(string_filename)), "<h1>Hello World</h1>");
						list_path.clear();
						listmap_path.clear();
						FileUtil.listDir(FileUtil.getExternalStorageDir().concat("/.kodestudio"), list_path);
						n = 0;
						while(true) {
							if (n < list_path.size()) {
								{
									HashMap<String, Object> _item = new HashMap<>();
									_item.put("data", list_path.get((int)(n)));
									listmap_path.add(_item);
								}
								
								listview1.setAdapter(new Listview1Adapter(listmap_path));
							}
							else {
								break;
							}
							n = n + 1;
						}
						SketchwareUtil.showMessage(getApplicationContext(), "Complete!");
					}
				});
				dua.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dua.create().show();
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i_newproject.setClass(getApplicationContext(), NewprojectActivity.class);
				startActivity(i_newproject);
			}
		});
		
		button5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(pick_project, REQ_CD_PICK_PROJECT);
			}
		});
		
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_drawer.openDrawer(GravityCompat.START);
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dialog.setMessage("New");
				dialog.setPositiveButton("File", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						LinearLayout mylayout = new LinearLayout(MainActivity.this);
						
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						
						mylayout.setLayoutParams(params); mylayout.setOrientation(LinearLayout.VERTICAL);
						
						final EditText myedittext = new EditText(MainActivity.this);
						myedittext.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
						 
						mylayout.addView(myedittext);
						dua.setView(mylayout);
						myedittext.setHint("Enter File Name");
						dua.setMessage("Create new file...");
						dua.setPositiveButton("Create", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								string_filename = myedittext.getText().toString ();
								FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/.kodestudio/".concat(string_filename)), "<h1>Hello World</h1>");
								list_path.clear();
								listmap_path.clear();
								FileUtil.listDir(FileUtil.getExternalStorageDir().concat("/.kodestudio"), list_path);
								n = 0;
								while(true) {
									if (n < list_path.size()) {
										{
											HashMap<String, Object> _item = new HashMap<>();
											_item.put("data", list_path.get((int)(n)));
											listmap_path.add(_item);
										}
										
										listview1.setAdapter(new Listview1Adapter(listmap_path));
									}
									else {
										break;
									}
									n = n + 1;
								}
								SketchwareUtil.showMessage(getApplicationContext(), "Complete!");
							}
						});
						dua.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								
							}
						});
						dua.create().show();
					}
				});
				dialog.setNegativeButton("Project", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						i_newproject.setClass(getApplicationContext(), NewprojectActivity.class);
						startActivity(i_newproject);
					}
				});
				dialog.setNeutralButton("Import", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						startActivityForResult(pick_project, REQ_CD_PICK_PROJECT);
					}
				});
				dialog.create().show();
			}
		});
	}
	private void initializeLogic() {
		vscroll2.setVisibility(View.GONE);
		_setStyle("#8BC34A", 360, 0, button1);
		_setStyle("#8BC34A", 360, 0, button2);
		_setStyle("#8BC34A", 360, 0, button3);
		_setStyle("#8BC34A", 360, 0, button4);
		_setStyle("#8BC34A", 360, 0, button5);
		FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/".concat(".kodestudio")));
		FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/".concat("KodeStudio")));
		FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/".concat("KodeStudio/Export")));
		FileUtil.makeDir(FileUtil.getExternalStorageDir().concat("/".concat("KodeStudio/Cache")));
		FileUtil.listDir(FileUtil.getExternalStorageDir().concat("/".concat(".kodestudio")), list_path);
		path = FileUtil.getExternalStorageDir().concat("/".concat(".kodestudio"));
		n = 0;
		while(true) {
			if (n < list_path.size()) {
				{
					HashMap<String, Object> _item = new HashMap<>();
					_item.put("data", list_path.get((int)(n)));
					listmap_path.add(_item);
				}
				
				listview1.setAdapter(new Listview1Adapter(listmap_path));
			}
			else {
				break;
			}
			n = n + 1;
		}
		_Darkmode(data, listview1);
		timer = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						FileUtil.listDir(FileUtil.getExternalStorageDir().concat("/.kodestudio"), list_tg);
						if (!(list_tg.size() == listmap_path.size())) {
							list_path.clear();
							listmap_path.clear();
							FileUtil.listDir(FileUtil.getExternalStorageDir().concat("/".concat(".kodestudio")), list_path);
							path = FileUtil.getExternalStorageDir().concat("/".concat(".kodestudio"));
							n = 0;
							while(true) {
								if (n < list_path.size()) {
									{
										HashMap<String, Object> _item = new HashMap<>();
										_item.put("data", list_path.get((int)(n)));
										listmap_path.add(_item);
									}
									
									listview1.setAdapter(new Listview1Adapter(listmap_path));
								}
								else {
									break;
								}
								n = n + 1;
							}
						}
						if (listmap_path.size() == 0) {
							vscroll2.setVisibility(View.VISIBLE);
						}
						else {
							vscroll2.setVisibility(View.GONE);
						}
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(timer, (int)(250), (int)(250));
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_PICK_PROJECT:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				if (_filePath.get((int)(0)).endsWith(".kode")) {
					FileUtil.copyFile(_filePath.get((int)(0)), _filePath.get((int)(0)).replace(".kode", ".zip"));
					_Unzip(_filePath.get((int)(0)).replace(".kode", ".zip"), FileUtil.getExternalStorageDir().concat("/.kodestudio"));
					FileUtil.deleteFile(_filePath.get((int)(0)).replace(".kode", ".zip"));
					SketchwareUtil.showMessage(getApplicationContext(), "Complete");
				}
				else {
					if (_filePath.get((int)(0)).endsWith(".zip")) {
						_Unzip(_filePath.get((int)(0)).replace(".kode", ".zip"), FileUtil.getExternalStorageDir().concat("/.kodestudio"));
						SketchwareUtil.showMessage(getApplicationContext(), "Complete");
					}
					else {
						SketchwareUtil.showMessage(getApplicationContext(), "Only Zip or Kode File");
					}
				}
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (_drawer.isDrawerOpen(GravityCompat.START)) {
			_drawer.closeDrawer(GravityCompat.START);
		}
		else {
			super.onBackPressed();
		}
	}
	private void _WriteError (final String _path, final String _type, final String _des) {
		FileUtil.writeFile(_path.concat("/log.kode"), FileUtil.readFile(_path.concat("/log.kode")).concat("\n\n$[".concat(_type.concat("]- ".concat(_des.concat(""))))));
	}
	
	
	private void _Darkmode (final SharedPreferences _db, final View _view) {
		if (_db.getString("darkmode", "").equals("true")) {
			_view.setBackgroundColor(0xFF000000);
		}
	}
	
	
	private void _DartText (final SharedPreferences _db, final TextView _tv) {
		if (_db.getString("darkmode", "").equals("true")) {
			_tv.setTextColor(0xFFFFFFFF);
		}
	}
	
	
	private void _Zip (final String _source_path, final String _final_path) {
		FileUtil.writeFile("Don't remove it.\nDesigned : SKETCHit\nEditor: Manish Nirmal", "This Block Added for Storage Permission");
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
	
	
	private void _setStyle (final String _color, final double _numb, final double _number, final View _view) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();gd.setColor(Color.parseColor(_color));gd.setCornerRadius((int)_numb);_view.setBackground(gd);
		
		_view.setElevation((int)_number);
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
				_v = _inflater.inflate(R.layout.cus_file, null);
			}
			
			final LinearLayout linear_f = (LinearLayout) _v.findViewById(R.id.linear_f);
			final LinearLayout linear_file = (LinearLayout) _v.findViewById(R.id.linear_file);
			final ImageView imageview1 = (ImageView) _v.findViewById(R.id.imageview1);
			final TextView textview_f = (TextView) _v.findViewById(R.id.textview_f);
			final ImageView imageview2 = (ImageView) _v.findViewById(R.id.imageview2);
			final TextView textview_file = (TextView) _v.findViewById(R.id.textview_file);
			
			if (FileUtil.isDirectory(listmap_path.get((int)_position).get("data").toString())) {
				linear_f.setVisibility(View.VISIBLE);
				linear_file.setVisibility(View.GONE);
				textview_f.setText(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment());
				if (FileUtil.isExistFile(listmap_path.get((int)_position).get("data").toString().concat("/log.kode"))) {
					imageview1.setImageResource(R.drawable.officematerial);
				}
				else {
					imageview1.setImageResource(R.drawable.nokode);
				}
			}
			else {
				linear_f.setVisibility(View.GONE);
				linear_file.setVisibility(View.VISIBLE);
				textview_file.setText(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment());
			}
			_Darkmode(data, linear_f);
			_Darkmode(data, linear_file);
			_DartText(data, textview_file);
			_DartText(data, textview_f);
			
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
