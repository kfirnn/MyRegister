package com.example.myregister.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myregister.R;
import com.example.myregister.model.Order;
import com.example.myregister.models.CartItem;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private final List<Order> orderList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        
        // Format order details
        String orderDate = dateFormat.format(new Date(order.getOrderDate()));
        StringBuilder itemsText = new StringBuilder();
        for (CartItem item : order.getItems()) {
            itemsText.append(item.getName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append("\n");
        }

        // Set the text views
        holder.tvOrderId.setText("Order ID: " + order.getOrderId());
        holder.tvUserName.setText("Customer: " + order.getUserName());
        holder.tvOrderDate.setText("Date: " + orderDate);
        holder.tvItems.setText(itemsText.toString().trim());
        holder.tvTotal.setText(String.format(Locale.getDefault(), "Total: ₪%.2f", order.getTotalAmount()));
        holder.tvStatus.setText("Status: " + order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvOrderId;
        public final TextView tvUserName;
        public final TextView tvOrderDate;
        public final TextView tvItems;
        public final TextView tvTotal;
        public final TextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvItems = itemView.findViewById(R.id.tvItems);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
} 