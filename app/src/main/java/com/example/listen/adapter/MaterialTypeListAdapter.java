package com.example.listen.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.listen.R;
import com.example.listen.activity.type.MaterialTypeDetailActivity;
import com.example.listen.entity.MaterialType;

import java.util.List;

public class MaterialTypeListAdapter extends RecyclerView.Adapter<MaterialTypeListAdapter.MaterialTypeViewHolder> {

    private final LayoutInflater mInflater;
    private List<MaterialType> materialTypeList; // Cached copy

    public MaterialTypeListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public MaterialTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_material_type, parent, false);
        MaterialTypeViewHolder viewHolder = new MaterialTypeViewHolder(itemView);

        viewHolder.cardView.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            MaterialType materialType = materialTypeList.get(position);
            Intent intent = new Intent(mInflater.getContext(), MaterialTypeDetailActivity.class);
            intent.putExtra("typeId", materialType.getId());
            mInflater.getContext().startActivity(intent);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialTypeViewHolder holder, int position) {
        if (materialTypeList != null) {
            MaterialType current = materialTypeList.get(position);
            holder.title.setText(current.getName());
            Glide.with(mInflater.getContext()).load(current.getCoverId()).into(holder.cover);
        } else {
            // Covers the case of data not being ready yet.
            holder.title.setText("No Word");
        }
    }

    public void setMaterials(List<MaterialType> materialList) {
        this.materialTypeList = materialList;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (materialTypeList != null)
            return materialTypeList.size();
        else return 0;
    }

    static class MaterialTypeViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView title;
        private final ImageView cover;

        private MaterialTypeViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.material_type_list);
            title = itemView.findViewById(R.id.material_type_list_title);
            cover = itemView.findViewById(R.id.material_type_list_cover);
        }
    }
}