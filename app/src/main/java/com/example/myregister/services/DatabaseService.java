package com.example.myregister.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myregister.model.Item;
import com.example.myregister.model.User;
import com.example.myregister.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/// a service to interact with the Firebase Realtime Database.
/// this class is a singleton, use getInstance() to get an instance of this class
/// @see #getInstance()
/// @see FirebaseDatabase
public class DatabaseService {

    /// tag for logging
    /// @see Log
    private static final String TAG = "DatabaseService";

    /// callback interface for database operations
    /// @param <T> the type of the object to return
    /// @see DatabaseCallback#onCompleted(Object)
    /// @see DatabaseCallback#onFailed(Exception)
    public interface DatabaseCallback<T> {
        /// called when the operation is completed successfully
        void onCompleted(T object);

        /// called when the operation fails with an exception
        void onFailed(Exception e);
    }

    /// the instance of this class
    /// @see #getInstance()
    private static DatabaseService instance;

    /// the reference to the database
    /// @see DatabaseReference
    /// @see FirebaseDatabase#getReference()
    private final DatabaseReference databaseReference;

    /// use getInstance() to get an instance of this class
    /// @see DatabaseService#getInstance()
    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    /// get an instance of this class
    /// @return an instance of this class
    /// @see DatabaseService
    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }


    // private generic methods to write and read data from the database

    /// write data to the database at a specific path
    /// @param path the path to write the data to
    /// @param data the data to write (can be any object, but must be serializable, i.e. must have a default constructor and all fields must have getters and setters)
    /// @param callback the callback to call when the operation is completed
    /// @return void
    /// @see DatabaseCallback
    private void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback == null) return;
                callback.onCompleted(task.getResult());
            } else {
                if (callback == null) return;
                callback.onFailed(task.getException());
            }
        });
    }

    /// read data from the database at a specific path
    /// @param path the path to read the data from
    /// @return a DatabaseReference object to read the data from
    /// @see DatabaseReference

    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }


    /// get data from the database at a specific path
    /// @param path the path to get the data from
    /// @param clazz the class of the object to return
    /// @param callback the callback to call when the operation is completed
    /// @return void
    /// @see DatabaseCallback
    /// @see Class
    private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz);
            callback.onCompleted(data);
        });
    }

    /// generate a new id for a new object in the database
    /// @param path the path to generate the id for
    /// @return a new id for the object
    /// @see String
    /// @see DatabaseReference#push()

    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }

    // end of private methods for reading and writing data

    // public methods to interact with the database

    /// create a new item in the database
    /// @param item the item object to create
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive void
    ///             if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see Item
    public void createNewItem(@NotNull final Item item, @Nullable final DatabaseCallback<Void> callback) {
        writeData("items/" + item.getId(), item, callback);
    }

    /// get a user from the database
    /// @param uid the id of the user to get
    /// @param callback the callback to call when the operation is completed
    ///               the callback will receive the user object
    ///             if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see User
    public void getUser(@NotNull final String uid, @NotNull final DatabaseCallback<User> callback) {
        getData("users/" + uid, User.class, callback);
    }

    /// get a item from the database
    /// @param itemId the id of the item to get
    /// @param callback the callback to call when the operation is completed
    ///               the callback will receive the item object
    ///              if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see Item
    public void getItem(@NotNull final String itemId, @NotNull final DatabaseCallback<Item> callback) {
        getData("items/" + itemId, Item.class, callback);
    }

    /// generate a new id for a new item in the database
    /// @return a new id for the item
    /// @see #generateNewId(String)
    /// @see Item
    public String generateItemId() {
        return generateNewId("items");
    }

    /// get all the items from the database
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive a list of item objects
    ///            if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see List
    /// @see Item
    /// @see #getData(String, Class, DatabaseCallback)
    public void getItems(@NotNull final DatabaseCallback<List<Item>> callback) {
        readData("items").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Item> items = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Item item = dataSnapshot.getValue(Item.class);
                Log.d(TAG, "Got item: " + item);
                items.add(item);
            });

            callback.onCompleted(items);
        });
    }

    /// get all the users from the database
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive a list of item objects
    ///            if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see List

    public void getUsers(@NotNull final DatabaseCallback<List<User>> callback) {
        readData("Users").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<User> users = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                User user = dataSnapshot.getValue(User.class);
                Log.d(TAG, "Got user: " + user);
                users.add(user);
            });

            callback.onCompleted(users);
        });
    }

    /// get all orders from the database
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive a list of order objects
    ///            if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see List
    public void getOrders(@NotNull final DatabaseCallback<List<Order>> callback) {
        readData("Orders").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting orders", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Order> orders = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Order order = dataSnapshot.getValue(Order.class);
                Log.d(TAG, "Got order: " + order);
                if (order != null) {
                    orders.add(order);
                }
            });
            callback.onCompleted(orders);
        });
    }

    /// create a new order in the database
    /// @param order the order object to create
    /// @param callback the callback to call when the operation is completed
    ///               the callback will receive void
    ///              if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see Order
    public void createNewOrder(@NotNull final Order order, @Nullable final DatabaseCallback<Void> callback) {
        writeData("Orders/" + order.getOrderId(), order, callback);
    }

    /// generate a new id for a new order in the database
    /// @return a new id for the order
    /// @see #generateNewId(String)
    /// @see Order
    public String generateOrderId() {
        return generateNewId("Orders");
    }

    public void deleteUser(String uid, DatabaseCallback<Void> callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        userRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onCompleted(null);
            } else {
                callback.onFailed(task.getException());
            }
        });
    }


}
