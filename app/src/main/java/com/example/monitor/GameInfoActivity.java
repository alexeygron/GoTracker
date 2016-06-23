package com.lotr.steammonitor.app;

/*
 * MasterServer.REGION_ALL, "<li><code>\gamedir\cstrike\</code>"
 * 
 * String cv = cc.substring(0,0)+cc.substring(0+1);
 * 
 * 	private Server srv = new Server();

 * new ServerInfo().execute();
 * */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.WebApi;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GameInfoActivity extends Activity implements View.OnClickListener {

	ImageButton mUpBut;
	Button mNavServers, mNavPlayers;
	TextView mTitle, mScheduler, mOnline_servers, mOnline_players, mSearching_players, mSearch_seconds_avg, mSessionslogon, mSteamcommunity, mIeconitems, mLeaderboards,
			mVersion, mTimestamp, mTime;

	LinearLayout mMatchmaking, mServices, mApp;

	FragmentManager fm;
	Fragment fragment;

	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_info_page);

		// кнопки навигации
		mNavPlayers = (Button)findViewById(R.id.nav_players_but);
		mNavPlayers.setOnClickListener(this);
		mNavServers = (Button)findViewById(R.id.nav_servers_but);
		mNavServers.setOnClickListener(this);

		mUpBut = (ImageButton)findViewById(R.id.update_list);
		mUpBut.setOnClickListener(this);
		
		mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText(R.string.title_info_page);

		mOnline_players = (TextView) findViewById(R.id.textView2);
		mOnline_servers = (TextView) findViewById(R.id.textView4);
		mSearching_players = (TextView) findViewById(R.id.textView6);
		mSearch_seconds_avg = (TextView) findViewById(R.id.textView8);
		mScheduler = (TextView) findViewById(R.id.textView10);

		mSessionslogon = (TextView) findViewById(R.id.textView11);
		mSteamcommunity = (TextView) findViewById(R.id.textView12);
		mIeconitems = (TextView) findViewById(R.id.textView13);
		mLeaderboards = (TextView) findViewById(R.id.textView14);

		mVersion = (TextView) findViewById(R.id.textView15);
		mTimestamp = (TextView) findViewById(R.id.textView16);
		mTime = (TextView) findViewById(R.id.textView17);

		mMatchmaking = (LinearLayout)findViewById(R.id.matchmaking);
		mServices = (LinearLayout)findViewById(R.id.services);
		mApp = (LinearLayout)findViewById(R.id.app);

		// подключения рекламного блока admob
		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		/*AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("0A596F7E158C70A3174EF291FCDEF0AC").build();
		mAdView.loadAd(adRequest);*/



		fm = getFragmentManager();
		fragment = fm.findFragmentById(R.id.fragmentContainer);

		// проверка наличия интернет соединения выполняется через метод isNetworkConnected()
		if (isNetworkConnected() == true){
			mMatchmaking.setVisibility(View.VISIBLE);
			mServices.setVisibility(View.VISIBLE);
			mApp.setVisibility(View.VISIBLE);
			new GameInfoTask().execute();
			if (fragment != null) {
				fm.beginTransaction().hide(fragment).commit();
			}
		} else {
			// прячем лауты
			mMatchmaking.setVisibility(View.INVISIBLE);
			mServices.setVisibility(View.INVISIBLE);
			mApp.setVisibility(View.INVISIBLE);

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
			case R.id.nav_servers_but:
				g = new Intent(GameInfoActivity.this,  FavoriteServersActivity.class);
				startActivity(g);
				break;
			case R.id.nav_players_but:
				g = new Intent(GameInfoActivity.this,  FavoritePlayersActivity.class);
				startActivity(g);
				break;
			case R.id.update_list:
				if (isNetworkConnected() == true){
					mMatchmaking.setVisibility(View.VISIBLE);
					mServices.setVisibility(View.VISIBLE);
					mApp.setVisibility(View.VISIBLE);
					Toast.makeText(this, R.string.toast_update_game_info, Toast.LENGTH_SHORT).show();
					new GameInfoTask().execute();
					if (fragment != null) {
						fm.beginTransaction().hide(fragment).commit();
					}
				} else {
					// прячем лауты
					mMatchmaking.setVisibility(View.INVISIBLE);
					mServices.setVisibility(View.INVISIBLE);
					mApp.setVisibility(View.INVISIBLE);

					Toast.makeText(this, R.string.error_network_text, Toast.LENGTH_SHORT).show();
					// транзакция добавляющая фрагмент в менаджер
					// дословно читается как: Создать новую транзакцию фрагмента, включить в нее одну операцию add , а затем закрепить
					if (fragment == null) {
						fragment = new MessageFragment();
						fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
					}
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
		} else {
			return true;
		}
	}

	class GameInfoTask extends AsyncTask<Void, Void, Void> {


		String json;

		@Override
		protected Void doInBackground(Void... params) {

			try {
				WebApi.setApiKey("20AA3876A3B48CB80B88033C77056A1F");

				json = WebApi.getJSON("ICSGOServers_730", "GetGameServersStatus", 1);

			} catch (SteamCondenserException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			//Log.d("Json", json);

			try {

				JSONObject jobj = new JSONObject(json);

				JSONObject mresult = jobj.getJSONObject("result");

				JSONObject matchmaking = mresult.getJSONObject("matchmaking");

				mOnline_players.setText(matchmaking.getString("online_players"));
				mOnline_servers.setText(matchmaking.getString("online_servers"));
				mSearching_players.setText(matchmaking.getString("searching_players"));
				mSearch_seconds_avg.setText(matchmaking.getString("search_seconds_avg"));
				mScheduler.setText(matchmaking.getString("scheduler"));

				JSONObject services = mresult.getJSONObject("services");

				mSessionslogon.setText(services.getString("SessionsLogon"));
				mSteamcommunity.setText(services.getString("SteamCommunity"));
				mIeconitems.setText(services.getString("IEconItems"));
				mLeaderboards.setText(services.getString("Leaderboards"));

				JSONObject app = mresult.getJSONObject("app");

				mVersion.setText(app.getString("version"));
				mTimestamp.setText(app.getString("timestamp"));
				mTime.setText(app.getString("time"));

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}


		}

	}
