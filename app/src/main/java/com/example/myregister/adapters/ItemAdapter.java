package com.example.myregister.adapters;

import android.content.Context;
import android.content.Intent;
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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemsList;
    private Context context;

    public ItemAdapter(List<Item> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemsList.get(position);
        holder.bindItem(item);
    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView name;
        private TextView price;
        // private Button addToCartButton;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.itemImageView);
            name = itemView.findViewById(R.id.nameTextView);
            price = itemView.findViewById(R.id.priceTextView);
            //   addToCartButton = itemView.findViewById(R.id.addToCartButton);

            // הוספת לחיצה על כל מוצר כדי להוביל לדף המידע של המוצר
            itemView.setOnClickListener(v -> {
                Item item = itemsList.get(getAdapterPosition());  // מקבל את המוצר שנלחץ
                // Intent intent = new Intent(context, ItemDetailActivity.class);
                //  intent.putExtra("itemId", item.getId()); // שולח את ה-ID של המוצר
                //   context.startActivity(intent);
            });
        }

        public void bindItem(Item item) {
            iv.setImageBitmap(ImageUtil.convertFrom64base(item.getImg()));
            name.setText(item.getName());
            price.setText("₪" + item.getPrice());

            //    addToCartButton.setOnClickListener(v -> {
            //        ((RecyclerViewActivity) context).addItemToCart(item); // הוספת המוצר לעגלה
            //   });
        }
    }
}



