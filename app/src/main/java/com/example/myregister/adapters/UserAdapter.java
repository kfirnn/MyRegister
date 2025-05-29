package com.example.myregister.adapters;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myregister.R;
import com.example.myregister.model.User;
import com.example.myregister.screens.ShowUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    /// list of users
    /// @see User
    private final List<User> userList;
    private final ShowUsers context;
    private final DatabaseReference usersRef;

    public UserAdapter(List<User> userList, ShowUsers showUsers) {
        this.userList = userList;
        this.context = showUsers;
        this.usersRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    /// create a view holder for the adapter
    /// @param parent the parent view group
    /// @param viewType the type of the view
    /// @return the view holder
    /// @see ViewHolder
    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /// inflate the item_selected_user layout
        /// @see R.layout.item_selected_user
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_user, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    /// bind the view holder with the data
    /// @param holder the view holder
    /// @param position the position of the item in the list
    /// @see ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null) return;

        holder.tvFname.setText(user.getfName());
        holder.tvLname.setText(user.getlName());
        holder.tvPhone.setText(user.getPhone());

        // Set up delete button click listener
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                .setTitle("מחיקת משתמש")
                .setMessage("האם אתה בטוח שברצונך למחוק את המשתמש " + user.getfName() + " " + user.getlName() + "?")
                .setPositiveButton("כן", (dialog, which) -> deleteUser(user, position))
                .setNegativeButton("לא", null)
                .show();
        });
    }

    private void deleteUser(User user, int position) {
        // Delete from Firebase
        usersRef.child(user.getId()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Remove from local list
                userList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, userList.size());
                Toast.makeText(context, "המשתמש נמחק בהצלחה", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "שגיאה במחיקת המשתמש", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /// get the number of items in the list
    /// @return the number of items in the list
    @Override
    public int getItemCount() {
        return userList.size();
    }

    /// View holder for the users adapter
    /// @see RecyclerView.ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvFname;
        public final TextView tvLname;
        public final TextView tvPhone;
        public final ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFname = itemView.findViewById(R.id.tvFname);
            tvLname = itemView.findViewById(R.id.tvLname);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            btnDelete = itemView.findViewById(R.id.btnDeleteUser);
        }
    }
}
