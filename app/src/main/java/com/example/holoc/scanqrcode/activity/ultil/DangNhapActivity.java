package com.example.holoc.scanqrcode.activity.ultil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.activity.admin.MainActivity;
import com.example.holoc.scanqrcode.activity.manager.ManagersActivity;
import com.example.holoc.scanqrcode.activity.user.UsersActivity;
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

public class DangNhapActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPass;
    private Button buttonDangnhap, buttonTrove;
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private TextView txtDangky, txtResetPass;

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";

    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        AnhXa();
      //  ActionToolbar();
        if (CheckConnect.haveNetworkConnection(getApplicationContext())) {
            ActionClick();
        } else {
            CheckConnect.ShowToast(getApplicationContext(), "Vui lòng kiểm tra kết nối");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //saveAccount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadPreferences();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // khi app mở lên nếu tài khoản đã đăng nhập thì chuyển qua màn hình màn theo quyền
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//
//            String current_user_id = mAuth.getCurrentUser().getUid();
////            loadPreferencesQuyen();
//
////            // get du lieu
//            mUserDatabase.child(current_user_id).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    String quyen = dataSnapshot.child("quyen").getValue().toString().trim();
//                    // String xacnhan = dataSnapshot.child("xacnhan").getValue().toString().trim();
//                    DangNhap_TheoQuyen(quyen);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        } else {
//            // No user is signed in
//        }
    }

    private void loadPreferencesQuyen() {
        SharedPreferences settings = getSharedPreferences("preferencesQuyen",Context.MODE_PRIVATE);
        String quyen = settings.getString("PREF_Quyen","0");
       // CheckConnect.ShowToast(getApplicationContext(),quyen);
        DangNhap_TheoQuyen(quyen);

    }

    private void loadPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        editTextEmail.setText(UnameValue);
        editTextPass.setText(PasswordValue);
    }

    private void saveAccount() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = editTextEmail.getText().toString().trim();
        PasswordValue = editTextPass.getText().toString().trim();
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.commit();

    }
    private void saveAccountQuyen(String quyen) {
        SharedPreferences settings = getSharedPreferences("preferencesQuyen",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        // edit
        editor.putString("PREF_Quyen", quyen);
        editor.commit();
    }

    private void ActionClick() {
        buttonDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String pass = editTextPass.getText().toString().trim();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {

                    ActionProgressbar("Đăng nhập tài khoản","Vui lòng chờ giây lát để hoàn thành đăng nhập");
                    // kiểm tra định dạng email và đăng nhập
                    checkEmail(email, pass);

                } else {
                    CheckConnect.ShowToast(getApplicationContext(), "Vui lòng điền đầy đủ thông tin đăng nhập");
                }
            }
        });

        txtDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                Intent intent = new Intent(DangNhapActivity.this, ResetPassEmailActivity.class);
                intent.putExtra("emailUser",email);
                startActivity(intent);
                finish();
            }
        });

    }
    /// Check Account already exists
    private void checkEmail(final String email, final String pass) {
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
                        progressDialog.hide();
                        CheckConnect.ShowToast(getApplicationContext(),"Không tìm thấy tài khoản của bạn");
                    }else{
                        //progressDialog.dismiss();
                        Dangnhap_Taikhoan(email, pass);

                    }
                }
            });
        }else{
            progressDialog.hide();
            CheckConnect.ShowToast(getApplicationContext(),"Vui lòng kiểm tra định dạng email");
        }


    }
    // ProgressBar
    private void ActionProgressbar(String title, String message) {
        progressDialog = new ProgressDialog(DangNhapActivity.this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void Dangnhap_Taikhoan(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    // id user
                    String current_user_id = mAuth.getCurrentUser().getUid();

                    // get du lieu
                    mUserDatabase.child(current_user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String quyen = dataSnapshot.child("quyen").getValue().toString().trim();
                           // String xacnhan = dataSnapshot.child("xacnhan").getValue().toString().trim();
                            //saveAccountQuyen(quyen);
                            DangNhap_TheoQuyen(quyen);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    // ẩn progress
                    progressDialog.hide();
                    Log.w("signInWithEmail:failed", task.getException());
                    CheckConnect.ShowToast(getApplicationContext(), "Không thể đăng nhập tài khoản. Vui lòng kiểm tra tài khoản");
                }
            }
        });
    }



    private void DangNhap_TheoQuyen(String quyen) {
        FirebaseUser current_user = mAuth.getCurrentUser();
        // kiểm tra email đã được xác nhận chưa?
        boolean emailVerified = current_user.isEmailVerified();
        if (emailVerified){
            // quyền 0 sinh viên, quyền 1 ngưới quyets, quyền 2 admin
            if(quyen.equals("0")){
                Intent intent = new Intent(DangNhapActivity.this, UsersActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                CheckConnect.ShowToast(getApplicationContext(), "Đăng nhập tài khoản users thành công");
                finish();
            }
            if(quyen.equals("1")){
                Intent intent = new Intent(DangNhapActivity.this, ManagersActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                CheckConnect.ShowToast(getApplicationContext(), "Đăng nhập tài khoản quyét thành công");
                finish();
            }
            if (quyen.equals("2")){
                // tạo dữ liệu cho device

                Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                CheckConnect.ShowToast(getApplicationContext(), "Đăng nhập tài khoản admin thành công");
                finish();
            }
        }else{
            FirebaseAuth.getInstance().signOut();
            CheckConnect.ShowToast(getApplicationContext(), "Vui lòng đăng nhập email để kích hoạt tài khoản!!!");


        }

    }


//    private void ActionToolbar() {
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }

    private void AnhXa() {
        editTextEmail = (EditText) findViewById(R.id.edittextDangnhapEmail);
        editTextPass = (EditText) findViewById(R.id.edittextDangnhapPassWord);
        buttonDangnhap = (Button) findViewById(R.id.buttonDangnhap);
        txtDangky = (TextView) findViewById(R.id.txtDangNhap_Dangky);
        txtResetPass  = (TextView) findViewById(R.id.txtDangnhap_resetEmail);
        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }
}
