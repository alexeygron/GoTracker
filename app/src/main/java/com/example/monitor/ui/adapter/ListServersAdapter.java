package com.example.monitor.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lotr.steammonitor.app.R;
import com.lotr.steammonitor.app.Server;

import java.util.List;

/**
 * Адаптер для списка серверов.
 */

public class ListServersAdapter extends RecyclerView.Adapter<ListServersAdapter.Holder> {

    private List<Server> mData;

    public ListServersAdapter(List<Server> data){
        mData = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_servers, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Server server = mData.get(position);
        holder.bindDoctor(server);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView info;

        public Holder(View itemView) {
            super(itemView);
            info = (TextView)itemView.findViewById(R.id.info_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }


        public void bindDoctor(Server server) {
             info.setText(server.getSrvName());
        }
    }
}
