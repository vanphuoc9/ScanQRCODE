package com.example.holoc.scanqrcode.activity.ultil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.ultil.CheckConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;


public class DangKyActivity extends AppCompatActivity {
    private EditText editTextTen, editTextEmail, editTextPass, editTextLaiPass, editTextMssv;
    private Button buttonXacnhan;
    private TextView txtHuyDangKy;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private Spinner spinnerDonVi, spinnerNganh;

    private ArrayList<String> mangtendv, mangtennganh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        Anhxa();

        if (CheckConnect.haveNetworkConnection(getApplicationContext())) {
            CatchEvenSpinnerDonVi();
            CatchEvenSpinnerNganhDefault();
            ActionClick();

        } else {
            CheckConnect.ShowToast(getApplicationContext(), "Vui lòng kiểm tra kết nối");
        }


    }

    private void CatchEvenSpinnerNganhDefault() {
        mangtennganh = new ArrayList<>();

        mangtennganh.add(0,"Chọn ngành");
        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        mangtennganh

                );

        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);

        spinnerNganh.setAdapter(adapter);
    }

    // chọn ngành
    private void CatchEvenSpinnerDonVi() {
        mangtendv.add(0,"Chọn đơn vị");

        // lấy đối tượng theo key, value
        // ở đây key là ten đơn vị, value là key của đơn vị đó
        final HashMap<String, String> spinnerMap = new HashMap<String, String>();


        spinnerMap.put("Chọn đơn vị","0");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("donvi");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren())
                {   // lấy tên đơn vị và key của từng đơn vị
                    String id = item.getKey().toString().trim();
                    String ten = item.child("tendv").getValue().toString().trim();
                    // thêm vào mảng
                    mangtendv.add(ten);
                    // gán key theo tên
                    spinnerMap.put(ten, id);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // gán mảng vào spinner
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mangtendv);
        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spinnerDonVi.setAdapter(adapter);

        // sự kiện click spinner
        spinnerDonVi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // lấy tên đơn vị theo sự kiện click
                String tenDonVi = spinnerDonVi.getSelectedItem().toString().trim();
                // lấy key của từng đơn vị
                String key = spinnerMap.get(tenDonVi).trim();
                // kiểm tra có chọn đơn vị không?
                // lấy danh sách ngành theo từng đơn vị
                CheckDonVi(key);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

    }

    private void CheckDonVi(String key) {
        // nếu đơn vị không được chọn thì danh sách ngành trở lại default
        if(key.equals("0")){
            CatchEvenSpinnerNganhDefault();
           // CheckConnect.ShowToast(getApplicationContext(),"Vui lòng chọn đơn vị");

        }else{
            // lấy danh sách ngành theo key của từng đơn vị
            CatchEvenSpinnerNganh(key);

        }

    }

    private void CatchEvenSpinnerNganh(String key) {
        // cho mảng về rỗng

        // lấy đối tượng theo key, value
        // ở đây key là ten ngành, value là key của ngành đó
        mangtennganh = new ArrayList<>();
        mangtennganh.add(0,"Chọn ngành");

//        final HashMap<String, String> spinnerMapNganh = new HashMap<String, String>();
//        spinnerMapNganh.put("Chọn ngành","0");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("dsnganh");
        // lấy danh sách ngành theo key của từng đơn vị
        mDatabase.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    // lây key của từng ngành
                    String id = item.getKey().toString();
                    // get tên ngành
                    String tennganh = item.child("ten").getValue().toString().trim();
                    // gán tên vào mảng ngành
                    mangtennganh.add(tennganh);
//                    spinnerMapNganh.put(tennganh,id);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        mangtennganh

                );

        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);

        spinnerNganh.setAdapter(adapter);

//        spinnerNganh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                // lấy tên ngành và key của ngành đó
//                String ten = spinnerNganh.getSelectedItem().toString();
//                String keyNganh = spinnerMapNganh.get(ten).trim();
//               // CheckConnect.ShowToast(getApplicationContext(),ten+" "+keyNganh);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


    }


    private void ActionClick() {
        txtHuyDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = editTextTen.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String pass = editTextPass.getText().toString().trim();
                String passlai = editTextLaiPass.getText().toString().trim();
                String tenDonVi = spinnerDonVi.getSelectedItem().toString().trim();
                String tenNganh = spinnerNganh.getSelectedItem().toString().trim();
                String massv = editTextMssv.getText().toString().trim();
                if (!TextUtils.isEmpty(ten) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(massv) && !tenNganh.equals("Chọn ngành") && !tenDonVi.equals("Chọn đơn vị")) {
                    if(pass.equals(passlai)){
                        // tạo progerss
                        ActionProgressbar("Đăng ký tài khoản","Vui lòng chờ chờ giây lát để hoàn thành đăng ký");
                        // kiểm tra tài khoản đã tồn tại chưa
                        // nếu chưa tạo tài khoản mới
                        checkEmail(ten,email,massv, pass, tenDonVi, tenNganh);

                    }else{
                        CheckConnect.ShowToast(getApplicationContext(), "Mật khẩu không khớp nhau");
                    }

                } else {
                    CheckConnect.ShowToast(getApplicationContext(), "Vui lòng điền đầy đủ thông tin");
                }

            }
        });
    }

    /// Check Account already exists
    private void checkEmail(final String ten,final String email, final String massv,  final String pass, final String tenDonVi, final String tenNganh) {
        // kiểm tra định dạng email
        String emailPattern =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        if(email.matches(emailPattern)){
            /// Check Account already exists
            mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                    boolean check = !task.getResult().getProviders().isEmpty();
                    if(!check){
                        DangKy_TaiKhoan(ten, email,massv, pass,tenDonVi,tenNganh);
                    }else{
                        progressDialog.dismiss();
                        CheckConnect.ShowToast(getApplicationContext(),"Tài khoản đã tồn tại");
                    }
                }
            });
        }else{
            progressDialog.hide();
            CheckConnect.ShowToast(getApplicationContext(),"Vui lòng kiểm tra định dạng email");
        }


    }

    private void DangKy_TaiKhoan(final String ten, String email, final String massv, String pass, final String tenDonVi, final String tenNganh) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // người dùng hiện tại
                    final FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    // gửi email xác nhận
                    sendUsersEmailVerification();

                    // lấy id của users
                    String uid = current_user.getUid();
                    // lấy dữ liệu từ id của mỗi đối tượng
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    // đẩy dữ liệu lên firebase
                    HashMap<String, String> userMap = new HashMap<String, String>();
                    userMap.put("name", ten);
                    userMap.put("mssv", massv);
                    userMap.put("image", "default");
                    userMap.put("sdt","default");
                    userMap.put("device_token", deviceToken);
                    userMap.put("donvi",tenDonVi);
                    userMap.put("nganh",tenNganh);
                    userMap.put("xacnhan","0");
                    userMap.put("quyen","0");
                    // đẩy lên
                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // đẩy lên thành công cho chuyển qua Activity main
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                                // giữ màn hình khi thoát ra
                                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                CheckConnect.ShowToast(getApplicationContext(), "Đăng ký tài khoản thành công. Vui lòng xác nhận email để đăng nhập");



                            }
                        }
                    });

                } else {
                    // ẩn progress
                    progressDialog.hide();
                    CheckConnect.ShowToast(getApplicationContext(), "Không thể tạo tài khoản.");
                }
            }
        });
    }
    // gửi email xác nhận
    private void sendUsersEmailVerification() {
        // người dùng hiện tại
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        current_user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
//                    CheckConnect.ShowToast(getApplicationContext(),"Đã gửi email xác nhận");
                }else{
                    progressDialog.hide();
                    CheckConnect.ShowToast(getApplicationContext(),"Lỗi: gửi email xác nhận");
                }
            }
        });

    }

    private void Anhxa() {
        editTextTen = (EditText) findViewById(R.id.edittextDangKyTen);
        editTextEmail = (EditText) findViewById(R.id.edittextDangKyEmail);
        editTextPass = (EditText) findViewById(R.id.edittextDangKyPassWord);
        editTextLaiPass = (EditText) findViewById(R.id.edittextDangKyLaiPassWord);
        editTextMssv = (EditText) findViewById(R.id.edittextDangKyMssv);
        buttonXacnhan = (Button) findViewById(R.id.buttonDangky);
        txtHuyDangKy = (TextView) findViewById(R.id.textviewHuyDangky);
        spinnerDonVi = (Spinner) findViewById(R.id.spinner_donvi_dangky);
        spinnerNganh = (Spinner) findViewById(R.id.spinner_nganh_dangky);
        // khởi tạo FireBase
        mAuth = FirebaseAuth.getInstance();
        mangtendv = new ArrayList<>();

    }
    // ProgressBar
    private void ActionProgressbar(String title, String message) {
        progressDialog = new ProgressDialog(DangKyActivity.this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

}
