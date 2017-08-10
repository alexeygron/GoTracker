package com.example.monitor.players;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lotr.steammonitor.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.monitor.utils.Helpers.makeLogTag;

public class ListPlayersAdapter extends RecyclerView.Adapter<ListPlayersAdapter.Holder> {

    private List<PlayerModel> mData;
    private FavoritePlayersPresenter mPresenter;

    private static final String TAG = makeLogTag(ListPlayersAdapter.class);

    public ListPlayersAdapter(FavoritePlayersPresenter presenter){
        mData = new ArrayList<>();
        mPresenter = presenter;
    }

    void setData(List<PlayerModel> data){
        mData.clear();
        mData.addAll(data);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_player, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        PlayerModel player = mData.get(position);
        holder.bindDoctor(player, position, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name) TextView mPlayerName;
        @BindView(R.id.status) TextView mStatus;
        @BindView(R.id.avatar) ImageView mAvatar;
        @BindView(R.id.delete_frame) FrameLayout mDeleteFrame;

        Holder(View itemView) {
            super(itemView);
            itemView.getId();
            ButterKnife.bind(this, itemView);
            mDeleteFrame.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            switch(v.getId()){
                case R.id.card_view:
                    break;
                case R.id.delete_frame:
                    ListPlayersAdapter.this.notifyItemRemoved(position);
                    mPresenter.onClickDelButton(position);
                    break;
            }
        }

        void bindDoctor(PlayerModel player, int position, Context context) {
            mPlayerName.setText(player.getPersonName());

            Resources res = context.getResources();
            if (player.getSteamID().equals(player.getPersonName())){
                mStatus.setText(res.getString(R.string.player_non_found));
                mStatus.setTextColor(res.getColor(R.color.status_offline));
            } else {
                mStatus.setText(player.getStatus(context.getResources()));
                mStatus.setTextColor(res.getColor(player.getStatusColor()));

                Glide.with(context).load(player.getAvatarUrl()).into(mAvatar);
            }
        }
    }
}
