package com.example.holoc.scanqrcode.activity.admin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.activity.DanhSachDiemDanhActivity;
import com.example.holoc.scanqrcode.activity.ultil.DangNhapActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.example.holoc.scanqrcode.activity.DanhSachDiemDanhActivity;
//import com.github.clans.fab.FloatingActionButton;
//import com.github.clans.fab.FloatingActionMenu;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String scannedData;
    private Button scanBtn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private TextView textNameUserSignIn, textEmailUserSignIn;
    private CircleImageView circleImageUserSignIn;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;



    // private DatabaseReference mData;
    private static String mssv, ghiChu;

    private final Activity activity = this;

    //private FloatingActionMenu floatingMenu;
    private FloatingActionMenu floatingMenu;
    private FloatingActionButton floatingButtonDiemDanhBoSung, floatingButtonDanhSachDaQuet;
    private static String mssvDelete;
    //URL Delete sinh viên đã điểm danh
    static String urlUpdate = "https://script.google.com/macros/s/AKfycbxJQfZF1sz11C02O5GAtzbyjF7uBWbUOEpDWqhFGQ2fSvGUv8ur/exec?";
    private String quyen="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        // Tạo action bar
        Actionbar();
        /// phuoc llllg
//        CatchChonItemListViewMenu();
//        CatchChonITemListVIewSetting();
//
//
//        ActionClickIconListView();
//        ActionClickIconAgainView();
//        GetData();
        // jjjjjj


        //new UpdateDataActivity().execute();


        floatingButtonDiemDanhBoSung.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                DialogDiemDanhSinhVien();
                //arrayList.clear();
                //getDanhSachSinhVien();
                //adapter.notifyDataSetChanged();

            }
        });
        floatingButtonDanhSachDaQuet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                //listDSDiemDanh.setVisibility(View.VISIBLE);
                //getDanhSachSinhVien();
                startActivity(new Intent(MainActivity.this, DanhSachDiemDanhActivity.class));
            }
        });



        //DonVi donvi = new DonVi(2,"Khoa CNTT");
//        mData.child("data").push().setValue(donvi, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                if (databaseError==null){
//                    //Toast.makeText(getApplicationContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
//                }else {
//                    //Toast.makeText(getApplicationContext(), "Lưu thất bại!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        //Toast.makeText(this, "jhdfjsgd", Toast.LENGTH_SHORT).show();

//        mData.child("data").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                //Toast.makeText(MainActivity.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
//                DonVi donVi = dataSnapshot.getValue(DonVi.class);
//                //Toast.makeText(MainActivity.this, donVi.getTendv(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
         /*
        Bắt sự kiện điểm danh sinh viên thủ công (Thêm sinh viên) vào trang tính
         */

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Đặt QR Code hoặc Barcode cần quét theo đường chỉ ngang");
                integrator.setBeepEnabled(true);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });
    }

    private void GetData() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Users")
                            .child(current_uid);
        // Not load data again
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameUser = dataSnapshot.child("name").getValue().toString().trim();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        mCurrentUser = mAuth.getCurrentUser();

        // nếu không có tài khoản chuyển sang màn hình startActivity
        if(mCurrentUser == null){
            sendtoStart();

        }else{
            // get email user
            final String email = mCurrentUser.getEmail();
            mUserDatabase = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(mAuth.getCurrentUser().getUid());

            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue().toString().trim();
                    String image = dataSnapshot.child("image").getValue().toString().trim();
                    textNameUserSignIn.setText(name);
                    textEmailUserSignIn.setText(email);

                    Picasso.with(MainActivity.this).load(image)
                            .placeholder(R.drawable.no_image_available)
                            .error(R.drawable.err)
                            .into(circleImageUserSignIn);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

    private void sendtoStart() {
        Intent startIntent = new Intent(MainActivity.this, DangNhapActivity.class);
        startActivity(startIntent);
        finish();
    }



    private void Actionbar() {
        //Set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void AnhXa() {



        mAuth = FirebaseAuth.getInstance();


        toolbar             = (Toolbar) findViewById(R.id.toolBarScan);
        scanBtn             = (Button) findViewById(R.id.buttonScan);
        drawerLayout        = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView      = (NavigationView) findViewById(R.id.nav_view_main);
        // set navigation item selected
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_user);
        textNameUserSignIn = (TextView) headerLayout.findViewById(R.id.navTextViewNameUser);
        circleImageUserSignIn = (CircleImageView) headerLayout.findViewById(R.id.navImageViewUser);
        textEmailUserSignIn = (TextView) headerLayout.findViewById(R.id.navTextViewEmail);

        //textNameUserSignIn.setText("Thaai Văn Phước");
        //navigationView.setBackgroundColor(Color.BLACK);


        floatingMenu                        = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingButtonDiemDanhBoSung        =  (FloatingActionButton) findViewById(R.id.itemDiemDanhBoSung);
        floatingButtonDanhSachDaQuet        = (FloatingActionButton) findViewById(R.id.itemDanhSachDaQuet);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //CheckConnect.ShowToast(getApplicationContext(),id+"");

        if (id == R.id.nav_quyet_admin) {
            //CheckConnect.ShowToast(getApplication(),"1");
            // Handle the camera action
        } else if (id == R.id.nav_quyet_admin) {


        } else if (id == R.id.nav_sukien_admin) {
            Intent intent = new Intent(MainActivity.this, XemSuKienActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_nguoidk_admin) {
            Intent intentDSDK = new Intent(MainActivity.this, DanhSachDangKyActivity.class);
            startActivity(intentDSDK);
            finish();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            mAuth.signOut();
            sendtoStart();

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // khởi tạo menu search
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // khởi tạo menu
        getMenuInflater().inflate(R.menu.menu_search,menu);
        return super.onCreateOptionsMenu(menu);
    }
    // bắt sự kiện cho search
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSearch:
                Intent intentDM = new Intent(MainActivity.this, DanhSachDangKyActivity.class);
                startActivity(intentDM);
        }
        return super.onOptionsItemSelected(item);
    }

    public static JSONObject updateData(String id, String name) {
        com.squareup.okhttp.Response response;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urlUpdate+"action=update&id="+id+"&name="+name)
                    .build();
            response = client.newCall(request).execute();
            //    Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            //Log.e(TAG, "recieving null " + e.getLocalizedMessage());
        }
        return null;
    }

    class UpdateDataActivity extends AsyncTask<Void, Void, Void> {
        String result=null;

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = updateData("b1507264","Update ne");

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {

                    result=jsonObject.getString("result");

                }
            } catch (JSONException je) {
                //Log.i(Controller.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
        }

    }


    public JSONObject insertData(String id, String name) {
        com.squareup.okhttp.Response response;
        try {
            OkHttpClient client = new OkHttpClient();
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .url(urlUpdate+"action=insert&id="+id+"&name="+name)
                    .build();
            response = client.newCall(request).execute();
            //    Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());


        } catch (@NonNull IOException | JSONException e) {
            //Log.e(TAG, "recieving null " + e.getLocalizedMessage());
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    class InsertData extends AsyncTask < Void, Void, Void > {
        String result = null;


        @Nullable
        @Override
        protected Void doInBackground(Void...params) {
            JSONObject jsonObject = insertData(mssv, ghiChu);
            //Log.i(Controller.TAG, "Json obj ");

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {

                    result = jsonObject.getString("result");

                }
            } catch (JSONException je) {
                // Log.i(Controller.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // dialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }


    }

    class QuetDiemDanh extends AsyncTask < Void, Void, Void > {
        String result = null;


        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = insertData(scannedData, null);
            //Log.i(Controller.TAG, "Json obj ");

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {

                    result = jsonObject.getString("result");

                }
            } catch (JSONException je) {
                // Log.i(Controller.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // dialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public JSONObject deleteData(String id) {
        com.squareup.okhttp.Response response;
        try {
            OkHttpClient client = new OkHttpClient();

            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .url(urlUpdate+"action=delete&id="+id)
                    .build();
            response = client.newCall(request).execute();
            // Log.e(TAG,"response from gs"+response.body().string());
            return new JSONObject(response.body().string());

        } catch (@NonNull IOException | JSONException e) {

        }
        return null;
    }

    class DeleteData extends AsyncTask<Void, Void, Void> {

        String result;
        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = deleteData(mssvDelete);

            try {
                if (jsonObject != null) {
                    result=jsonObject.getString("result");
                }
            } catch (JSONException je) {

            }
            return null;

        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            scannedData = result.getContents();
            if (scannedData != null) {
                // Here we need to handle scanned data...
                //new SendRequest().execute();
                new QuetDiemDanh().execute();

            } else {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void DialogDiemDanhSinhVien(){
        final Dialog dialog = new Dialog(this);
        //Bỏ tiêu đề hộp thoại dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Gán layout custom vào màn hình chính(activity_main.xml)
        dialog.setContentView(R.layout.update_diemdanh);
        //Khóa màn hình lúc hộp thoại dialog hiện lên
        dialog.setCanceledOnTouchOutside(false);
        final EditText editTextMssv =  dialog.findViewById(R.id.editTextMSSV);
        final EditText editTextGhiChu =  dialog.findViewById(R.id.editTextGhiChu);
        Button btnDiemDanh =  dialog.findViewById(R.id.buttonAdd);
        Button btnHuy =  dialog.findViewById(R.id.buttonHuy);

        btnDiemDanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mssv = editTextMssv.getText().toString().trim();
                ghiChu = editTextGhiChu.getText().toString().trim();
                if (mssv.isEmpty() && ghiChu.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    new InsertData().execute();
                    dialog.dismiss();
                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tắt dialog
                //dialog.cancel();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public class SendRequest extends AsyncTask<String, Void, String> {


        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {
                URL url = new URL("https://script.google.com/macros/s/AKfycbzwGHxllkvkXYX6dWRKDWDZqyf5P77KSN0dHH85BM76oHNUC8c/exec");
                //Enter script URL Here
                //URL url = new URL("https://script.google.com/macros/s/AKfycbzwGHxllkvkXYX6dWRKDWDZqyf5P77KSN0dHH85BM76oHNUC8c/exec");

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("sdata", scannedData);

                //Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        //Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}


