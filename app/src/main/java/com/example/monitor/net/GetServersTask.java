package com.example.monitor.net;

import android.os.AsyncTask;
import android.util.Log;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.github.koraktor.steamcondenser.steam.sockets.SteamSocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;


public class GetServersTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {

        HashMap<String, Object> map = null;

        try {
            SteamSocket.setTimeout(3000);
            SourceServer srv = new SourceServer("46.174.50.240:27015");
            srv.initialize();
            map = srv.getServerInfo();

            Log.i("ASYNC_WORK_RESPONCE", "" + map.get("serverTags"));

       /*     for(Map.Entry<String, Object> entry : map.entrySet()){

                map.get
                Log.i("ASYNC_WORK_RESPONCE", "" + entry.getValue().getClass());
                Log.i("ASYNC_WORK_RESPONCE", "" + entry.getValue().getClass());

            }*/

        } catch (SteamCondenserException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);

    }

}