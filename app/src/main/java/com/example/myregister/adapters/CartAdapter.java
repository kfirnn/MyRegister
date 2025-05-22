package com.example.myregister.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myregister.R;
import com.example.myregister.models.CartItem;
import com.example.myregister.utils.CartManager;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> items = new ArrayList<>();
    private final Context context;
    private final CartManager cartManager;
    private OnCartItemRemovedListener onCartItemRemovedListener;

    public interface OnCartItemRemovedListener {
        void onCartItemRemoved();
    }

    public void setOnCartItemRemovedListener(OnCartItemRemovedListener listener) {
        this.onCartItemRemovedListener = listener;
    }

    public CartAdapter(Context context) {
        this.context = context;
        this.cartManager = CartManager.getInstance();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<CartItem> newItems) {
        items = new ArrayList<>(newItems);
        notifyDataSetChanged();
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView priceText;
        private final TextView quantityText;
        private final ImageButton decreaseButton;
        private final ImageButton increaseButton;
        private final ImageButton removeButton;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.item_name);
            priceText = itemView.findViewById(R.id.item_price);
            quantityText = itemView.findViewById(R.id.item_quantity);
            decreaseButton = itemView.findViewById(R.id.decrease_quantity);
            increaseButton = itemView.findViewById(R.id.increase_quantity);
            removeButton = itemView.findViewById(R.id.remove_item);
        }

        void bind(final CartItem item) {
            nameText.setText(item.getName());
            priceText.setText(String.format("₪%.2f", item.getPrice() * item.getQuantity()));
            quantityText.setText(String.valueOf(item.getQuantity()));

            decreaseButton.setOnClickListener(v -> {
                int newQuantity = item.getQuantity() - 1;
                if (newQuantity > 0) {
                    cartManager.updateQuantity(item.getItemId(), newQuantity);
                } else {
                    cartManager.removeFromCart(item.getItemId());
                }
            });

            increaseButton.setOnClickListener(v -> {
                cartManager.updateQuantity(item.getItemId(), item.getQuantity() + 1);
            });

            removeButton.setOnClickListener(v -> {
                // Remove item from local list immediately
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    items.remove(position);
                    notifyItemRemoved(position);
                    // Notify listener about the immediate removal
                    if (onCartItemRemovedListener != null) {
                        onCartItemRemovedListener.onCartItemRemoved();
                    }
                }
                
                // Then remove from Firebase
                cartManager.removeFromCart(item.getItemId());
                Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
