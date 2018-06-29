package com.example.holoc.scanqrcode.activity.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.model.SuKien;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ChiTietSuKienActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String key;
    private ArrayList<SuKien> mang;
    private int item;
    private ArrayList<String> mangdonvi;
    private int macapnhat;
    private EditText sngay;
    private TextView ngay;
    private EditText stgbd;
    private EditText stgkt;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.xemdsddoption :
                int vitri = getIntent().getExtras().getInt("Position");
                ArrayList<SuKien> mangsk = (ArrayList<SuKien>)getIntent().getExtras().getSerializable("mangsukien");
                Intent i1 = new Intent(getApplicationContext(), XemDanhSachDiemDanhActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("vitri",vitri);
                bundle1.putSerializable("mangsk",mangsk);
                i1.putExtras(bundle1);
                startActivity(i1);
                return true;
            case R.id.xemdsdkoption :
                int vitri1 = getIntent().getExtras().getInt("Position");
                ArrayList<SuKien> mangsk1 = (ArrayList<SuKien>)getIntent().getExtras().getSerializable("mangsukien");
                Intent i2 = new Intent(getApplicationContext(), XemDanhSachDangKyActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("vitri1",vitri1);
                bundle2.putSerializable("mangsk1",mangsk1);
                i2.putExtras(bundle2);
                startActivity(i2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_su_kien,menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_su_kien);

        final TextView ten = (TextView)findViewById(R.id.ten);
        final TextView donvi = (TextView)findViewById(R.id.donvi);
        ngay = (TextView)findViewById(R.id.ngay);
        final TextView tg = (TextView)findViewById(R.id.tg);
        //  final TextView tgkt = (TextView)findViewById(R.id.tgkt);
        final TextView chuthich = (TextView)findViewById(R.id.chuthich);
        final Button sua = (Button)findViewById(R.id.sua);
        final Button xoa = (Button)findViewById(R.id.xoa);
        final Toolbar tb = (Toolbar)findViewById(R.id.toolbarChiTietSuKien);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        Bundle bundle = getIntent().getExtras();
        //      Toast.makeText(ChiTietSuKienActivity.this,bundle+"",Toast.LENGTH_SHORT).show();

        item = bundle.getInt("Position");
        mang = (ArrayList<SuKien>) bundle.getSerializable("mangsukien");
        mangdonvi = (ArrayList<String>)bundle.getSerializable("mangdonvi");

        if(bundle != null){
            key = mang.get(item).getKey().toString();
            ten.setText(mang.get(item).getTensk().toString());
            donvi.setText(mang.get(item).getTendonvi().toString());
            ngay.setText(mang.get(item).getNgay().toString());
            tg.setText(mang.get(item).getTgbatdau().toString()+" - "+mang.get(item).getTgketthuc().toString());
            //   tgkt.setText(mang.get(item).getTgketthuc().toString());
            chuthich.setText(mang.get(item).getChuthich().toString());
        }


        sua.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {
                xacnhancapnhat();

            }
        });
        xoa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {
                xacnhanxoa();

            }
        });

    }

    public void xacnhanxoa(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("XÁC NHẬN!");
        alert.setIcon(R.drawable.delete_icon);
        alert.setMessage("Bạn có chắc chắn muốn xóa?");
        alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                myRef.child("sukien").child(key).removeValue();
                Toast.makeText(ChiTietSuKienActivity.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChiTietSuKienActivity.this,XemSuKienActivity.class);
                startActivity(intent);

            }
        });
        alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ChiTietSuKienActivity.this,XemSuKienActivity.class);
                startActivity(intent);
            }
        });
        alert.show();

    }

    public void xacnhancapnhat(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_sua_su_kien);
        // dialog.setTitle("CẬP NHẬT SỰ KIỆN");
        final EditText sten = (EditText)dialog.findViewById(R.id.suaten);
        sngay = (EditText)dialog.findViewById(R.id.suangay);
        stgbd = (EditText)dialog.findViewById(R.id.suatgbd);
        stgkt = (EditText)dialog.findViewById(R.id.suatgkt);
        final EditText schuthich = (EditText)dialog.findViewById(R.id.suachuthich);
        Spinner sdonvi = (Spinner)dialog.findViewById(R.id.suadonvi);
        Button capnhat = (Button)dialog.findViewById(R.id.capnhat);
        Button huy = (Button)dialog.findViewById(R.id.huy);
        final int madv=laymadv(mang.get(item).getTendonvi().toString());

        sten.setText(mang.get(item).getTensk().toString());
        sngay.setText(mang.get(item).getNgay().toString());
        stgbd.setText(mang.get(item).getTgbatdau().toString());
        stgkt.setText(mang.get(item).getTgketthuc().toString());
        schuthich.setText(mang.get(item).getChuthich().toString());
        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        mangdonvi
                );

        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);

        sdonvi.setAdapter(adapter);
        sdonvi.setSelection(madv);
        sdonvi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1,
                                       int arg2,
                                       long arg3) {

                macapnhat = arg2+1;

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                macapnhat = madv;

            }

        });


        capnhat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {


                SuKien sukien = new SuKien(sten.getText().toString(),macapnhat,sngay.getText().toString(),stgbd.getText().toString(),
                        stgkt.getText().toString(),schuthich.getText().toString());

                myRef.child("sukien").child(key).setValue(sukien);
                Toast.makeText(ChiTietSuKienActivity.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Intent i = new Intent(ChiTietSuKienActivity.this, XemSuKienActivity.class);
                startActivity(i);
            }
        });

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sngay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {
                chonNgay();
                //   Toast.makeText(ChiTietSuKienActivity.this, "fff", Toast.LENGTH_SHORT).show();
            }
        });

        stgbd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {
                chonThoiGianBatDau();

            }
        });

        stgkt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            {
                chonThoiGianKetThuc();

            }
        });

        dialog.show();

    }

    public int laymadv(String tendv){
        int ma;
        for(int i=0; i<mangdonvi.size(); i++)
            if(mangdonvi.get(i).equals(tendv))
                return i;
        return 0;
    }

    public void chonNgay(){
        final Calendar calendar= Calendar.getInstance();
        String gngay = ngay.getText().toString().substring(0,2);
        String gthang = ngay.getText().toString().substring(3,5);
        String gnam =ngay.getText().toString().substring(6,10);
        int ngay = Integer.parseInt(gngay);
        int thang = Integer.parseInt(gthang)-1;
        int nam = Integer.parseInt(gnam);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ChiTietSuKienActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                sngay.setText(simpleDateFormat.format(calendar.getTime()));

            }
        },nam,thang,ngay);
        datePickerDialog.show();


    }

    public void chonThoiGianBatDau(){
        final Calendar calendar= Calendar.getInstance();
        String ggio = mang.get(item).getTgbatdau().toString().substring(0,2);
        String gphut = mang.get(item).getTgbatdau().toString().substring(3,5);
        int gio = Integer.parseInt(ggio);
        int phut = Integer.parseInt(gphut);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                if(i<10) {
                    if (i1 < 10)
                        stgbd.setText("0" + i + ":0" + i1);
                    else
                        stgbd.setText("0"+i +":"+ i1);

                }
                else {
                    if (i1 < 10)
                        stgbd.setText(i + ":0" + i1);
                    else
                        stgbd.setText(i + ":"+i1);
                }

            }
        },gio,phut,false);
        timePickerDialog.show();

    }

    public void chonThoiGianKetThuc(){
        final Calendar calendar= Calendar.getInstance();
        String ggio = mang.get(item).getTgketthuc().toString().substring(0,2);
        String gphut = mang.get(item).getTgketthuc().toString().substring(3,5);
        int gio = Integer.parseInt(ggio);
        int phut = Integer.parseInt(gphut);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                if(i<10) {
                    if (i1 < 10)
                        stgkt.setText("0" + i + ":0" + i1);
                    else
                        stgkt.setText("0"+i +":"+ i1);

                }
                else {
                    if (i1 < 10)
                        stgkt.setText(i + ":0" + i1);
                    else
                        stgkt.setText(i + ":"+i1);
                }


            }
        },gio,phut,false);
        timePickerDialog.show();

    }


}
