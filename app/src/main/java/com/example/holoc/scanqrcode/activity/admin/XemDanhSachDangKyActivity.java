package com.example.holoc.scanqrcode.activity.admin;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.adapter.XemDanhSachAdapter;
import com.example.holoc.scanqrcode.model.DanhSachDiemDanh;
import com.example.holoc.scanqrcode.model.SuKien;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class XemDanhSachDangKyActivity extends AppCompatActivity {
    private int vitri;
    private ArrayList<SuKien> mangsk;
    private ArrayList<DanhSachDiemDanh> mangsv;
    private String masukien;
    private FirebaseDatabase database  = FirebaseDatabase.getInstance();;
    private DatabaseReference myRef=  database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_danh_sach_dang_ky);
            final Toolbar tb = (Toolbar)findViewById(R.id.toolBarXemDanhSachDangKy);
            setSupportActionBar(tb);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            tb.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();

                }
            });

            vitri = getIntent().getExtras().getInt("vitri1");
            mangsk = (ArrayList<SuKien>)getIntent().getExtras().getSerializable("mangsk1");
            masukien = mangsk.get(vitri).getKey().toString();
            final ListView lv1 = (ListView)findViewById(R.id.lvdsdk);
            FloatingActionButton btn_themmssv = (FloatingActionButton)findViewById(R.id.btn_themsvdk);
            tb.setTitle(mangsk.get(vitri).getTensk());
            getSupportActionBar().setTitle(mangsk.get(vitri).getTensk());
            mangsv = new ArrayList<DanhSachDiemDanh>();


            myRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mangsv.clear();
                    for (DataSnapshot item : dataSnapshot.child("sukien").child(masukien).child("dsDangKy").getChildren()) {

                        String mssv = item.child("mssv").getValue(String.class);
                        String giodd = item.child("tgDangKy").getValue(String.class);
                        DanhSachDiemDanh diemdanh = new DanhSachDiemDanh(mssv,giodd);
                        mangsv.add(diemdanh);
                     //   Toast.makeText(XemDanhSachDangKyActivity.this,mssv,Toast.LENGTH_SHORT).show();
                    }

                    XemDanhSachAdapter arrayAdapter = new XemDanhSachAdapter(XemDanhSachDangKyActivity.this, R.layout.xem_danh_sach_adapter,mangsv);

                    lv1.setAdapter(arrayAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("",databaseError.getMessage());
                }

            });

            btn_themmssv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)

                {
                    final Dialog dialog = new Dialog(XemDanhSachDangKyActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.update_diemdanh);
                    final EditText edtmssv = (EditText)dialog.findViewById(R.id.editTextMSSV);
                    final EditText edtghichu = (EditText)dialog.findViewById(R.id.editTextGhiChu);
                    TextView title = (TextView)dialog.findViewById(R.id.title_bosung);
                    title.setText("ĐĂNG KÝ BỔ SUNG");
                    Button btnadd = (Button) dialog.findViewById(R.id.buttonAdd);
                    Button btnhuy = (Button) dialog.findViewById(R.id.buttonHuy);

                    btnadd.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)

                        {
                            DanhSachDiemDanh diemdanh = new DanhSachDiemDanh(edtmssv.getText().toString(),edtghichu.getText().toString());
                            myRef.child("sukien").child(masukien).child("dsDangKy").push()
                                    .setValue(diemdanh)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(XemDanhSachDangKyActivity.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // callback.onGetDataFailed(e.getMessage());
                                            Toast.makeText(XemDanhSachDangKyActivity.this,"Thêm không thành công",Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });

                        }
                    });

                    btnhuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });

        }

    }

