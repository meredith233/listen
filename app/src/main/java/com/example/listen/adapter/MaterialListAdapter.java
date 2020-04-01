package com.example.listen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listen.R;
import com.example.listen.entity.Material;
import com.example.listen.player.MusicPlayer;

import java.util.List;

public class MaterialListAdapter extends RecyclerView.Adapter<MaterialListAdapter.MaterialViewHolder> {

    private final LayoutInflater mInflater;
    private List<Material> materialList; // Cached copy
    private MusicPlayer player = MusicPlayer.getInstance();

    public MaterialListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_material, parent, false);
        MaterialViewHolder viewHolder = new MaterialViewHolder(itemView);

        viewHolder.cardView.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            Material material = materialList.get(position);
            Toast.makeText(v.getContext(), material.getName(), Toast.LENGTH_SHORT).show();
        });

        viewHolder.playButton.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            Material material = materialList.get(position);
            player.play(material);
            viewHolder.isPlaying = player.getIsPlaying();
            int id = viewHolder.isPlaying ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
            viewHolder.playButton.setImageResource(id);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        if (materialList != null) {
            Material current = materialList.get(position);
            if (current.equals(player.getPlayingMaterial())) {
                holder.isPlaying = player.getIsPlaying();
            } else {
                holder.isPlaying = Boolean.FALSE;
            }
            int id = holder.isPlaying ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp;
            holder.playButton.setImageResource(id);
            holder.materialTitle.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.materialTitle.setText("No Word");
        }
    }

    public void setMaterials(List<Material> materialList) {
        this.materialList = materialList;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (materialList != null)
            return materialList.size();
        else return 0;
    }

    static class MaterialViewHolder extends RecyclerView.ViewHolder {
        private final TextView materialTitle;
        private final CardView cardView;
        private final ImageButton playButton;
        private Boolean isPlaying;

        private MaterialViewHolder(View itemView) {
            super(itemView);
            materialTitle = itemView.findViewById(R.id.material_title);
            cardView = itemView.findViewById(R.id.material_list_main_page);
            playButton = itemView.findViewById(R.id.play_button_item);
            isPlaying = false;
        }
    }
}