package com.example.listen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.listen.R;
import com.example.listen.entity.Material;

import java.util.List;

public class MaterialListAdapter extends RecyclerView.Adapter<MaterialListAdapter.MaterialViewHolder> {

    private final LayoutInflater mInflater;
    private List<Material> materialList; // Cached copy of words

    public MaterialListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_material, parent, false);
        return new MaterialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MaterialViewHolder holder, int position) {
        if (materialList != null) {
            Material current = materialList.get(position);
            holder.wordItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.wordItemView.setText("No Word");
        }
    }

    public void setWords(List<Material> materialList) {
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

    class MaterialViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;

        private MaterialViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.material_title);
        }
    }
}