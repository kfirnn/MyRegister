package com.example.myregister.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myregister.R;
import com.example.myregister.model.Item;
import com.example.myregister.utils.ImageUtil;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final List<Item> itemList;

    public ItemAdapter() {
        itemList = new ArrayList<>();
    }

    public void addItems(@NonNull List<Item> items) {
        for (Item item : items) {
            addItem(item);
        }
    }

    public void addItem(Item item) {
        if (item == null) return;

        itemList.add(item);
        notifyItemInserted(itemList.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        if (item == null) return;

        holder.nameTextView.setText(item.getName());
        holder.typeTextView.setText("Type: " + item.getType());
        holder.sizeTextView.setText("Size: " + item.getSize());
        holder.priceTextView.setText("Price: " + item.getPrice());

        holder.itemImageView.setImageBitmap(ImageUtil.convertFrom64base(item.getImg())); // מתקן לתמונה מבסיס 64
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameTextView, typeTextView, sizeTextView, priceTextView;
        public final ImageView itemImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            sizeTextView = itemView.findViewById(R.id.sizeTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
        }
    }
}


