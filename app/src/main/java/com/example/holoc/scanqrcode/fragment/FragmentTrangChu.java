package com.example.holoc.scanqrcode.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.holoc.scanqrcode.R;

/**
 * Created by holoc on 6/24/2018.
 */

public class FragmentTrangChu extends Fragment {

//    ListView listView;
//    SuKienAdapter adapter;
//    ArrayList<SuKien> suKienArrayList;
//    View view;
//    DatabaseReference mDatabase;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_trangchu_user,container, false);
        //AnhXa();
        return view;
    }
//
//    private void AnhXa() {
//        listView = view.findViewById(R.id.listviewDangKiSuKienUser);
//        suKienArrayList = new ArrayList<>();
//        adapter = new SuKienAdapter(getActivity(), R.layout.activity_su_kien_adapter,suKienArrayList);
//        listView.setAdapter(adapter);
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        mDatabase.child("sukien").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot item: dataSnapshot.getChildren()){
//                    suKienArrayList.add(new SuKien(item.child("tensk").getValue(String.class),
//                            item.child("ngay").getValue(String.class),
//                            item.child("tgbatdau").getValue(String.class)
//                            ));
//                }
//                adapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }
}
