package com.example.monitor.serverdetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.monitor.utils.ConvertUtils;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;
import com.lotr.steammonitor.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.monitor.utils.Helpers.makeLogTag;

class ListPlayersAdapter extends RecyclerView.Adapter<ListPlayersAdapter.Holder> {

    private static final String TAG = makeLogTag(ListPlayersAdapter.class);

    private List<SteamPlayer> mData;

    ListPlayersAdapter() {
        mData = new ArrayList<>();
    }

    void setData(List<SteamPlayer> data) {
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

        @BindView(R.id.player_name) TextView mPlayerName;
        @BindView(R.id.player_score) TextView mPlayerScore;
        @BindView(R.id.player_rate) TextView mPlayerTime;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        void bindDoctor(SteamPlayer player) {
            mPlayerName.setText(player.getName());
            mPlayerScore.setText(String.valueOf(player.getScore()));
            mPlayerTime.setText(ConvertUtils.formatTime(player.getConnectTime()));
        }
    }
}
