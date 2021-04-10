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
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.ClipData;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.AdapterView;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class FolderActivity extends AppCompatActivity {
	
	public final int REQ_CD_PICK = 101;
	private Timer _timer = new Timer();
	
	private Toolbar _toolbar;
	private FloatingActionButton _fab;
	private double n = 0;
	private String path = "";
	private String string = "";
	
	private ArrayList<String> list_path = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> listmap_path = new ArrayList<>();
	private ArrayList<String> list_tree = new ArrayList<>();
	private ArrayList<String> list_tg = new ArrayList<>();
	
	private ListView listview1;
	
	private SharedPreferences data;
	private Intent i_load = new Intent();
	private AlertDialog.Builder dia;
	private AlertDialog.Builder dua;
	private Intent pick = new Intent(Intent.ACTION_GET_CONTENT);
	private AlertDialog.Builder dialog_delete;
	private Intent i_video = new Intent();
	private Intent i_picture = new Intent();
	private Intent i_edit = new Intent();
	private TimerTask timer;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.folder);
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
		data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		dia = new AlertDialog.Builder(this);
		dua = new AlertDialog.Builder(this);
		pick.setType("*/*");
		pick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		dialog_delete = new AlertDialog.Builder(this);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (FileUtil.isDirectory(listmap_path.get((int)_position).get("data").toString())) {
					path = listmap_path.get((int)_position).get("data").toString();
					list_path.clear();
					listmap_path.clear();
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					setTitle(Uri.parse(path).getLastPathSegment());
					FileUtil.listDir(path, list_path);
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
						n++;
					}
				}
				else {
					if (listmap_path.get((int)_position).get("data").toString().endsWith(".mp4")) {
						data.edit().putString("video_path", listmap_path.get((int)_position).get("data").toString()).commit();
						i_video.setClass(getApplicationContext(), VideoActivity.class);
						startActivity(i_video);
					}
					else {
						if (listmap_path.get((int)_position).get("data").toString().endsWith(".mp3")) {
							data.edit().putString("video_path", listmap_path.get((int)_position).get("data").toString()).commit();
							i_video.setClass(getApplicationContext(), VideoActivity.class);
							startActivity(i_video);
						}
						else {
							if (listmap_path.get((int)_position).get("data").toString().endsWith(".jpg")) {
								data.edit().putString("picture_path", listmap_path.get((int)_position).get("data").toString()).commit();
								i_picture.setClass(getApplicationContext(), PictureActivity.class);
								startActivity(i_picture);
							}
							else {
								if (listmap_path.get((int)_position).get("data").toString().endsWith(".png")) {
									data.edit().putString("picture_path", listmap_path.get((int)_position).get("data").toString()).commit();
									i_picture.setClass(getApplicationContext(), PictureActivity.class);
									startActivity(i_picture);
								}
								else {
									data.edit().putString("file_path", listmap_path.get((int)_position).get("data").toString()).commit();
									i_edit.setClass(getApplicationContext(), ProjectCodeActivity.class);
									startActivity(i_edit);
								}
							}
						}
					}
				}
			}
		});
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				dialog_delete.setMessage("Do you want delete ".concat(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment().concat(" ?")));
				dialog_delete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						FileUtil.deleteFile(listmap_path.get((int)_position).get("data").toString());
						list_path.clear();
						listmap_path.clear();
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
						FileUtil.listDir(path, list_path);
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
							n++;
						}
					}
				});
				dialog_delete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dialog_delete.create().show();
				return true;
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dia.setMessage("Create New");
				dia.setPositiveButton("File", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						LinearLayout mylayout = new LinearLayout(FolderActivity.this);
						
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						
						mylayout.setLayoutParams(params); mylayout.setOrientation(LinearLayout.VERTICAL);
						
						final EditText myedittext = new EditText(FolderActivity.this);
						myedittext.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
						 
						mylayout.addView(myedittext);
						dua.setView(mylayout);
						myedittext.setHint("Enter File Name");
						dua.setMessage("Create new file...");
						dua.setPositiveButton("Create", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								string = myedittext.getText().toString ();
								FileUtil.writeFile(path.concat("/".concat(string)), "Code here...");
								SketchwareUtil.showMessage(getApplicationContext(), "Created");
								list_path.clear();
								listmap_path.clear();
								FileUtil.listDir(path, list_path);
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
									n++;
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
						LinearLayout mylayout = new LinearLayout(FolderActivity.this);
						
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						
						mylayout.setLayoutParams(params); mylayout.setOrientation(LinearLayout.VERTICAL);
						
						final EditText myedittext = new EditText(FolderActivity.this);
						myedittext.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
						 
						mylayout.addView(myedittext);
						dua.setView(mylayout);
						myedittext.setHint("Enter Folder Name");
						dua.setMessage("Create new file...");
						dua.setPositiveButton("Create", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								string = myedittext.getText().toString ();
								FileUtil.makeDir(path.concat("/".concat(string)));
								SketchwareUtil.showMessage(getApplicationContext(), "Created");
								list_path.clear();
								listmap_path.clear();
								FileUtil.listDir(path, list_path);
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
									n++;
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
				dia.setNeutralButton("Add", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						startActivityForResult(pick, REQ_CD_PICK);
					}
				});
				dia.create().show();
			}
		});
	}
	private void initializeLogic() {
		setTitle(Uri.parse(data.getString("folder_path", "")).getLastPathSegment());
		FileUtil.listDir(data.getString("folder_path", ""), list_path);
		path = data.getString("folder_path", "");
		list_tree.add((int)(0), data.getString("folder_path", ""));
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
			n++;
		}
		_Darkmode(data, listview1);
		_ReFress();
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_PICK:
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
				n = 0;
				while(true) {
					if (n < _filePath.size()) {
						FileUtil.copyFile(_filePath.get((int)(n)), path.concat("/".concat(Uri.parse(_filePath.get((int)(n))).getLastPathSegment())));
					}
					else {
						break;
					}
					n++;
				}
				list_path.clear();
				listmap_path.clear();
				FileUtil.listDir(path, list_path);
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
					n++;
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
		if (!path.equals(data.getString("folder_path", ""))) {
			path = path.replace("/".concat(Uri.parse(path).getLastPathSegment()), "");
			list_path.clear();
			listmap_path.clear();
			n = 0;
			setTitle(Uri.parse(path).getLastPathSegment());
			FileUtil.listDir(path, list_path);
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
				n++;
			}
		}
		else {
			finish();
		}
	}
	private void _FileType (final String _path, final ImageView _image) {
		if (_path.endsWith(".info")) {
			_image.setImageResource(R.drawable.default_image);
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
	
	
	private void _ReFress () {
		timer = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						FileUtil.listDir(path, list_tg);
						if (!(list_path.size() == list_tg.size())) {
							list_path.clear();
							listmap_path.clear();
							FileUtil.listDir(path, list_path);
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
			
			if (FileUtil.isFile(listmap_path.get((int)_position).get("data").toString())) {
				linear_f.setVisibility(View.GONE);
				linear_file.setVisibility(View.VISIBLE);
				textview_file.setText(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment());
				_FileType(listmap_path.get((int)_position).get("data").toString(), imageview2);
			}
			else {
				linear_f.setVisibility(View.VISIBLE);
				imageview1.setImageResource(R.drawable.folder);
				linear_file.setVisibility(View.GONE);
				textview_f.setText(Uri.parse(listmap_path.get((int)_position).get("data").toString()).getLastPathSegment());
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
