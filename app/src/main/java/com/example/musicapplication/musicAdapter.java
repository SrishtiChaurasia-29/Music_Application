package com.example.musicapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class musicAdapter extends RecyclerView.Adapter<musicAdapter.MyViewHolder> {

    private final List<musiclist> list;
    private final Context context;
    private int playingPosition=0;
    private final SongChangeListener songChangeListener;

    public musicAdapter(List<musiclist> list, Context context) {
        this.list = list;
        this.context = context;
        this.songChangeListener=((SongChangeListener)context);
    }

    @NonNull
    @Override
    public musicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull musicAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        musiclist list2 = list.get(position);
        if (list2.isIsplaying()) {
            playingPosition=position;
            holder.rootLayout.setBackgroundResource(R.drawable.round_black_blue);
        } else {
            holder.rootLayout.setBackgroundResource(R.drawable.round_background);

        }
        String generateDuration=String.format(Locale.getDefault(),"%02d:%02d",
        TimeUnit.MICROSECONDS.toMinutes(Long.parseLong(list2.getDuration())),
                TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(list2.getDuration())) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(list2.getDuration()))));

//                TimeUnit.MILLISECONDS.toSeconds(list2.getDuration(),
//                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(list2.getDuration())))));
        holder.title.setText((list2.getTitle()));
        holder.artist.setText(list2.getArtist());
        holder.musicDuration.setText(list2.getDuration());
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(playingPosition).setIsplaying(false);
                list2.setIsplaying(true);
                songChangeListener.onChanged(position);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout rootLayout;
        private final TextView title;
        private final TextView artist;
        private final TextView musicDuration;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rootLayout=itemView.findViewById(R.id.rootlayout);
            title=itemView.findViewById(R.id.musicTitle);
            artist=itemView.findViewById(R.id.musicArtist);
            musicDuration=itemView.findViewById(R.id.musicDuration);
        }
    }
}
