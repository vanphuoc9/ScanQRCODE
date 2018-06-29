package com.example.holoc.scanqrcode.activity.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.holoc.scanqrcode.R;
import com.example.holoc.scanqrcode.adapter.UsersAdapter;
import com.example.holoc.scanqrcode.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DanhSachDangKyActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference mUsersDatabase;
    private ProgressDialog progressDialog;
    private ImageView imageUser;
    private FloatingActionButton btnAddUser;
   // private EditText editSearch;


    private ListView listViewUser;
    private ArrayList<Users> arrayUser;
    private UsersAdapter adapterUser;

    // storage


    private static final int GALLERY_PICK = 1;

    private FirebaseStorage imageStorage = FirebaseStorage.getInstance();
    private StorageReference imageStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_dang_ky);
          Anhxa();
         ActionBar();
//        ActionAddUser();
        //ActionSearch();



    }

//    private void ActionSearch() {
//        editSearch.setHintTextColor(Color.WHITE);
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//        editSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(!s.toString().isEmpty()){
//                    String search = s.toString().trim();
//                    firebaseSearch(search);
//
//
//                }else{
//                    GetData();
//
//                }
//
//            }
//        });
//    }

//    private void firebaseSearch(String search) {
//        Query firebaseSearch = mUsersDatabase.orderByChild("name").startAt(search).endAt(search + "\uf8ff");
//        // lấy dữ liệu từ firebase
//        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyler = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
//                Users.class,
//                R.layout.dong_recycler_users,
//                UsersViewHolder.class,
//                firebaseSearch
//        ) {
//            @Override
//            protected void populateViewHolder(final UsersViewHolder viewHolder, final Users model, int position) {
//                // đổ  dữ liệu từ firebase về app
//                viewHolder.setName(model.getName());
//                viewHolder.setUserMSSV(model.getMssv());
//                viewHolder.setTrangThai(doiQuyen(model.getQuyen()));
////                Log.e("AAAA",model.getXacnhan());
//                viewHolder.setUserImage(model.getImage(), getApplicationContext());
//                // lấy id của user
//                final String user_id = getRef(position).getKey();
//                // xét sự kiện click vào từng người màn hình chi tiết
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intentDetailUser = new Intent(DanhSachDangKyActivity.this, ChiTietUserActivity.class);
//
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("model",model);
//                        bundle.putString("user_id",user_id);
//                        intentDetailUser.putExtra("dulieu",bundle);
//                        startActivity(intentDetailUser);
//                    }
//                });
//
//                // button Xóa
//                Button btnXoa = viewHolder.mView.findViewById(R.id.dong_user_btnxoa);
//                btnXoa.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        mUsersDatabase.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                mUsersDatabase.child(user_id).removeValue();
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//
//                });
//                // button sửa
//                final String name= model.getName();
//                Button btnSua = viewHolder.mView.findViewById(R.id.dong_user_btnsua);
//                btnSua.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DialogUpdate(user_id, model);
//                        //Toast.makeText(getApplicationContext(),model.getName(),Toast.LENGTH_LONG).show();
//                    }
//                });
//
//
//            }
//        };
//
//
//        recyclerView.setAdapter(firebaseRecyler);
//
//    }


//    // Thêm người dùng
//    private void ActionAddUser() {
//        btnAddUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogAddUser();
//            }
//        });
//
//    }

    // DialogAddUser
//    private void DialogAddUser() {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // dùng để goi layout dialog
//        dialog.setContentView(R.layout.dialog_user_sua);
//        dialog.setCanceledOnTouchOutside(false);
//
//        imageUser = dialog.findViewById(R.id.imge_user_imge_sua);
//
//        final EditText editTen = dialog.findViewById(R.id.edit_user_ten_sua);
//        final EditText editMSSV = dialog.findViewById(R.id.edit_user_mssv_sua);
//        final EditText editDonVi = dialog.findViewById(R.id.edit_user_donvi_sua);
//        final EditText editSDT = dialog.findViewById(R.id.edit_user_sdt_sua);
//        Button btnThem = dialog.findViewById(R.id.btn_user_capnhat_sua);
//        Button btnHuy = dialog.findViewById(R.id.btn_user_huy_sua);
//
//        btnThem.setText("Thêm");
//        btnHuy.setText("Trở về");
//
//        // event upload image
//        imageUser.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                //  Toast.makeText(getApplicationContext(), "Long Clicked " , Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "SECLECT IMAGE"), GALLERY_PICK);
//                return false;
//            }
//        });
//
//        btnThem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActionProgressbar("Thêm", "Đang thêm người dùng...");
//
//                final String ten = editTen.getText().toString();
//                final String mssv = editMSSV.getText().toString();
//                final String donvi = editDonVi.getText().toString();
//                final String sdt = editSDT.getText().toString();
//                final String deviceToken = FirebaseInstanceId.getInstance().getToken();
//
//                // Biểu thức chính qui
//                String pattern ="0[0-9]{9,10}";
//
//
//                if(ten.equals("") | mssv.equals("") | donvi.equals("") | sdt.equals("")){
//                    progressDialog.hide();
//                    CheckConnect.ShowToast(DanhSachDangKyActivity.this, "Vui lòng điển đầy đủ thông tin");
//                }else {
//                    if(sdt.matches(pattern)){
////                        CheckConnect.ShowToast(getApplicationContext(),"Bạn đã nhập đúng");
//                        // không trùng giữa các hình upload
//                        Calendar calendar = Calendar.getInstance();
//                        final String KeyImageUser = String.valueOf(calendar.getTimeInMillis());
//                        imageStorageRef = imageStorage.getReferenceFromUrl("gs://nckh-1cbe0.appspot.com/Users");
//                        StorageReference userImageRef = imageStorageRef.child("images").child(calendar.getTimeInMillis()+".jpg");
//                        // final String KeyHinh = String .valueOf(calendar.getTimeInMillis());
////                    imageUser.setScaleType(ImageView.ScaleType.FIT_XY);
//                        // upload image
//                        // Get the data from an ImageView as bytes
//                        imageUser.setDrawingCacheEnabled(true);
//                        imageUser.buildDrawingCache();
//                        Bitmap bitmap = ((BitmapDrawable) imageUser.getDrawable()).getBitmap();
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                        byte[] data = baos.toByteArray();
//                        Toast.makeText(DanhSachDangKyActivity.this, data.toString(), Toast.LENGTH_SHORT).show();
//                        UploadTask uploadTask = userImageRef.putBytes(data);
//                        uploadTask.addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                // Handle unsuccessful uploads
//                                CheckConnect.ShowToast(getApplicationContext(),"Thêm hình không thành công");
//                                progressDialog.dismiss();
//                            }
//                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                                @SuppressWarnings("VisibleForTests")
//                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                                // upload database on database if image upload success
//                                // tạo new key
//                                String key = mUsersDatabase.push().getKey();
//
//                                Users user = new Users();
//                                user.setName(ten);
//                                user.setMssv(mssv);
//                                user.setImage(downloadUrl.toString());
//                                user.setDonvi(donvi);
//                                user.setSdt(sdt);
//                                user.setQuyen("0");
//                                user.setXacnhan("0");
//                                user.setDevice_token(deviceToken);
//
//                                mUsersDatabase.child(key).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            progressDialog.dismiss();
//                                            CheckConnect.ShowToast(DanhSachDangKyActivity.this,"Thêm thành công");
//                                            dialog.dismiss();
//                                        } else {
//                                            progressDialog.hide();
//                                            CheckConnect.ShowToast(DanhSachDangKyActivity.this, "Thêm không thành công");
//                                        }
//                                    }
//                                });
//
//                            }
//                        });
//
//
//                    }else{
//                        progressDialog.hide();
//                        CheckConnect.ShowToast(getApplicationContext(),"Vui lòng nhập đúng định dạng số điện thoại");
//                    }
//
//
//
//
//
//
//                }
//            }
//        });
//
//
//
//
//        btnHuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//
//    }


    // start
    @Override
    protected void onStart() {
        super.onStart();

        //GetData();
        GetData();




    }

    private void GetData() {

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item: dataSnapshot.getChildren()){
//
                    String name = item.child("name").getValue().toString().trim();
                    String mssv = item.child("mssv").getValue().toString().trim();
                    String image = item.child("image").getValue().toString().trim();
                    String donvi = item.child("donvi").getValue().toString().trim();
                    String nganh = item.child("nganh").getValue().toString().trim();
                    String sdt = item.child("sdt").getValue().toString().trim();
                    String quyen = item.child("quyen").getValue().toString().trim();
                    String devicetoken = item.child("device_token").getValue().toString().trim();

                    arrayUser.add(new Users(name,mssv,image,donvi,nganh,sdt,quyen,devicetoken));
                    adapterUser.notifyDataSetChanged();
                    //CheckConnect.ShowToast(getApplicationContext(),name+" "+mssv+" "+image+" "+donvi+" "+nganh+" "+quyen+" "+sdt+" "+devicetoken);


                    listViewUser.smoothScrollToPosition(arrayUser.size() - 1);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void GetData() {
//
//        // lấy dữ liệu từ firebase
//        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyler = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
//                Users.class,
//                R.layout.dong_recycler_users,
//                UsersViewHolder.class,
//                mUsersDatabase
//        ) {
//            @Override
//            protected void populateViewHolder(final UsersViewHolder viewHolder, final Users model, int position) {
//                // đổ  dữ liệu từ firebase về app
//                viewHolder.setName(model.getName());
//                viewHolder.setUserMSSV(model.getMssv());
//                viewHolder.setTrangThai(doiQuyen(model.getQuyen()));
////                Log.e("AAAA",model.getXacnhan());
//                viewHolder.setUserImage(model.getImage(), getApplicationContext());
//                // lấy id của user
//                final String user_id = getRef(position).getKey();
//                // xét sự kiện click vào từng người màn hình chi tiết
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intentDetailUser = new Intent(DanhSachDangKyActivity.this, ChiTietUserActivity.class);
//
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("model",model);
//                        bundle.putString("user_id",user_id);
//                        intentDetailUser.putExtra("dulieu",bundle);
//                        startActivity(intentDetailUser);
//                    }
//                });
//
//                // button Xóa
//                Button btnXoa = viewHolder.mView.findViewById(R.id.dong_user_btnxoa);
//                btnXoa.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        mUsersDatabase.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                mUsersDatabase.child(user_id).removeValue();
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//
//                });
//                // button sửa
//                final String name= model.getName();
//                Button btnSua = viewHolder.mView.findViewById(R.id.dong_user_btnsua);
//                btnSua.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DialogUpdate(user_id, model);
//                        //Toast.makeText(getApplicationContext(),model.getName(),Toast.LENGTH_LONG).show();
//                    }
//                });
//
//
//            }
//        };
//
//
//        recyclerView.setAdapter(firebaseRecyler);
//
//    }

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


    // DialogUpdateUser theo idUser và được truyền dữ liệu từ model Users

//    private void DialogUpdate(final String idUser, final Users model) {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // dùng để goi layout dialog
//        dialog.setContentView(R.layout.dialog_user_sua);
//        dialog.setCanceledOnTouchOutside(false);
//
//        imageUser = dialog.findViewById(R.id.imge_user_imge_sua);
//
//        final EditText editTen = dialog.findViewById(R.id.edit_user_ten_sua);
//        final EditText editMSSV = dialog.findViewById(R.id.edit_user_mssv_sua);
//        final EditText editDonVi = dialog.findViewById(R.id.edit_user_donvi_sua);
//        final EditText editSDT = dialog.findViewById(R.id.edit_user_sdt_sua);
//        Button btnCapNhap = dialog.findViewById(R.id.btn_user_capnhat_sua);
//        Button btnHuy = dialog.findViewById(R.id.btn_user_huy_sua);
//
//        ///////GET DATA
//
//
//        //Toast.makeText(getApplicationContext(),model.getName(),Toast.LENGTH_LONG).show();
//        editTen.setText(model.getName());
//        editMSSV.setText(model.getMssv());
//        editDonVi.setText(model.getDonvi());
//        editSDT.setText(model.getSdt());
//        Picasso.with(getApplicationContext()).load(model.getImage())
//                .placeholder(R.drawable.no_image_available)
//                .error(R.drawable.err)
//                .into(imageUser);
//
//
//        /////// UPLOAD DATA
////
//        imageUser.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                //  Toast.makeText(getApplicationContext(), "Long Clicked " , Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "SECLECT IMAGE"), GALLERY_PICK);
//                return false;
//            }
//        });
//        btnCapNhap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActionProgressbar("Cập nhât","Đang cập nhật dữ liệu...");
//
//                final String ten = editTen.getText().toString();
//                final String mssv = editMSSV.getText().toString();
//                final String donvi = editDonVi.getText().toString();
//                final String sdt = editSDT.getText().toString();
//                final String deviceToken = FirebaseInstanceId.getInstance().getToken();
//
//                // Biểu thức chính qui kiểm tra số điện thoại
//                String pattern ="0[0-9]{9,10}";
//
//                if(ten.equals("") | mssv.equals("") | donvi.equals("") | sdt.equals("")){
//                    progressDialog.hide();
//                    CheckConnect.ShowToast(DanhSachDangKyActivity.this, "Vui lòng điển đầy đủ thông tin cập nhật");
//                }else {
//
//                    if(sdt.matches(pattern)){
//
//
//                        // không trùng giữa các hình upload
//                        Calendar calendar = Calendar.getInstance();
//                        final String KeyImageUser = String.valueOf(calendar.getTimeInMillis());
//                        imageStorageRef = imageStorage.getReferenceFromUrl("gs://nckh-1cbe0.appspot.com/Users");
//                        StorageReference userImageRef = imageStorageRef.child("images").child(calendar.getTimeInMillis()+".jpg");
//                        // final String KeyHinh = String .valueOf(calendar.getTimeInMillis());
////                    imageUser.setScaleType(ImageView.ScaleType.FIT_XY);
//                        // upload image
//                        // Get the data from an ImageView as bytes
//                        imageUser.setDrawingCacheEnabled(true);
//                        imageUser.buildDrawingCache();
//                        Bitmap bitmap = ((BitmapDrawable) imageUser.getDrawable()).getBitmap();
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                        byte[] data = baos.toByteArray();
//
//                        UploadTask uploadTask = userImageRef.putBytes(data);
//                        uploadTask.addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                // Handle unsuccessful uploads
//                                CheckConnect.ShowToast(getApplicationContext(),"Cập nhật hình không thành công");
//                                progressDialog.dismiss();
//                            }
//                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                                @SuppressWarnings("VisibleForTests")
//                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                                // upload database on database if image upload success
//                                Users user = new Users();
//                                user.setName(ten);
//                                user.setMssv(mssv);
//                                user.setImage(downloadUrl.toString());
//                                user.setDonvi(donvi);
//                                user.setSdt(sdt);
//                                user.setQuyen(model.getQuyen());
//                                user.setXacnhan(model.getXacnhan());
//                                user.setDevice_token(deviceToken);
//
//                                mUsersDatabase.child(idUser).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            progressDialog.dismiss();
//                                            CheckConnect.ShowToast(DanhSachDangKyActivity.this, "Cập nhật thành công");
//                                            dialog.dismiss();
//                                        } else {
//                                            progressDialog.hide();
//                                            CheckConnect.ShowToast(DanhSachDangKyActivity.this, "Cập nhật không thành công");
//                                        }
//                                    }
//                                });
//
//                            }
//                        });
//
//
//
//
//
//                    }else{
//                        progressDialog.hide();
//                        CheckConnect.ShowToast(getApplicationContext(),"Vui lòng nhập đúng định dạng số điện thoại");
//                    }
//
//
//
//                }
//
//
//
//
//
//
//            }
//        });
////
//        btnHuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }

    // Nhận hình
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            Uri uriImage = data.getData();
            imageUser.setImageURI(uriImage);
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            imageUser.setImageBitmap(bitmap);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // ProgressBar
    private void ActionProgressbar(String title, String message) {
        progressDialog = new ProgressDialog(DanhSachDangKyActivity.this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }


    // Đổi số ra trạng thái
    private String doiTrangThai(String xacnhan) {
        if (xacnhan.equals("1")) return "Đã xác nhận";
        else return "Chưa xác nhận";
    }

    // ActionBar
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachDangKyActivity.this, MainActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    // Ánh xạ
    private void Anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_dsdk);
        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView_dsdk);
        btnAddUser = (FloatingActionButton) findViewById(R.id.btn_dangky_add_user);
       // editSearch = findViewById(R.id.edit_ds_dangky_search);

        listViewUser = (ListView) findViewById(R.id.listview_dsdk);
        arrayUser = new ArrayList<>();
        adapterUser = new UsersAdapter(getApplicationContext(),arrayUser);
        listViewUser.setAdapter(adapterUser);

        // lấy dữ liệu người dùng
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        // đúng kích thước
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    // khởi tạo menu search
//    @Override
//    public boolean onCreateOptionsMenu(android.view.Menu menu) {
//        // khởi tạo menu
//        getMenuInflater().inflate(R.menu.menu_search,menu);
//
//        MenuItem searchItem = menu.findItem(R.id.menuSearch);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setQueryHint("Tìm kiếm...");
//        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate",null,null);
//        View searchPlate = searchView.findViewById(searchPlateId);
//        if(searchPlate != null){
//            searchPlate.setBackgroundColor(Color.DKGRAY);
//            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
//            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
//            if (searchText!=null) {
//                searchText.setTextColor(Color.WHITE);
//                searchText.setHintTextColor(Color.WHITE);
//            }
//        }
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                CheckConnect.ShowToast(getApplicationContext(),newText);
//                if(!newText.isEmpty()){
//
//                    firebaseSearch(newText);
//
//                }else{
//                    GetData();
//                }
//                return true;
//            }
//        });
//
//        //SearchManager searchManager = (SearchManager) DanhSachDangKyActivity.this.getSystemService(Context.)
//
//        return true;
//    }

}
