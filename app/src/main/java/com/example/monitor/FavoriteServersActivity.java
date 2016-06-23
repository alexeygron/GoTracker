package com.lotr.steammonitor.app;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.github.koraktor.steamcondenser.steam.sockets.SteamSocket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// библиотеки admob

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FavoriteServersActivity extends Activity implements OnClickListener{

	DBHelper dbHelper;
	Button mTestBut, mNavHome, mNavFavorite, mNavServers;
	ImageButton  mButtonAdd, mUpBut;
	TextView mTitle;
	String cdv;
	Cursor c;
	SQLiteDatabase db;
	ListView lServers;
	SrvAdapter adapter;
	Server[] masSrv;
	AlertDialog dialog;

	FragmentManager fm;
	Fragment fragment;
	Fragment check;

	// comm
	final String LOG_TAG = "лог БД";

	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favoriteservers_activity);

	// кнопки навигации
		mNavHome = (Button)findViewById(R.id.nav_home_but);
		mNavHome.setOnClickListener(this);
		mNavFavorite = (Button)findViewById(R.id.nav_favorite_but);
		mNavFavorite.setOnClickListener(this);
		mNavServers = (Button)findViewById(R.id.nav_servers_but);
		mNavServers.setOnClickListener(this);

		lServers = (ListView) findViewById(R.id.lServers);
		mUpBut = (ImageButton)findViewById(R.id.update_list);
		mUpBut.setOnClickListener(this);
		mButtonAdd = (ImageButton)findViewById(R.id.btn_add_ip);
		mButtonAdd.setOnClickListener(this);
		//mIpAdr = (EditText)findViewById(R.id.editText1);
		
		mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText(R.string.fav_srv_text);

		// подключения рекламного блока admob
		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		/*AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("0A596F7E158C70A3174EF291FCDEF0AC").build();
		mAdView.loadAd(adRequest);*/

		// создаем объект для создания и управления версиями БД
	    dbHelper = new DBHelper(this, "favorites");
	    
	    //delp("11111:333");
		// получение объекта FragmentManager
		fm = getFragmentManager();
		fragment = fm.findFragmentById(R.id.fragmentContainer);
		check = fm.findFragmentById(R.id.fragmentContainer2);

		// проверка наличия интернет соединения выполняется через метод isNetworkConnected()
		if (isNetworkConnected() == true){
			lServers.setVisibility(View.VISIBLE);
			createServersList();
			updateServerList();

			if (fragment != null) {
					fm.beginTransaction().hide(fragment).commit();
			}
				if (masSrv.length == 0) {
					check = new CheckFragment();
					fm.beginTransaction().add(R.id.fragmentContainer2, check).commit();

				}

		} else {
			lServers.setVisibility(View.INVISIBLE);
			createServersList();
			// транзакция добавляющая фрагмент в менаджер
			// дословно читается как: Создать новую транзакцию фрагмента, включить в нее одну операцию add , а затем закрепить
			if (fragment == null) {
				fragment = new MessageFragment();
				fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
			}
		}

	}

	@Override
	public void onClick(View v) {
		final Intent g;
		switch(v.getId()){
			case R.id.nav_home_but:
				g = new Intent(FavoriteServersActivity.this,  GameInfoActivity.class);
				startActivity(g);
				break;
			case R.id.nav_servers_but:
				g = new Intent(FavoriteServersActivity.this,  FavoritePlayersActivity.class);
				startActivity(g);
				break;
		 	case R.id.btn_add_ip:
				if (isNetworkConnected() == true){
		    		addIpDialog();
				} else {
					Toast.makeText(this, R.string.error_network_text, Toast.LENGTH_SHORT).show();
				}
		        break;
		    case R.id.update_list:
				// проверка наличия интернет соединения выполняется через метод isNetworkConnected()
				if (isNetworkConnected() == true){
					lServers.setVisibility(View.VISIBLE);
					updateServerList();
					Toast.makeText(this, R.string.toast_update_server_list, Toast.LENGTH_SHORT).show();
					if (fragment != null) {
						fm.beginTransaction().hide(fragment).commit();
					}
				} else {

					Toast.makeText(this, R.string.error_network_text, Toast.LENGTH_SHORT).show();
					lServers.setVisibility(View.INVISIBLE);
					// транзакция добавляющая фрагмент в менаджер
					// дословно читается как: Создать новую транзакцию фрагмента, включить в нее одну операцию add , а затем закрепить
					if (fragment == null) {
						fragment = new MessageFragment();
						fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
					} else {
						fm.beginTransaction().show(fragment).commit();

					}
				}

				break;

		    } 
		 }
		   
	// метод в котором происходит создание массива объектов и инициалиция их адресами серверов из базы
	void createServersList () {
	
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		Cursor c = db.query("favorites", null, null, null, null, null, null);
		
        Log.d("количество столбцов", " " + c.getCount());
        
        // массив объектов в полях которых получается и хранится информация о сервере
		masSrv = new Server[c.getCount()];
		int i = 0;
		
		if (c.moveToFirst()) {
			int ipColIndex = c.getColumnIndex("ip");

	        	do {
		        	  // получаем значения по номерам столбцов и пишем все в лог
		              Log.d("Добавление в массив", " " + "ip = " + c.getString(ipColIndex));
		              
		              // запись в объект ip из базы
		              masSrv[i] = new Server(c.getString(ipColIndex));
		              i = i + 1;
		             
		              // переход на следующую строку 
		              // а если следующей нет (текущая - последняя), то false - выходим из цикла
		              
		            } while (c.moveToNext());
		          
		} else
			Log.d(LOG_TAG, "0 строк");
	        
			c.close();
			dbHelper.close();

			// цикл выводящий лог с добавляемыми адресами
			//for (i = 0; i < masSrv.length; i++) {
				
				//Log.d("Значение поля адреса " + i, masSrv[i].getIpAddr());
				
			//}
			adapter = new SrvAdapter(masSrv);
			lServers.setAdapter(adapter);
		}

	// этот метод обновляет поля в объектов созданного выше массива
	void updateServerList(){

		adapter = new SrvAdapter(masSrv);

	    for (int i = 0; i < masSrv.length; i++){
	    	NewTask nt = new NewTask();
	    	nt.execute(i);
	    }

	}
	
	// этот метод проверяет наличие соединения с интернетом
	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();

		if (ni == null) {
			return false;
		} else {
			return true;
		}
	}
	
	// вспомогательный метод удалающий из бд запись по полю ip
	private void delServer(int pos) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
	      // удаляем записи
		  String ipDel = masSrv[pos].getIpAddr();
	      int clearCount = db.delete("favorites", "ip" + "='" + ipDel + "'", null);

	      dbHelper.close();
	      createServersList();
	      updateServerList();
	}
	
	// Метод вызывающий диалог ввода IP
	private void addIpDialog(){
		
    	AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteServersActivity.this);

    	builder.setTitle(R.string.add_server_title);

        LayoutInflater inflater = FavoriteServersActivity.this.getLayoutInflater();
        
        View layout = inflater.inflate(R.layout.add_ip_dialog,null);       
        builder.setView(layout);
        
        final EditText usernameInput = (EditText)layout.findViewById(R.id.editText1);
		//usernameInput.setInputType(InputType.TYPE_CLASS_DATETIME);
    	
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				// создаем объект для данных
				ContentValues cv = new ContentValues();

				// получаем данные из полей ввода
				String ipAddr = usernameInput.getText().toString();

				// подключаемся к БД
				SQLiteDatabase db = dbHelper.getWritableDatabase();

				Log.d(LOG_TAG, "--- Вставка в таблицу: ---");

				// подготовим данные для вставки в виде пар: наименование столбца - значение

				cv.put("ip", ipAddr);

				// вставляем запись и получаем ее ID
				db.insert("favorites", null, cv);
				// закрываем подключение к БД
				dbHelper.close();

				// проверка наличия интернет соединения выполняется через метод isNetworkConnected()
				if (isNetworkConnected() == true) {
					createServersList();
					updateServerList();

					if (fragment != null) {
						fm.beginTransaction().hide(fragment).commit();
					}

					if (check != null) {
						fm.beginTransaction().remove(check).commit();
					}
				} else {
					createServersList();
					// транзакция добавляющая фрагмент в менаджер
					// дословно читается как: Создать новую транзакцию фрагмента, включить в нее одну операцию add , а затем закрепить
					if (fragment == null) {
						fragment = new MessageFragment();
						fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
					}
				}
				Toast.makeText(FavoriteServersActivity.this, R.string.text_add_ip, Toast.LENGTH_SHORT).show();

			}
		});

    	AlertDialog dialog = builder.create();
		dialog.show();

	}
	
 	// этот класс переопределяет отображение элементов списка
	private class ServerAdapter extends ArrayAdapter<Server> {
		public ServerAdapter(Server[] adapter) {
		super(FavoriteServersActivity.this, 0, adapter);
		} 
	}

	// переопределение адаптера для отображения своего пункта списка
	public class SrvAdapter extends ArrayAdapter<Server> {
		
		  public SrvAdapter(Server[] masSrv) {
		        super(FavoriteServersActivity.this, 0, masSrv);
		  }

		  @Override
		  public View getView(final int position, View convertView, ViewGroup parent) {
			  
			  // Если мы не получили представление, заполняем его
			  if (convertView == null) {
				  convertView = FavoriteServersActivity.this.getLayoutInflater()
						  .inflate(R.layout.list_item_server, null);
			  }
		        
			  // Настройка представления для объекта Crime
			  Server c = getItem(position);
			  int in = 0;
			  
			
			  TextView titleTextView = (TextView)convertView.findViewById(R.id.srv_name);

			  if (c.srvName != null)
				  titleTextView.setText(c.srvName);
			  else
				  titleTextView.setText(getResources().getString(R.string.err_get_server));

		        convertView.setOnClickListener(new View.OnClickListener() {
		            
		        	final Intent g = new Intent(FavoriteServersActivity.this, ServerInfoPage.class);
		        	
		        	@Override
		            public void onClick(View v) {
		        		// передаем в дополнении к интенту адрес выбранного сервера для новой активности
						g.putExtra("ipAddr", masSrv[position].getIpAddr());
						if (masSrv[position].getGame() != null) {
							g.putExtra("name", masSrv[position].getSrvName());
							g.putExtra("game", masSrv[position].getGame());
							g.putExtra("tags", masSrv[position].getTags());
							g.putExtra("map", masSrv[position].getMap());
							g.putExtra("playersf", masSrv[position].getMaxPlayers());
							g.putExtra("playersc", masSrv[position].getNumPlayers());
						}
		        		// и запускаем ее
		        		startActivity(g);		
		            }
		        });
		        
		        TextView dateTextView =  (TextView)convertView.findViewById(R.id.srv_ip);
		        dateTextView.setText(c.getIpAddr());
		        
		        TextView mapTextView = (TextView)convertView.findViewById(R.id.map_name);
		        mapTextView.setText(c.getMap());
		        
		        TextView playersTextView = (TextView)convertView.findViewById(R.id.players);
		        playersTextView.setText(c.getNumPlayers() + "/" + c.getMaxPlayers());
		        
		        ImageButton delBut = (ImageButton)convertView.findViewById(R.id.del_item_button);
		       
		        delBut.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

						delServer(position);
						Toast.makeText(FavoriteServersActivity.this, R.string.text_del_ip, Toast.LENGTH_SHORT).show();
						createServersList();
						updateServerList();
					}
				});
		        
		        return convertView;
		    }
	}
	
	class NewTask extends AsyncTask <Integer, Void, HashMap<String, Object>>{

		Integer indObj;
	// метод используется для размещения тяжелого кода, который будет выполняться в другом потоке
			@Override
		    protected HashMap<String, Object> doInBackground(Integer... params) { 

				//Integer cx = cv;
				HashMap<String, Object> stest = null;
				
				indObj = params[0];
				// здесь происходит запрос к игровому серверу, его адрес инициируется из конструктора
				try {
					SteamSocket.setTimeout(3000);
					SourceServer srv = new SourceServer(masSrv[params[0]].getIpAddr());
					srv.initialize();
					stest = srv.getServerInfo();

				} catch (SteamCondenserException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				} 
				
				return stest;
		}
		
		// этот метод выполняется после предыдущего и в основном потоке, поэтому в нем можно делать действия с ui
		@Override
		protected void onPostExecute(HashMap<String, Object> hm){
			//super.onPostExecute(params);
			
			if (hm != null){
			Log.d("Пост экзекут", "" + hm);
	
				masSrv[indObj].setSrvName(hm.get("serverName").toString());
				masSrv[indObj].setNumPlayers(hm.get("numberOfPlayers").toString());	
				masSrv[indObj].setMaxPlayers(hm.get("maxPlayers").toString());	
				masSrv[indObj].setMap(hm.get("mapName").toString());	
				masSrv[indObj].setTags(hm.get("serverTags").toString());
				masSrv[indObj].setGame(hm.get("gameDescription").toString());
				
				lServers.setAdapter(adapter);
			}
			lServers.setAdapter(adapter);
		}

	}

}