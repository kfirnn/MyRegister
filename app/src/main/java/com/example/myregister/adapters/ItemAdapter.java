package com.example.myregister.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myregister.R;
import com.example.myregister.model.Item;
import com.example.myregister.services.DatabaseService;
import com.example.myregister.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private DatabaseService databaseService;


    public interface ItemClickListener {
        void onClick(Item item);
    }

    private static final String TAG = "ItemsAdapter";
    private List<Item> originalItemsList;
    private List<Item> filteredItemsList;
    private Context context;


    @Nullable
    private final ItemClickListener itemClickListener;

    public ItemAdapter(List<Item> itemsList, Context context, @Nullable final ItemClickListener itemClickListener) {
        this.originalItemsList = itemsList;
        this.filteredItemsList = new ArrayList<>(itemsList);
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = filteredItemsList.get(position);
        holder.bindItem(item);
    }

    @Override
    public int getItemCount() {
        return filteredItemsList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView previewImageView;
        private TextView previewTextView;
        private TextView previewPriceTextView;
        private String itemId;

        public ItemViewHolder(View itemView) {
            super(itemView);
            previewImageView = itemView.findViewById(R.id.itemImageView);
            previewTextView = itemView.findViewById(R.id.itemName);
            previewPriceTextView = itemView.findViewById(R.id.priceTextView);
        }

        public void bindItem(final Item item) {
            previewImageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPic()));
            previewTextView.setText(item.getName());
            previewPriceTextView.setText("₪" + item.getPrice());
            itemId = item.getId();

            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onClick(item);
                }
            });
        }

    }
}