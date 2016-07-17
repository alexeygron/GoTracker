/* Активность на которой будет выводиться список игроков из бд */

package com.lotr.steammonitor.app;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monitor.model.Player;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.exceptions.WebApiException;
import com.github.koraktor.steamcondenser.steam.community.SteamId;
import com.github.koraktor.steamcondenser.steam.community.WebApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

// библиотеки admob

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FavoritePlayersActivity extends Activity implements OnClickListener {

    DBHelper dbHelper;
    TextView  errText, mTitle;
    Button  mNavHome, mNavFavorite, mNavServers;
    ImageButton mButtonAdd, mUpBut;
    ListView pServers;
    PlrAdapter adapter;
    ImageView test;

    Bitmap bm = null;

    FragmentManager fm;
    Fragment fragment;
    Fragment check;

    Player masPlr[];

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_players_activity);

        mTitle = (TextView)findViewById(R.id.title);
        mTitle.setText(R.string.title_favorites_players);

        // кнопки навигации
        mNavHome = (Button)findViewById(R.id.nav_home_but);
        mNavHome.setOnClickListener(this);
        mNavFavorite = (Button)findViewById(R.id.nav_favorite_but);
        mNavFavorite.setOnClickListener(this);
        mNavServers = (Button)findViewById(R.id.nav_servers_but);
        mNavServers.setOnClickListener(this);

        mUpBut = (ImageButton)findViewById(R.id.update_list);
        mUpBut.setOnClickListener(this);
        mButtonAdd = (ImageButton)findViewById(R.id.btn_add_ip);
        mButtonAdd.setOnClickListener(this);
        pServers = (ListView) findViewById(R.id.pServers);

        // подключения рекламного блока admob
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        /*AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("0A596F7E158C70A3174EF291FCDEF0AC").build();
        mAdView.loadAd(adRequest);*/

       // mAvatar = (ImageView) findViewById(R.id.avatar);

        test = (ImageView) findViewById(R.id.imageView1);
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this, "players");

        fm = getFragmentManager();
        fragment = fm.findFragmentById(R.id.fragmentContainer);
        check = fm.findFragmentById(R.id.fragmentContainer2);

        if (isNetworkConnected() == true){
            pServers.setVisibility(View.VISIBLE);
            createServersList();
            updateServerList();

            if (fragment != null) {
                fm.beginTransaction().hide(fragment).commit();
            }

            if (masPlr.length == 0) {
                check = new CheckFragmentPlayer();
                fm.beginTransaction().add(R.id.fragmentContainer2, check).commit();

            }

        } else {
            pServers.setVisibility(View.INVISIBLE);
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

        Intent g;

        switch(v.getId()) {

            case R.id.nav_favorite_but:
                g = new Intent(FavoritePlayersActivity.this, FavoriteServersActivity.class);
                startActivity(g);
                break;

            case R.id.nav_home_but:
                g = new Intent(FavoritePlayersActivity.this,  GameInfoActivity.class);
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
                if (isNetworkConnected() == true){
                    pServers.setVisibility(View.VISIBLE);
                    createServersList();
                    updateServerList();
                    Toast.makeText(this, R.string.toast_update_players_list, Toast.LENGTH_SHORT).show();
                    if (fragment != null) {
                        fm.beginTransaction().hide(fragment).commit();
                    }
                } else {
                    Toast.makeText(this, R.string.error_network_text, Toast.LENGTH_SHORT).show();
                    pServers.setVisibility(View.INVISIBLE);
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


    // этот метод проверяет является ли введеный ID числом или строкой
    private boolean checkString(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // метод в котором происходит создание массива объектов и инициалиция их адресами серверов из базы
    void createServersList () {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor ct = db.query("players", null, null, null, null, null, null);

        Log.d("count colum", " " + ct.getCount());

        // массив объектов в полях которых получается и хранится информация о сервере
        masPlr = new Player[ct.getCount()];

        int i = 0;

        if (ct.moveToFirst()) {
            int ipColIndex = ct.getColumnIndex("id");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("Добавление в массив", " " + "id = " + ct.getString(ipColIndex));

                // запись в объект ip из базы
                masPlr[i] = new Player(ct.getString(ipColIndex));
                i = i + 1;

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла

            } while (ct.moveToNext());

        } else
            Log.d("", "0 строк");

        ct.close();
        dbHelper.close();

        adapter = new PlrAdapter(masPlr);
        pServers.setAdapter(adapter);
    }

    // этот метод обновляет поля в объектов созданного выше массива
    void updateServerList(){

       adapter = new PlrAdapter(masPlr);
       // for (int i = 0; i < masPlr.length; i++){
            PlayersTask nt = new PlayersTask();
            nt.execute();
       // }

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

    // метод конвертирующий время из unix time  в обычный формат
    public String convertTime(String time){


        Date now = new Date();
        Long longTime = new Long(now.getTime()/1000);

        long unixSeconds = Long.parseLong(time);

        Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // the format of your date "yyyy-MM-dd HH:mm:ss z"
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-2")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    // Метод вызывающий диалог ввода IP
    private void addIpDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(FavoritePlayersActivity.this);

        builder.setTitle(R.string.add_player_title);

        LayoutInflater inflater = FavoritePlayersActivity.this.getLayoutInflater();

        View layout = inflater.inflate(R.layout.add_id_dialog, null);
        builder.setView(layout);

        final EditText usernameInput = (EditText)layout.findViewById(R.id.editText1);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                // создаем объект для данных
                ContentValues cv = new ContentValues();

                // получаем данные из полей ввода
                String ipAddr = usernameInput.getText().toString();

                Log.d("ipAddr", ipAddr);

                // проверяем правильность введенного id, и приобразуем если это нужно в отделььном потоке, который вернет значение в зависимости от результата
                try {
                    ipAddr = new IdTask().execute(ipAddr).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                // если id действительный, бодавляем его в bd
                if (ipAddr != null) {

                    // подключаемся к БД
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // подготовим данные для вставки в виде пар: наименование столбца - значение

                    cv.put("id", ipAddr);

                    // вставляем запись и получаем ее ID
                    db.insert("players", null, cv);
                    // закрываем подключение к БД
                    dbHelper.close();
                    Toast.makeText(FavoritePlayersActivity.this, R.string.text_add_id, Toast.LENGTH_SHORT).show();
                 } else {
                    // если нет, то выводим сообщение об этом
                    Toast.makeText(FavoritePlayersActivity.this, R.string.toast_non_found, Toast.LENGTH_SHORT).show();
                }

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
                Toast.makeText(FavoritePlayersActivity.this, R.string.text_del_id, Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // вспомогательный метод удалающий из бд запись по полю ip
    private void delServer(int pos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // удаляем записи
        String ipDel = masPlr[pos].getPlayerId();
        int clearCount = db.delete("players", "id" + "='" + ipDel + "'", null);

        dbHelper.close();
        createServersList();
        updateServerList();
    }

    // переопределение адаптера для отображения своего пункта списка
    public class PlrAdapter extends ArrayAdapter<Player> {

        public PlrAdapter(Player[] masPlr) {
            super(FavoritePlayersActivity.this, 0, masPlr);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // Если мы не получили представление, заполняем его
            if (convertView == null) {
                convertView = FavoritePlayersActivity.this.getLayoutInflater()
                        .inflate(R.layout.list_item_player, null);
            }

            // Настройка представления для объекта Crime
            Player c = getItem(position);
            int in = 0;


            TextView titleTextView = (TextView)convertView.findViewById(R.id.srv_name);
            titleTextView.setText(c.getPlayerName());

            TextView titleGameInfo = (TextView)convertView.findViewById(R.id.status_text);

            if (c.getgameextrainfo() != null) {
                titleGameInfo.setText(FavoritePlayersActivity.this.getResources().getString(R.string.playing) + " "+ c.getgameextrainfo());
                titleGameInfo.setTextColor(Color.parseColor("#a3d445"));
            } else {

                switch (c.getpersonastate()) {
                    case "0":
                        titleGameInfo.setText(FavoritePlayersActivity.this.getResources().getString(R.string.last_online_status) + " " + convertTime(c.getlastlogoff()));
                        titleGameInfo.setTextColor(Color.parseColor("#c8c8c8"));
                        break;
                    case "1":
                        titleGameInfo.setText(FavoritePlayersActivity.this.getResources().getString(R.string.online_status));
                        titleGameInfo.setTextColor(Color.parseColor("#57cbde"));
                        break;
                    case "2":
                        titleGameInfo.setText(FavoritePlayersActivity.this.getResources().getString(R.string.busy_status));
                        titleGameInfo.setTextColor(Color.parseColor("#57cbde"));
                        break;
                    case "3":
                        titleGameInfo.setText(FavoritePlayersActivity.this.getResources().getString(R.string.away_status));
                        titleGameInfo.setTextColor(Color.parseColor("#57cbde"));
                        break;
                    case "4":
                        titleGameInfo.setText(FavoritePlayersActivity.this.getResources().getString(R.string.snooze_status));
                        titleGameInfo.setTextColor(Color.parseColor("#57cbde"));
                        break;
                    case "5":
                        titleGameInfo.setText(FavoritePlayersActivity.this.getResources().getString(R.string.looking_to_trade_status));
                        titleGameInfo.setTextColor(Color.parseColor("#57cbde"));
                        break;
                    case "6":
                        titleGameInfo.setText(FavoritePlayersActivity.this.getResources().getString(R.string.looking_to_play_status));
                        titleGameInfo.setTextColor(Color.parseColor("#57cbde"));
                        break;

                }
            }

            /* convertView.setOnClickListener(new View.OnClickListener() {

                final Intent g = new Intent(FavoritePlayersActivity.this, PlayerInfoPage.class);

                @Override
                public void onClick(View v) {

                    g.putExtra("id", masPlr[position].getPlayerId());
                    g.putExtra("name", masPlr[position].getPlayerName());

                    startActivity(g);
                }
            });
            */
            ImageView mAvatar = (ImageView) convertView.findViewById(R.id.avatar);
            mAvatar.setImageBitmap(masPlr[position].getBtmAvs());

            ImageButton delBut = (ImageButton)convertView.findViewById(R.id.del_item_button);

            delBut.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    delServer(position);
                }
            });

            return convertView;
        }
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // наследник AsyncTask в качестве параметров получает типы данныъ первый для входных данных, второй для промежуточных данных, третий для выходных
    class PlayersTask extends AsyncTask <Integer, Void, String>{

        String json, puts = "";
        Integer indObj;

        @Override
        protected String doInBackground(Integer... params) {

           // indObj = params[0];

            try {
                WebApi.setApiKey("20AA3876A3B48CB80B88033C77056A1F");
                Map<String, Object> map = new HashMap<String, Object>();

                for (int i = 0; i < masPlr.length; i++){
                     puts = puts + masPlr[i].getPlayerId() + ",";
                }
               // Log.d ("Puts", puts);
                map.put("steamids", puts);
                //Log.d ("HashMap", map.toString());

                json = WebApi.getJSON("ISteamUser", "GetPlayerSummaries", 2, map);

                //Log.d ("Json", json.toString());
            } catch (SteamCondenserException e) {
                e.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(String jsn){

            try {
                // наркоманская обработка json
                //Log.d ("Final result", jsn);

                JSONObject  jobj = new JSONObject(jsn);

                JSONObject response = jobj.getJSONObject("response");

                JSONArray players = response.getJSONArray("players");

                for (int v = 0; v < masPlr.length; v++){

                    masPlr[v].setPlayerName(players.getJSONObject(v).getString("personaname"));

                    if (players.getJSONObject(v).isNull("gameextrainfo") != true) {
                        masPlr[v].setgameextrainfo(players.getJSONObject(v).getString("gameextrainfo"));
                    }

                    Log.d("GameExtraInfo", masPlr[v].getPlayerName() + masPlr[v].getgameextrainfo());

                    masPlr[v].setPlayerId(players.getJSONObject(v).getString("steamid"));
                    masPlr[v].setpersonastate(players.getJSONObject(v).getString("personastate"));
                    masPlr[v].setlastlogoff(players.getJSONObject(v).getString("lastlogoff"));
                    Log.d("getpersonastate", "" + masPlr[v].getpersonastate());
                    masPlr[v].setUrlAvatar(players.getJSONObject(v).getString("avatarmedium"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            pServers.setAdapter(adapter);
            new  AvatarsTask().execute(masPlr.length);

        }

    }

    class AvatarsTask extends AsyncTask <Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... id) {

           for (int i = 0; i < masPlr.length; i++)
                masPlr[i].setBtmAvs(getBitmapFromURL(masPlr[i].getUrlAvatar()));

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //test.setImageBitmap(masPlr[0].getBtmAvs());

            pServers.setAdapter(adapter);
        }
    }

        class IdTask extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... id) {

                String id2 = id.toString();

                String js = null;
                String nid = null;
                Long nidl = null;
                JSONArray players2 = null;

                // пытаемся конвертировать id в 64бит формат
                for (String s : id) {
                    Log.d("string..", s);
                    try {
                        nidl = SteamId.resolveVanityUrl(s);
                    } catch (WebApiException e) {
                        e.printStackTrace();
                    }
                }

                // если получается, возращаем его в диалог
                if (nidl != null) {
                    nid = nidl.toString();
                    return nid;

                } else {
                    // если нет, проверяем евляется ли он уже реальным 64битным адресом
                    try {
                        WebApi.setApiKey("20AA3876A3B48CB80B88033C77056A1F");
                        Map<String, Object> map2 = new HashMap<String, Object>();

                        for (String i : id) {

                            id2 = i;
                        }

                        map2.put("steamids", id2);

                        js = WebApi.getJSON("ISteamUser", "GetPlayerSummaries", 2, map2);

                        Log.d ("MAP", map2.toString());

                        JSONObject  jobj2 = new JSONObject(js);

                        JSONObject response2 = jobj2.getJSONObject("response");

                        players2 = response2.getJSONArray("players");


                        Log.d("JS Otver", js);
                    } catch (WebApiException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // если полученные масив из json не пустой, значит является, возвращаем id в диалог
                    if (players2.length() != 0) {
                        return id2;
                        // в противном случае возвращаем null
                    } else {
                        return null;
                    }
                }
                //Log.d("VanityUrl", "" + testl);
            }

            @Override
            protected void onPostExecute(String result) {
                // super.onPostExecute(result);

                //test.setImageBitmap(masPlr[0].getBtmAvs());

               // adapter = new PlrAdapter(masPlr);
                pServers.setAdapter(adapter);
            }

    }}
