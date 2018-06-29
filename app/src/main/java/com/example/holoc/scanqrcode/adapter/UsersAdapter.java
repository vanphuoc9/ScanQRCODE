package com.example.holoc.scanqrcode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.model.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 29/06/2018.
 */

public class UsersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Users> arrayUsers;

    public UsersAdapter(Context context, ArrayList<Users> arrayUsers) {
        this.context = context;
        this.arrayUsers = arrayUsers;
    }

    @Override
    public int getCount() {
        return arrayUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder{
        TextView textViewName, textViewQuyen, textViewMssv;
        CircleImageView imageUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        // nếu chưa có dữ liệu
        if(viewHolder == null){
            viewHolder = new ViewHolder();
            // get service
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dong_recycler_users, null);

            // anh xa tung dong
            viewHolder.textViewName = convertView.findViewById(R.id.dong_user_txtTen);
            viewHolder.textViewMssv = convertView.findViewById(R.id.dong_user_txtmssv);
            viewHolder.textViewQuyen = convertView.findViewById(R.id.dong_user_txtXacnhan);
            viewHolder.imageUser = convertView.findViewById(R.id.dong_user_image);


            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();

        }

        //
        Users user = (Users) getItem(position);
        viewHolder.textViewName.setText(user.getName());
        viewHolder.textViewMssv.setText(user.getMssv());
        viewHolder.textViewQuyen.setText(doiQuyen(user.getQuyen()));
        Picasso.with(context).load(user.getImage())
                .error(R.drawable.err)
                .placeholder(R.drawable.no_image_available)
                .into(viewHolder.imageUser);

        return convertView;
    }

    private String doiQuyen(String number){
        int num = Integer.parseInt(number);
        String quyen = "";
        switch (num){
            case 0:
                quyen = "Sinh viên";
                break;
            case 1:
                quyen = "Người quyét";
                break;
            case 2:
                quyen = "Admin";
                break;
        }
        return quyen;

    }
}
