package com.kodestudio.ide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import android.widget.Button;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.AdapterView;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class ProjectActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private Toolbar _toolbar;
	private FloatingActionButton _fab;
	private double n = 0;
	private String more = "";
	private String string = "";
	private String string_run = "";
	private double dem = 0;
	private double moc = 0;
	private String str_run = "";
	private HashMap<String, Object> map_run = new HashMap<>();
	private HashMap<String, Object> map_type = new HashMap<>();
	
	private ArrayList<String> list_path = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> listmap_path = new ArrayList<>();
	private ArrayList<String> list_command = new ArrayList<>();
	private ArrayList<String> list_tg = new ArrayList<>();
	
	private ListView listview1;
	private Button button_run;
	
	private SharedPreferences data;
	private Intent intent_edit = new Intent();
	private AlertDialog.Builder dia;
	private AlertDialog.Builder dua;
	private Intent i_run = new Intent();
	private Calendar cal = Calendar.getInstance();
	private Intent i_console = new Intent();
	private Intent i_folder = new Intent();
	private Intent i_gohome = new Intent();
	private TimerTask timer;
	private Intent i_edit = new Intent();
	private AlertDialog.Builder dia_editor;
	private AlertDialog.Builder dia_remove;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.project);
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
		
		listview1 = (ListView) findViewById(R.id.listview1);
		button_run = (Button) findViewById(R.id.button_run);
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		dia = new AlertDialog.Builder(this);
		dua = new AlertDialog.Builder(this);
		dia_editor = new AlertDialog.Builder(this);
		dia_remove = new AlertDialog.Builder(this);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (FileUtil.isFile(listmap_path.get((int)_position).get("data").toString())) {
					if (Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment().equals("log.kode")) {
						i_console.setClass(getApplicationContext(), ConsoleActivity.class);
						startActivity(i_console);
					}
					else {
						if (Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment().equals("manifest.kode") || Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment().equals("type.st")) {
							dia_editor.setMessage("Choose editor you want use?");
							dia_editor.setPositiveButton("Visual", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									data.edit().putString("json_path", listmap_path.get((int)_position).get("data").toString()).commit();
									i_edit.setClass(getApplicationContext(), JsonEditActivity.class);
									startActivity(i_edit);
								}
							});
							dia_editor.setNegativeButton("Text", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									data.edit().putString("file_path", listmap_path.get((int)_position).get("data").toString()).commit();
									intent_edit.setClass(getApplicationContext(), ProjectCodeActivity.class);
									startActivity(intent_edit);
								}
							});
							dia_editor.create().show();
						}
						else {
							data.edit().putString("file_path", listmap_path.get((int)_position).get("data").toString()).commit();
							intent_edit.setClass(getApplicationContext(), ProjectCodeActivity.class);
							startActivity(intent_edit);
						}
					}
				}
				else {
					data.edit().putString("folder_path", listmap_path.get((int)_position).get("data").toString()).commit();
					i_folder.setClass(getApplicationContext(), FolderActivity.class);
					startActivity(i_folder);
				}
			}
		});
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				dia_remove.setMessage("Do you want to remove ".concat(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment().concat("?")));
				dia_remove.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						FileUtil.deleteFile(listmap_path.get((int)_position).get("data").toString());
					}
				});
				dia_remove.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dia_remove.create().show();
				return true;
			}
		});
		
		button_run.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_WriteError(data.getString("project", ""), "StartBuild", "");
				if (FileUtil.isExistFile(data.getString("project", "").concat("/".concat("manifest.kode")))) {
					map_run = new Gson().fromJson(FileUtil.readFile(data.getString("project", "").concat("/".concat("manifest.kode"))), new TypeToken<HashMap<String, Object>>(){}.getType());
					if (map_run.containsKey("package")) {
						if (map_run.get("package").toString().equals("com.kodestudio.ide")) {
							if (map_run.containsKey("startpage")) {
								if (FileUtil.isExistFile(data.getString("project", "").concat("/".concat(map_run.get("startpage").toString())))) {
									data.edit().putString("project_run", map_run.get("startpage").toString()).commit();
									i_run.setClass(getApplicationContext(), RunActivity.class);
									startActivity(i_run);
									_WriteError(data.getString("project", ""), "CompleteBuild", "");
								}
								else {
									_WriteError(data.getString("project", ""), "BuildError", "File start not found");
									_createSnackBar("File start not found!");
								}
							}
							else {
								_WriteError(data.getString("project", ""), "BuildError", "Keywork \"startpage\" not found in \"config.st\"");
								_createSnackBar("Keywork \"startpage\" not found in \"manifest.kode\"");
							}
						}
						else {
							if (map_run.get("package").toString().equals("com.kodestudio.server")) {
								if (map_run.containsKey("port")) {
									data.edit().putString("port", map_run.get("port").toString()).commit();
									i_run.setClass(getApplicationContext(), BrowserActivity.class);
									startActivity(i_run);
									_WriteError(data.getString("project", ""), "CompleteBuild", "...");
								}
								else {
									_WriteError(data.getString("project", ""), "BuildError", "Keywork \"port\" not found in \"manifest.kode\"");
									_createSnackBar("Keywork \"port\" not found in \"manifest.kode\"");
								}
							}
							else {
								
							}
						}
					}
					else {
						_WriteError(data.getString("project", ""), "BuildError", "Keywork \"package\" not found in \"manifest.kode\"");
						_createSnackBar("Keywork \"package\" not found in \"manifest.kode\"");
					}
				}
				else {
					_WriteError(data.getString("project", ""), "BuildError", "File \"manifest.kode\" not found");
					_createSnackBar("File \"manifest.kode\" not found");
				}
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				cal = Calendar.getInstance();
				dia.setMessage("Create New");
				dia.setPositiveButton("File", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						LinearLayout mylayout = new LinearLayout(ProjectActivity.this);
						
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						
						mylayout.setLayoutParams(params); mylayout.setOrientation(LinearLayout.VERTICAL);
						
						final EditText myedittext = new EditText(ProjectActivity.this);
						myedittext.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
						 
						mylayout.addView(myedittext);
						dua.setView(mylayout);
						myedittext.setHint("Enter File Name");
						dua.setMessage("Create new file...");
						dua.setPositiveButton("Create", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								string = myedittext.getText().toString ();
								FileUtil.writeFile(data.getString("project", "").concat("/".concat(string)), "{}");
								SketchwareUtil.showMessage(getApplicationContext(), "Created");
								_WriteError(data.getString("project", ""), "New File", string.concat(""));
								list_path.clear();
								listmap_path.clear();
								FileUtil.listDir(data.getString("project", ""), list_path);
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
						dua.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								
							}
						});
						dua.create().show();
					}
				});
				dia.setNegativeButton("Folder", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						LinearLayout mylayout = new LinearLayout(ProjectActivity.this);
						
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						
						mylayout.setLayoutParams(params); mylayout.setOrientation(LinearLayout.VERTICAL);
						
						final EditText myedittext = new EditText(ProjectActivity.this);
						myedittext.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
						 
						mylayout.addView(myedittext);
						dua.setView(mylayout);
						myedittext.setHint("Enter Folder Name");
						dua.setMessage("Create new file...");
						dua.setPositiveButton("Create", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								string = myedittext.getText().toString ();
								FileUtil.makeDir(data.getString("project", "").concat("/".concat(string)));
								SketchwareUtil.showMessage(getApplicationContext(), "Created");
								_WriteError(data.getString("project", ""), "New Folder", string);
								list_path.clear();
								listmap_path.clear();
								FileUtil.listDir(data.getString("project", ""), list_path);
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
						dua.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								
							}
						});
						dua.create().show();
					}
				});
				dia.create().show();
			}
		});
	}
	private void initializeLogic() {
		setTitle(Uri.parse(data.getString("project", "")).getLastPathSegment());
		FileUtil.listDir(data.getString("project", ""), list_path);
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
						FileUtil.listDir(data.getString("project", ""), list_tg);
						if (!(list_tg.size() == list_path.size())) {
							list_path.clear();
							listmap_path.clear();
							FileUtil.listDir(data.getString("project", ""), list_path);
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
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(timer, (int)(500), (int)(500));
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
		i_gohome.setClass(getApplicationContext(), MainActivity.class);
		startActivity(i_gohome);
		finish();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		list_path.clear();
		listmap_path.clear();
		FileUtil.listDir(data.getString("project", ""), list_path);
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
	}
	private void _Type (final String _string, final ImageView _images) {
		if (_string.equals(".html")) {
			_images.setImageResource(R.drawable.html);
		}
		else {
			if (_string.equals(".css")) {
				_images.setImageResource(R.drawable.css);
			}
			else {
				if (_string.equals(".js")) {
					_images.setImageResource(R.drawable.javascript);
				}
				else {
					if (_string.equals(".json")) {
						_images.setImageResource(R.drawable.jsonfile);
					}
					else {
						if (_string.equals(".java")) {
							_images.setImageResource(R.drawable.java);
						}
						else {
							if (_string.equals(".py")) {
								_images.setImageResource(R.drawable.python);
							}
							else {
								if (_string.equals(".rb")) {
									_images.setImageResource(R.drawable.ruby);
								}
								else {
									if (_string.equals(".st")) {
										_images.setImageResource(R.drawable.settings);
									}
									else {
										if (_string.equals(".md")) {
											_images.setImageResource(R.drawable.books);
										}
										else {
											if (_string.equals(".sql")) {
												_images.setImageResource(R.drawable.database);
											}
											else {
												if (_string.equals(".info")) {
													_images.setImageResource(R.drawable.books);
												}
												else {
													if (_string.equals(".wide")) {
														_images.setImageResource(R.drawable.books);
													}
													else {
														if (_string.equals(".txt")) {
															_images.setImageResource(R.drawable.txts);
														}
														else {
															if (_string.equals(".xml")) {
																_images.setImageResource(R.drawable.xmls);
															}
															else {
																if (_string.equals(".iso")) {
																	_images.setImageResource(R.drawable.iso);
																}
																else {
																	if (_string.equals(".pdf")) {
																		_images.setImageResource(R.drawable.pdf);
																	}
																	else {
																		if (_string.equals(".mp3")) {
																			_images.setImageResource(R.drawable.mp3);
																		}
																		else {
																			_images.setImageResource(R.drawable.file);
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	private void _createSnackBar (final String _message) {
		ViewGroup parentLayout = (ViewGroup) ((ViewGroup) this .findViewById(android.R.id.content)).getChildAt(0);
		   android.support.design.widget.Snackbar.make(parentLayout, _message, android.support.design.widget.Snackbar.LENGTH_LONG) 
		        .setAction("View log.kode", new View.OnClickListener() {
			            @Override 
			            public void onClick(View view) {
				
				           
				i_console.setClass(getApplicationContext(), ConsoleActivity.class);
				startActivity(i_console);
				 } 
			        }).show();
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
	
	
	private void _FileType (final String _path, final ImageView _image) {
		if (_path.endsWith(".info")) {
			_image.setImageResource(R.drawable.files);
		}
		else {
			if (_path.endsWith(".st")) {
				_image.setImageResource(R.drawable.settings);
			}
			else {
				if (_path.endsWith(".rb")) {
					_image.setImageResource(R.drawable.ruby);
				}
				else {
					if (_path.endsWith(".py")) {
						_image.setImageResource(R.drawable.python);
					}
					else {
						if (_path.endsWith(".java")) {
							_image.setImageResource(R.drawable.java);
						}
						else {
							if (_path.endsWith(".json")) {
								_image.setImageResource(R.drawable.jsonfile);
							}
							else {
								if (_path.endsWith(".js")) {
									_image.setImageResource(R.drawable.javascript);
								}
								else {
									if (_path.endsWith(".css")) {
										_image.setImageResource(R.drawable.css);
									}
									else {
										if (_path.endsWith(".html")) {
											_image.setImageResource(R.drawable.html);
										}
										else {
											if (_path.endsWith(".sql")) {
												_image.setImageResource(R.drawable.database);
											}
											else {
												if (_path.endsWith(".zip")) {
													_image.setImageResource(R.drawable.zip_1);
												}
												else {
													if (_path.endsWith(".png")) {
														_image.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(_path, 1024, 1024));
													}
													else {
														if (_path.endsWith(".jpg")) {
															_image.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(_path, 1024, 1024));
														}
														else {
															if (_path.endsWith(".mp4")) {
																_image.setImageResource(R.drawable.videoplayer);
															}
															else {
																if (_path.endsWith(".mp3")) {
																	_image.setImageResource(R.drawable.mp3);
																}
																else {
																	if (_path.endsWith(".txt")) {
																		_image.setImageResource(R.drawable.txts);
																	}
																	else {
																		if (_path.endsWith(".xml")) {
																			_image.setImageResource(R.drawable.xmls);
																		}
																		else {
																			if (_path.endsWith(".pdf")) {
																				_image.setImageResource(R.drawable.pdf);
																			}
																			else {
																				if (_path.endsWith(".iso")) {
																					_image.setImageResource(R.drawable.iso);
																				}
																				else {
																					if (_path.endsWith(".kode")) {
																						_image.setImageResource(R.drawable.logo);
																					}
																					else {
																						_image.setImageResource(R.drawable.file);
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
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
				imageview1.setImageResource(R.drawable.folder);
				linear_file.setVisibility(View.GONE);
				textview_f.setText(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment());
			}
			else {
				linear_f.setVisibility(View.GONE);
				linear_file.setVisibility(View.VISIBLE);
				textview_file.setText(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment());
				_FileType(listmap_path.get((int)_position).get("data").toString(), imageview2);
			}
			_Darkmode(data, linear_f);
			_Darkmode(data, linear_file);
			_DartText(data, textview_f);
			_DartText(data, textview_file);
			
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
