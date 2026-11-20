
package com.example.uidesignlab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserRecord> users = new ArrayList<>();

    // Interface for click listener
    public interface OnUserClickListener {
        void onUserClick(UserRecord user);
    }

    private OnUserClickListener listener;

    public UserAdapter(OnUserClickListener listener) {
        this.listener = listener;
    }

    public void setUsers(List<UserRecord> newUsers) {
        users = new ArrayList<>(newUsers);
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView txtItem;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItem = itemView.findViewById(R.id.txtItem);
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull UserViewHolder holder, int position) {

        UserRecord user = users.get(position);

        holder.txtItem.setText(user.id + ". " + user.name);

        // Delete user when row is clicked
        holder.itemView.setOnClickListener(v -> {

            if (listener != null) {
                listener.onUserClick(user);
            }

        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
