package com.lotr.steammonitor.app;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.github.koraktor.steamcondenser.steam.sockets.SteamSocket;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.os.AsyncTask;

public class ServerInfoPage extends Activity implements OnClickListener {
	
	Button mNavHome, mNavFavorite, mNavServers;
	ImageButton mUpBut, mShowBar;
	TextView mClassName, errText, mTitle, mPlayerNameCol, mPlayerScoreCol, mTextSrvName, mTextSrvGame, mTextSrvTags, mTextSrvIp, 
	mTextSrvMap, mTextSrvPlayersf, mTextSrvPlayersc, textRank;
	ListView mPlayersList;
	ArrayAdapter<String> adapter;
	String masName[];
	LinearLayout mOne, mTwo, info;
	int infoBar = 1;

	FragmentManager fm2;
	Fragment fragment2;
	
	private String srvIp, srvName, srvGame, srvTags, srvMap, srvPlayersf, srvPlayersc;
	protected HashMap<String, SteamPlayer> stest;
	protected HashMap<String, Object> srvInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_info_activity);

		srvIp = getIntent().getStringExtra("ipAddr");

		if (getIntent().getStringExtra("game") != null) {
			// метод getIntent возвращает объект который был передан в startActivity при запуске этой активности
			srvName = getIntent().getStringExtra("name");
			srvGame = getIntent().getStringExtra("game");
			srvTags = getIntent().getStringExtra("tags");
			srvMap = getIntent().getStringExtra("map");
			srvPlayersf = getIntent().getStringExtra("playersf");
			srvPlayersc = getIntent().getStringExtra("playersc");
		} else {

			srvName = getResources().getString(R.string.err_get_server);
			Log.d("Imtent --", "intent is empty");
		}
		// кнопки навигации
		mNavHome = (Button)findViewById(R.id.nav_home_but);
		mNavHome.setOnClickListener(this);
		mNavFavorite = (Button)findViewById(R.id.nav_servers_but);
		mNavFavorite.setOnClickListener(this);
		mNavServers = (Button)findViewById(R.id.nav_players_but);
		mNavServers.setOnClickListener(this);
		mUpBut = (ImageButton)findViewById(R.id.update_list);
		mUpBut.setOnClickListener(this);
		mShowBar = (ImageButton)findViewById(R.id.show_bar);
		mShowBar.setOnClickListener(this);

		//mClassName = (TextView)findViewById(R.id.info_text);
		//mClassName.setText(getLocalClassName());

		mTextSrvName = (TextView)findViewById(R.id.text_info_srv_name);
		mTextSrvName.setText(" " + srvName);
		
		mTextSrvGame = (TextView)findViewById(R.id.text_info_srv_game);
		mTextSrvGame.setText(srvGame);
		
		mTextSrvTags = (TextView)findViewById(R.id.text_info_srv_tags);
		mTextSrvTags.setText(srvTags);
		
		mTextSrvIp = (TextView)findViewById(R.id.text_info_srv_addr);
		mTextSrvIp.setText(srvIp);
		
		mTextSrvMap = (TextView)findViewById(R.id.text_info_srv_map);
		mTextSrvMap.setText(srvMap);
		
		mTextSrvPlayersf = (TextView)findViewById(R.id.text_info_srv_playerf);
		mTextSrvPlayersf.setText(srvPlayersc + "/" + srvPlayersf);
		
		mPlayersList = (ListView)findViewById(R.id.players_list);
		mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText(srvName);

		mOne = (LinearLayout)findViewById(R.id.si_one);
		mTwo = (LinearLayout)findViewById(R.id.si_two);

		mTwo.setOnClickListener(this);

		fm2 = getFragmentManager();
		fragment2 = fm2.findFragmentById(R.id.fragmentContainer);

		// подключения рекламного блока admob
		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		/*AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("0A596F7E158C70A3174EF291FCDEF0AC").build();
		mAdView.loadAd(adRequest);*/

		if (isNetworkConnected() == true){
			mOne.setVisibility(View.VISIBLE);
			mTwo.setVisibility(View.VISIBLE);

			if (fragment2 != null) {
				fm2.beginTransaction().hide(fragment2).commit();
			}
	    		getInfo();

		} else {
			mOne.setVisibility(View.INVISIBLE);
			mTwo.setVisibility(View.INVISIBLE);
			if (fragment2 == null) {
				fragment2 = new MessageFragment();
				fm2.beginTransaction().add(R.id.fragmentContainer, fragment2).commit();
			}

		}
	}

	void getInfo(){
		
		new InfoTask().execute();
		
	}
	
	@Override
	public void onClick(View v) {
		final Intent g;
		switch(v.getId()){
			case R.id.nav_home_but:
				g = new Intent(ServerInfoPage.this,  FavoriteServersActivity.class);
				startActivity(g);
				break;
			case R.id.nav_servers_but:
				g = new Intent(ServerInfoPage.this, FavoritePlayersActivity.class);
				startActivity(g);
				break;
			case R.id.nav_players_but:
				g = new Intent(ServerInfoPage.this,  GameInfoActivity.class);
				startActivity(g);
				break;
			case R.id.si_two:
				mTwo.setVisibility(LinearLayout.GONE);
				infoBar = 0;
				break;
			case R.id.show_bar:

				if (infoBar == 1) {
					mTwo.setVisibility(LinearLayout.GONE);
					infoBar = 0;
				}else {
					mTwo.setVisibility(LinearLayout.VISIBLE);
					infoBar = 1;
				}
				break;
			case R.id.update_list:
				if (isNetworkConnected() == true){
					mOne.setVisibility(View.VISIBLE);
					mTwo.setVisibility(View.VISIBLE);
					getInfo();
					mPlayersList.setVisibility(View.VISIBLE);
					Toast.makeText(this, R.string.toast_update_server_info, Toast.LENGTH_SHORT).show();

					if (fragment2 != null) {
						fm2.beginTransaction().hide(fragment2).commit();
					}
				} else {
					mOne.setVisibility(View.INVISIBLE);
					mTwo.setVisibility(View.INVISIBLE);
					mPlayersList.setVisibility(View.INVISIBLE);
					Toast.makeText(this, R.string.error_network_text, Toast.LENGTH_SHORT).show();
					if (fragment2 == null) {
						fragment2 = new MessageFragment();
						fm2.beginTransaction().add(R.id.fragmentContainer, fragment2).commit();

					}

				}

				if (infoBar == 1) {
					mTwo.setVisibility(LinearLayout.VISIBLE);
				}else {
					mTwo.setVisibility(LinearLayout.GONE);
				}
				break;
			
		
		}	
	}
	
	// этот метод проверяет наличие соединения с интернетом
		private boolean isNetworkConnected() {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();

			if (ni == null) {
				return false;
			} else
				return true;
		}
		
	// метод который обновляет инфу о карте сервера и коли-ве игроков
		
		public void updateMap(){
			
    		srvMap = srvInfo.get("mapName").toString();
    		srvPlayersc = srvInfo.get("numberOfPlayers").toString();
    		mTextSrvMap.setText(srvMap);
    		mTextSrvPlayersf.setText(srvPlayersc + "/" + srvPlayersf);
			
		}
	
	// переопределение адаптера для отображения своего пункта списка
		public class PlayersAdapter extends ArrayAdapter<String> {
			
			  public PlayersAdapter(String[] masName2) {
			        super(ServerInfoPage.this, 0, masName2);
			  }

			  @Override
			  public View getView(final int position, View convertView, ViewGroup parent) {
				  
				  // Если мы не получили представление, заполняем его
				  if (convertView == null) {
					  convertView = ServerInfoPage.this.getLayoutInflater()
							  .inflate(R.layout.players_list, null);
				  }
				 // TextView textRank = (TextView)convertView.findViewById(R.id.text_rank);
				 // textRank.setText("" + (position + 1));
			    
				  TextView titleTextView = (TextView)convertView.findViewById(R.id.player_name);
				  titleTextView.setText(masName[position]);
				  
				  TextView playerscore = (TextView)convertView.findViewById(R.id.player_score);
				  playerscore.setText("" + stest.get(masName[position]).getScore());
				  
				  TextView playertime = (TextView)convertView.findViewById(R.id.player_rate);
				  playertime.setText("" + String.format("%.2f", (stest.get(masName[position]).getConnectTime() / 60 / 60)));


				  Log.d("Time", "" + stest.get(masName[position]).getConnectTime());


				  return convertView;
			    }
		}

		 class InfoTask extends AsyncTask<Void, Void, HashMap<String, SteamPlayer>> {

			    @Override
			    protected HashMap<String, SteamPlayer> doInBackground(Void... params) {
			    	
			    	// здесь происходит запрос к игровому серверу, его адрес инициируется из конструктора
					try {
							SourceServer srvIn = new SourceServer(srvIp);
							SteamSocket.setTimeout(6000);
							srvIn.initialize();
							stest = srvIn.getPlayers();
							srvInfo = srvIn.getServerInfo();
							
							Log.d("Rules", "" +  srvIn.toString());
						
					} catch (SteamCondenserException e) {

						e.printStackTrace();
					} catch (TimeoutException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				} 
					return stest;
		}

			    @Override
			    protected void onPostExecute(HashMap<String, SteamPlayer> result) {
					super.onPostExecute(result);

			        if (result != null){
			      	Log.d ("Размер ", "" + result.size());
			      
			      	masName = new String[result.size()];
					
					// заполяем массив именами игроков
					int i = 0;
					for(Entry entry: result.entrySet()) {
						  //получить ключ
						  String key = entry.getKey().toString();
						  masName[i] = key;
						 // Log.d ("Содержимое массива " + i, masName[i].toString());
						  i = i + 1;
						}
					
					// и сортируем его методом пузырька
					for(int ic = 0; ic < masName.length - 1; ic++){
					   
						for(int j = 0; j < masName.length - ic - 1; j++){
					    
							if(result.get(masName[j]).getScore() < result.get(masName[j + 1]).getScore()){
					   
								String temp = masName[j];
								masName[j] = masName[j + 1];
								masName[j + 1] = temp;
					
							}
						}
					}
					
				PlayersAdapter adapter = new PlayersAdapter(masName);
				mPlayersList.setAdapter(adapter);   
				updateMap();
			    } else {



			 }}
		 }
}