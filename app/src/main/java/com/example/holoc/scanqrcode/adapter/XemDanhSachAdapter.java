package com.example.holoc.scanqrcode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.model.DanhSachDiemDanh;

import java.util.ArrayList;
import java.util.Objects;

public class XemDanhSachAdapter extends BaseAdapter{

    private Context context;
    private int layout;
    private ArrayList<DanhSachDiemDanh> listmssv;

    public XemDanhSachAdapter(Context context, int layout, ArrayList<DanhSachDiemDanh> listmssv) {
        this.context = context;
        this.layout = layout;
        this.listmssv = listmssv;
    }
    public XemDanhSachAdapter(){

    }


    @Override
    public int getCount(){
        return listmssv.size();
    }
    @Override
    public Objects getItem(int i){
        return null;
    }
    @Override
    public long getItemId(int i){
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);
        TextView mssv = (TextView) view.findViewById(R.id.txtmssv);
        TextView gioDiemDanh = (TextView) view.findViewById(R.id.txtgiodiemdanh);
        mssv.setText(listmssv.get(i).getMssv());
        gioDiemDanh.setText(listmssv.get(i).getGioDiemDanh());
        return view;
    }

}
