package com.example.holoc.scanqrcode.activity.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.model.Users;
import com.example.holoc.scanqrcode.ultil.CheckConnect;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChiTietUserActivity extends AppCompatActivity {

    private TextView txtName, txtMssv, txtDonvi, txtSDT, txtTrangThai;
    private ImageView imageUser;
    private Button btnXacnhan, btnHuy;

    private DatabaseReference UsersDatabase;
    private FirebaseUser current_user;
    private ProgressDialog progressDialog;

    private String user_id;
    private ArrayList<String> mangQuyen;
    private Spinner spinnerQuyen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_user);
        Anhxa();
        GetData();
        XacNhan();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        XacNhan();
//    }

    private void XacNhan() {

//        btnXacnhan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // xác nhận
//                if(doiTrangThaiNguoc(txtTrangThai.getText().toString()).equals("0")){
//
//                    btnXacnhan.setVisibility(View.VISIBLE);
//                    btnHuy.setVisibility(View.INVISIBLE);
//                    // get dữ liệu của từng người dùng
//                    UsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            UsersDatabase.child("xacnhan").setValue("1");
//                            btnXacnhan.setVisibility(View.INVISIBLE);
//                            btnHuy.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }else{
//                    btnXacnhan.setVisibility(View.VISIBLE);
//                    btnHuy.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//
//        btnHuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(doiTrangThaiNguoc(txtTrangThai.getText().toString()).equals("0")){
//                    btnXacnhan.setVisibility(View.VISIBLE);
//                    btnHuy.setVisibility(View.INVISIBLE);
//                }else{
//                    btnXacnhan.setVisibility(View.INVISIBLE);
//                    btnHuy.setVisibility(View.VISIBLE);
//                    UsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            UsersDatabase.child("xacnhan").setValue("0");
//                            btnXacnhan.setVisibility(View.VISIBLE);
//                            btnHuy.setVisibility(View.INVISIBLE);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
//                }
//            }
//        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietUserActivity.this, DanhSachDangKyActivity.class);
                // giữ màn hình khi thoát ra
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // CheckConnect.ShowToast(getApplicationContext(), spinnerQuyen.getSelectedItemPosition() + "");
                ActionProgressbar("Thay đổi quyền", "Vui lòng chờ đang thay đổi quyền");
                xacNhanThayDoiQuyen();

            }
        });




    }


    private void GetData() {


        ActionProgressbar("Lấy dữ liệu", "Đang lấy dữ liệu");

        Bundle bundle = getIntent().getBundleExtra("dulieu");

        if(bundle!=null){
            Users users = (Users) bundle.getSerializable("model");

          //  Toast.makeText(getApplicationContext(),users.getName(),Toast.LENGTH_SHORT).show();
            String name = users.getName();
            String mssv = users.getMssv();
            String image = users.getImage();
            String sdt = users.getSdt();
            String quyen = users.getQuyen().trim();
            //String xacnhan = users.getXacnhan();
            String donvi = users.getDonvi();

            // gán vào
            txtName.setText(name);
            txtMssv.setText(mssv);
            txtDonvi.setText(donvi);
            txtSDT.setText(sdt);

            Picasso.with(ChiTietUserActivity.this).load(image)
                    .placeholder(R.drawable.no_image_available)
                    .error(R.drawable.err)
                    .into(imageUser);
            //CheckConnect.ShowToast(getApplicationContext(),quyen);
            // sự kiện Sponner
            // truyền dữ liệu
            // phần tử có quyền sẵn sẽ được đánh dấu đầu tiên
            // ví dụ gửi dữ liệu là có quyền Admin thì Admin sẽ được đánh dấu trước
            CatchEvenSpinnerQuyen(doiQuyen(quyen));

            progressDialog.dismiss();


        }





//        UsersDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String xacnhan = dataSnapshot.child("xacnhan").getValue().toString();
//                txtTrangThai.setText(doiTrangThai(xacnhan));
//                if(xacnhan.equals("0")){
//                    btnXacnhan.setVisibility(View.VISIBLE);
//                    btnHuy.setVisibility(View.INVISIBLE);
//                }else{
//                    btnXacnhan.setVisibility(View.INVISIBLE);
//                    btnHuy.setVisibility(View.VISIBLE);
//                }
//
//
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    // đổi quyền: từ số sang chữ
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

    private void CatchEvenSpinnerQuyen(String quyen) {
        mangQuyen = new ArrayList<>();

        // danh sách quyền
        mangQuyen.add(0, "Sinh viên");
        mangQuyen.add(1, "Người quyét");
        mangQuyen.add(2, "Admin");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (
                        this,
                        R.layout.spinner_item,
                        mangQuyen

                );

        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spinnerQuyen.setAdapter(adapter);
        // quyền được chọn đầu tiên
        if(quyen !=null){
            int spinnerPosition = adapter.getPosition(quyen);
            spinnerQuyen.setSelection(spinnerPosition);
        }

    }
//
//    private String doiTrangThai(String xacnhan){
//        if(xacnhan.equals("0"))
//            return "Chưa xác nhận";
//        else
//            return "Đã xác nhận";
//    }
//    private String doiTrangThaiNguoc(String xacnhan){
//        if(xacnhan.equals("Chưa xác nhận")) return "0";
//        else return "1";
//    }


    // ProgressBar
    private void ActionProgressbar(String title, String message) {
        progressDialog = new ProgressDialog(ChiTietUserActivity.this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    // alter xác nhận
    public void xacNhanThayDoiQuyen(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("XÁC NHẬN!");
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setMessage("Bạn có chắc chắn thay đổi quyền?");
        alert.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UsersDatabase.child("quyen").setValue(spinnerQuyen.getSelectedItemPosition() + "");
                        //saveAccountQuyen(spinnerQuyen.getSelectedItemPosition() + "");
                        progressDialog.dismiss();
                        CheckConnect.ShowToast(getApplicationContext(),"Thay đổi quyền thành công");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        alert.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog.hide();
            }
        });
        alert.show();

    }

//    private void saveAccountQuyen(String quyen) {
//        SharedPreferences settings = getSharedPreferences("preferencesQuyen", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        // edit
//        editor.putString("PREF_Quyen", quyen);
//        editor.commit();
//    }


    private void Anhxa() {
        txtName = (TextView) findViewById(R.id.chitiet_user_textTen);
        txtMssv = (TextView) findViewById(R.id.chitiet_user_textmssv);
        txtDonvi = (TextView) findViewById(R.id.chitiet_user_textdv);
        txtSDT = (TextView) findViewById(R.id.chitiet_user_textsdt);
        //txtTrangThai = (TextView) findViewById(R.id.chitiet_user_trangthai);
        imageUser = (ImageView) findViewById(R.id.chitiet_user_image);
        btnXacnhan = (Button) findViewById(R.id.chitiet_user_btnXacNhan);
        btnHuy = (Button) findViewById(R.id.chitiet_user_btnhuy);

        spinnerQuyen = (Spinner) findViewById(R.id.spinner_chitiet_user_quyen);


        current_user = FirebaseAuth.getInstance().getCurrentUser();

        Bundle bundle = getIntent().getBundleExtra("dulieu");
        user_id = bundle.getString("user_id");
        // get dữ liệu của từng người dùng
        UsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);


    }
}
