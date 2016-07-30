package com.example.monitor.serverdetails;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.monitor.servers.FavoriteServersPresenter;
import com.example.monitor.servers.ServerModel;
import com.example.monitor.utils.ConvertUtils;
import com.example.monitor.utils.LogUtils;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;
import com.lotr.steammonitor.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Адаптер для списка серверов.
 */

public class ListPlayersAdapter extends RecyclerView.Adapter<ListPlayersAdapter.Holder> {

    private static final String TAG = LogUtils.makeLogTag(ListPlayersAdapter.class);

    private List<SteamPlayer> mData;

    public ListPlayersAdapter(){
        mData = new ArrayList<>();
    }

    void setData(List<SteamPlayer> data){
        mData.clear();
        mData.addAll(data);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_details_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        SteamPlayer player = mData.get(position);
        holder.bindDoctor(player);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mPlayerName;
        private TextView mPlayerScore;
        private TextView mPlayerTime;

        public Holder(View itemView) {
            super(itemView);
            mPlayerName = (TextView) itemView.findViewById(R.id.player_name);
            mPlayerScore = (TextView) itemView.findViewById(R.id.player_score);
            mPlayerTime = (TextView) itemView.findViewById(R.id.player_rate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           Log.i(TAG, "Click " + getLayoutPosition());
        }

        public void bindDoctor(SteamPlayer player) {
            mPlayerName.setText(player.getName());
            mPlayerScore.setText(String.valueOf(player.getScore()));
            mPlayerTime.setText(ConvertUtils.formatTime(player.getConnectTime()));
        }
    }
}
