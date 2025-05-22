package com.example.myregister.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myregister.R;
import com.example.myregister.model.Item;
import com.example.myregister.utils.ImageUtil;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> items;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
        void onAddToCartClick(Item item);
    }

    public ItemAdapter(List<Item> items, Context context, OnItemClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        
        holder.nameTextView.setText(item.getName());
        holder.typeTextView.setText("Type: " + item.getType());
        holder.priceTextView.setText("Price: ₪" + item.getPrice());

        if (item.getPic() != null && !item.getPic().isEmpty()) {
            holder.itemImageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPic()));
        } else {
            holder.itemImageView.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.addToCartButton.setOnClickListener(v -> listener.onAddToCartClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView nameTextView;
        TextView typeTextView;
        TextView priceTextView;
        Button addToCartButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}