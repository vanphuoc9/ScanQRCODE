package com.example.holoc.scanqrcode.activity.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.adapter.SuKienAdapter;
import com.example.holoc.scanqrcode.model.DanhSachDiemDanh;
import com.example.holoc.scanqrcode.model.DonVi;
import com.example.holoc.scanqrcode.model.SuKien;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class XemSuKienActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<SuKien> msk;
    private ArrayList<DonVi> mdv;
    private ArrayList<String>mangtendv;
    private ArrayList<SuKien> mangtamsk;
    private FloatingActionButton btn_themsukien;
    private EditText ngay;
    private EditText tgbatdau;
    private EditText tgketthuc;
    private SuKien sukien;
    private int madv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_su_kien);

        Toolbar tb = (Toolbar) findViewById(R.id.toolBarXemSuKien);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ListView lv = (ListView) findViewById(R.id.listview);
        btn_themsukien = (FloatingActionButton) findViewById(R.id.btn_themsukien);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        msk = new ArrayList<SuKien>();
        mdv = new ArrayList<DonVi>();
        mangtendv = new ArrayList<String>();
        mangtamsk = new ArrayList<SuKien>();

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mdv.clear();
                msk.clear();
                mangtendv.clear();
                mangtamsk.clear();
                for (DataSnapshot item : dataSnapshot.child("donvi").getChildren()) {

                    String tendv = item.child("tendv").getValue(String.class);
                    int madonvi = item.child("madv").getValue(Integer.class);
                    final DonVi donvi = new DonVi(madonvi,tendv);
                    mdv.add(donvi);
                    mangtendv.add(tendv);
                }

                for (DataSnapshot item : dataSnapshot.child("sukien").getChildren()) {
                    String key = item.getKey();
                    String tensk = item.child("tensk").getValue(String.class);
                    int madv = item.child("madv").getValue(Integer.class);
                    String ngay = item.child("ngay").getValue(String.class);
                    String tgbatdau = item.child("tgbatdau").getValue(String.class);
                    String tgketthuc = item.child("tgketthuc").getValue(String.class);
                    String chuthich = item.child("chuthich").getValue(String.class);
                    ArrayList<DanhSachDiemDanh> mangmssv= new ArrayList<DanhSachDiemDanh>();

                    final SuKien sukien1 = new SuKien(key,tensk,mdv.get(madv-1).getTendv().toString(),ngay, tgbatdau, tgketthuc, chuthich);
                    mangtamsk.add(sukien1);
                }

                for(int i=mangtamsk.size()-1; i>=0; i--) {
                    msk.add(mangtamsk.get(i));

                }
                SuKienAdapter arrayAdapter = new SuKienAdapter(XemSuKienActivity.this, R.layout.activity_su_kien_adapter,msk);

                lv.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("SUKIEN",databaseError.getMessage());
            }

        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(getApplicationContext(), ChiTietSuKienActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Position",i);
                bundle.putSerializable("mangdonvi",mangtendv);
                bundle.putSerializable("mangsukien",msk);
                intent1.putExtras(bundle);
                getApplicationContext().startActivity(intent1);
//Toast.makeText(XemSuKienActivity.this,mangdsdd.get(i).getMssv()+"",Toast.LENGTH_SHORT).show();
            }
        });


        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        ThemSuKien();
    }

    private void ThemSuKien() {
        btn_themsukien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogThemSuKien();
            }
        });

    }


    private void DialogThemSuKien() {
        final Dialog dialog = new Dialog(XemSuKienActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dùng để goi layout dialog
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.activity_sua_su_kien);


        final EditText tensk = (EditText)dialog.findViewById(R.id.suaten);
        ngay = (EditText)dialog.findViewById(R.id.suangay);
        tgbatdau = (EditText)dialog.findViewById(R.id.suatgbd);
        tgketthuc = (EditText)dialog.findViewById(R.id.suatgkt);
        final EditText chuthich = (EditText)dialog.findViewById(R.id.suachuthich);
        Spinner donvi = (Spinner)dialog.findViewById(R.id.suadonvi);
        Button tao = (Button)dialog.findViewById(R.id.capnhat);
        Button huy = (Button)dialog.findViewById(R.id.huy);

        ngay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {
                chonNgay();
            }
        });

        tgbatdau.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {
                chonThoiGianBatDau();
            }
        });

        tgketthuc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {
                chonThoiGianKetThuc();
            }
        });



        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        mangtendv


                );

        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);

        donvi.setAdapter(adapter);

        donvi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1,
                                       int arg2,
                                       long arg3) {

                madv = arg2+1;

            }
            //Nếu không chọn gì cả
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                madv = 1;

            }

        });

        tao.setText("Thêm");

        tao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {

                sukien = new SuKien(tensk.getText().toString(),madv,ngay.getText().toString(),tgbatdau.getText().toString(),
                        tgketthuc.getText().toString(),chuthich.getText().toString());

                myRef.child("sukien").push()
                        .setValue(sukien)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(XemSuKienActivity.this,"Tạo thành công",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(XemSuKienActivity.this,"Tạo thất bại",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
            }
        });

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void chonNgay(){
        final Calendar calendar= Calendar.getInstance();
        int gngay = calendar.get(Calendar.DATE);
        int gthang = calendar.get(Calendar.MONTH);
        int gnam = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(XemSuKienActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                ngay.setText(simpleDateFormat.format(calendar.getTime()));

            }
        },gnam,gthang,gngay);
        datePickerDialog.show();

    }

    public void chonThoiGianBatDau(){
        final Calendar calendar= Calendar.getInstance();
        final int gio = calendar.get(Calendar.HOUR_OF_DAY);
        final int phut = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(XemSuKienActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                if(i<10) {
                    if (i1 < 10)
                        tgbatdau.setText("0" + i + ":0" + i1);
                    else
                        tgbatdau.setText("0"+i +":"+ i1);

                }
                else {
                    if (i1 < 10)
                        tgbatdau.setText(i + ":0" + i1);
                    else
                        tgbatdau.setText(i + ":"+i1);
                }

            }
        },gio,phut,false);
        timePickerDialog.show();

    }

    public void chonThoiGianKetThuc(){
        final Calendar calendar= Calendar.getInstance();
        final int gio = calendar.get(Calendar.HOUR_OF_DAY);
        final int phut = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(XemSuKienActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                if(i<10) {
                    if (i1 < 10)
                        tgketthuc.setText("0" + i + ":0" + i1);
                    else
                        tgketthuc.setText("0"+i +":"+ i1);

                }
                else {
                    if (i1 < 10)
                        tgketthuc.setText(i + ":0" + i1);
                    else
                        tgketthuc.setText(i + ":"+i1);
                }

            }
        },gio,phut,false);
        timePickerDialog.show();

    }


}
