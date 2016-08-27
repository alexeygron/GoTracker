package com.example.monitor.servers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lotr.steammonitor.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Адаптер для списка серверов.
 */

public class ListServersAdapter extends RecyclerView.Adapter<ListServersAdapter.Holder> {

    private ArrayList<ServerModel> mData;
    private FavoriteServersPresenter mPresenter;

    public ListServersAdapter(FavoriteServersPresenter presenter){
        mData = new ArrayList<>();
        mPresenter = presenter;
    }

    void setData(List<ServerModel> data){
        mData.clear();
        mData.addAll(data);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_server, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ServerModel server = mData.get(position);
        holder.bindDoctor(server, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.srv_name) TextView mServerName;
        @BindView(R.id.srv_ip) TextView mServerIp;
        @BindView(R.id.players) TextView mPlayersCount;
        @BindView(R.id.map_name) TextView mMapName;
        @BindView(R.id.delete_frame) FrameLayout mDeleteFrame;
        private int mPosition;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            /*mServerName = (TextView)itemView.findViewById(R.id.srv_name);
            mServerIp = (TextView)itemView.findViewById(R.id.srv_ip);
            mPlayersCount = (TextView)itemView.findViewById(R.id.players);
            mMapName = (TextView)itemView.findViewById(R.id.map_name);
            mButtonDel = (ImageView)itemView.findViewById(R.id.del_item_button);*/
            mDeleteFrame.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();

            switch(v.getId()){
                case R.id.card_view:
                    mPresenter.onClickListItem(position);
                    break;
                case R.id.delete_frame:
                    ListServersAdapter.this.notifyItemRemoved(position);
                    mPresenter.onClickDelButton(position);
                    break;
            }
        }

        public void bindDoctor(ServerModel server, int position) {
            mPosition = position;
            mServerName.setText(server.getmName());
            mServerIp.setText(server.getIpAddr());
            mPlayersCount.setText(server.getPlayers());
            mMapName.setText(server.getMap());
        }
    }
}
