package com.example.bybloslogin.ui.login;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bybloslogin.R;

import java.util.List;

public class UserList extends ArrayAdapter<User> {

    private Activity context;
    List<User> users;

    public UserList(Activity context, List<User> users) {
        super(context, R.layout.layout_user_list, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);

        TextView textViewUsername = (TextView) listViewItem.findViewById(R.id.textViewUsername);
        TextView textViewAccountType = (TextView) listViewItem.findViewById(R.id.textViewAccountType);

        User user = users.get(position);
        textViewUsername.setText(user.getUsername());
        textViewAccountType.setText(user.getAccountType());
        return listViewItem;
    }
}
