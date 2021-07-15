package com.xmut.harmony.Video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xmut.harmony.R;
import com.xmut.harmony.Video.contract.OnItemClickListener;
import com.xmut.harmony.Video.entity.PlayEntity;
import com.xmut.harmony.Video.utils.LogUtil;
import com.xmut.harmony.Video.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayDataAdapter extends RecyclerView.Adapter<SelectPlayDataAdapter.PlayViewHolder> {
    private static final String TAG = "SelectPlayDataAdapter";

    // Data sources list
    private List<PlayEntity> playList;

    // Context
    private Context context;

    // Click item listener
    private OnItemClickListener onItemClickListener;

    /**
     * Constructor
     *
     * @param context             Context
     * @param onItemClickListener Listener
     */
    public SelectPlayDataAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        playList = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Set list data
     *
     * @param playList Play data
     */
    public void setSelectPlayList(List<PlayEntity> playList) {
        if (this.playList.size() > 0) {
            this.playList.clear();
        }
        this.playList.addAll(playList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.select_play_item, parent, false);
        return new PlayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayViewHolder holder, final int position) {
        if (playList.size() > position && holder != null) {
            PlayEntity playEntity = playList.get(position);
            if (playEntity == null) {
                LogUtil.i(TAG, "current item data is empty.");
                return;
            }
            StringUtil.setTextValue(holder.playName, playEntity.getName());
            StringUtil.setTextValue(holder.playUrl, playEntity.getUrl());
            StringUtil.setTextValue(holder.playType, String.valueOf(playEntity.getUrlType()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });

            RequestOptions options = new RequestOptions();
            options.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .frame(1000000);

            Glide.with(context).setDefaultRequestOptions(options).load(playEntity.getUrl()).placeholder(R.drawable.ic_video_img).into(holder.play_icon);

        }
    }

    @Override
    public int getItemCount() {
        return playList.size();
    }

    /**
     * Show view holder
     */
    static class PlayViewHolder extends RecyclerView.ViewHolder {

        // The video name
        private TextView playName;

        // The video type
        private TextView playType;

        // The video url
        private TextView playUrl;

        private ImageView play_icon;
        /**
         * Constructor
         *
         * @param itemView Item view
         */
        public PlayViewHolder(View itemView) {
            super(itemView);
            if (itemView != null) {
                playName = itemView.findViewById(R.id.play_name);
                playType = itemView.findViewById(R.id.play_type);
                playUrl = itemView.findViewById(R.id.play_url);
                play_icon = itemView.findViewById(R.id.play_icon);
            }
        }
    }
}
