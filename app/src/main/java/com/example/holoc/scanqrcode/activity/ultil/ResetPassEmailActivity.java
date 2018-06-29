package com.example.holoc.scanqrcode.activity.ultil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.ultil.CheckConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ResetPassEmailActivity extends AppCompatActivity {

    Button btnXacnhan, btnHuy;
    EditText editEmail;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_email);
        AnhXa();
        if (CheckConnect.haveNetworkConnection(getApplicationContext())) {
            ActionClick();
        } else {
            CheckConnect.ShowToast(getApplicationContext(), "Vui lòng kiểm tra kết nối");
        }


    }

    private void ActionClick() {


        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassEmailActivity.this, DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionProgressbar("Lấy mật khẩu", "Đang lấy mật khẩu người dùng...");
                final String email = editEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplication(), "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show();
                }else{
                    if (isEmailValid(email)) {
                        // kiểm tra tài khoản có tồn tại không
                        mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                boolean check = !task.getResult().getProviders().isEmpty();
                                if(!check){
                                    progressDialog.hide();
                                    CheckConnect.ShowToast(getApplicationContext(),"Tài khoản không tồn tại");
                                }else{
                                    sendPassResetEmail(email);
                                }
                            }
                        });

                    }else{
                        progressDialog.hide();
                        Toast.makeText(getApplication(), "Địa chỉ email không đúng định dạng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void sendPassResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(ResetPassEmailActivity.this, DangNhapActivity.class);
                    // giữ màn hình khi thoát ra
                    startActivity(intent);
                    CheckConnect.ShowToast(getApplicationContext(), "Chúng tôi đã gửi mật khẩu đến email của bạn");
                    finish();

                } else {
                    progressDialog.hide();
                    CheckConnect.ShowToast(getApplicationContext(), "Lỗi: mật khẩu không được gửi đến email");
                }
            }
        });

    }

    private boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    private void AnhXa() {
        btnXacnhan = (Button) findViewById(R.id.btn_reset_tieptuc);
        btnHuy = (Button) findViewById(R.id.btn_reset_trove);
        editEmail = (EditText) findViewById(R.id.edit_resetpass_email);
        mAuth = FirebaseAuth.getInstance();

        String newString;

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            newString= null;
        } else {
            newString= extras.getString("emailUser").trim();
            editEmail.setText(newString);
        }

    }

    // ProgressBar
    private void ActionProgressbar(String title, String message) {
        progressDialog = new ProgressDialog(ResetPassEmailActivity.this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
}
