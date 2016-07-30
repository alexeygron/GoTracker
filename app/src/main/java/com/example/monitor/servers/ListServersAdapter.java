package com.example.monitor.servers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lotr.steammonitor.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер для списка серверов.
 */

public class ListServersAdapter extends RecyclerView.Adapter<ListServersAdapter.Holder> {

    private static final String TAG = "ListServersAdapter";

    private List<ServerModel> mData;
    private FavoriteServersPresenter mPresenter;

    public ListServersAdapter(FavoriteServersPresenter presenter){
        mData = new ArrayList();
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

        private int mPosition;

        private TextView mServerName;
        private TextView mServerIp;
        private TextView mPlayersCount;
        private TextView mMapName;
        private ImageView mButtonDel;


        public Holder(View itemView) {
            super(itemView);
            mServerName = (TextView)itemView.findViewById(R.id.srv_name);
            mServerIp = (TextView)itemView.findViewById(R.id.srv_ip);
            mPlayersCount = (TextView)itemView.findViewById(R.id.players);
            mMapName = (TextView)itemView.findViewById(R.id.map_name);
            mButtonDel = (ImageView)itemView.findViewById(R.id.del_item_button);
            mButtonDel.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();

            switch(v.getId()){
                case R.id.card_view:
                    mPresenter.onClickListItem(position);
                    break;
                case R.id.del_item_button:
                    ListServersAdapter.this.notifyItemRemoved(position);
                    mPresenter.onClickDelButton(position);
                    break;
            }
        }

        public void bindDoctor(ServerModel server, int position) {
            mPosition = position;
            mServerName.setText(server.getSrvName());
            mServerIp.setText(server.getIpAddr());
            mPlayersCount.setText(server.getPlayers());
            mMapName.setText(server.getMap());
        }
    }
}
