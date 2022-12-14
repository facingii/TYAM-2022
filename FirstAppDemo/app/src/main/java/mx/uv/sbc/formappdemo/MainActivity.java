package mx.uv.sbc.formappdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import mx.uv.sbc.formappdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
//    private static String NAME_KEY = "name";
//    private static String LAST_NAME_KEY = "lastname";
//    private static String PHONE_KEY = "phone";
    private static final String TAG = "MyFirstApp";
    //private EditText edtName, edtLastname, edtPhone;

    ActivityMainBinding binding;
    FormViewModel formViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d (TAG, "OnCreate");

        binding = ActivityMainBinding.inflate (getLayoutInflater ());
        var view1  = binding.getRoot ();
        setContentView (view1);

        formViewModel = new ViewModelProvider (this).get (FormViewModel.class);

        //edtName = findViewById (R.id.edtName);
        binding.edtName.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged (Editable editable) {
                formViewModel.Name = editable.toString ();
            }
        });

        //edtLastname = findViewById (R.id.edtLastname);
        binding.edtLastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                formViewModel.LastName = editable.toString ();
            }
        });

        //edtPhone = findViewById (R.id.edtPhone);
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                formViewModel.Phone = editable.toString ();
            }
        });

        binding.btnSend.setOnClickListener (view -> {
            Intent intent = new Intent (getBaseContext (), DetailsActivity.class);
            intent.putExtra ("NAME", binding.edtName.getText ().toString ());
            intent.putExtra ("LASTNAME", binding.edtLastname.getText ().toString ());
            intent.putExtra ("PHONE", binding.edtPhone.getText ().toString ());

            startActivity (intent);
        });

        binding.btnOpenCamera.setOnClickListener (view -> {
            var permission = ContextCompat.checkSelfPermission (getBaseContext (),
                    Manifest.permission.CAMERA);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermissionLauncher.launch (Manifest.permission.CAMERA);
                return;
            }

            openCamera ();
        });

        binding.btnSearch.setOnClickListener (view -> {
            var intent = new Intent (getBaseContext (), SearchActivity.class);
            lauchSearchActivityForResult.launch (intent);
        });
    }

    private void openCamera () {
        Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        launchCameraForResult.launch (intent);
    }

    private final ActivityResultLauncher<Uri> lauchTakePictureForResult = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult (Boolean result) {
                    if (result) {
                        // TODO
                    }
                }
            }
    );

    private final ActivityResultLauncher<String> requestCameraPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission (),
            result -> {
                if (result) {
                    openCamera ();
                }
            }
    );

    private final ActivityResultLauncher<Intent> launchCameraForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult (ActivityResult result) {
                    if (result.getResultCode () == Activity.RESULT_OK) {
                        var intent = result.getData ();
                        if (intent == null) return;

                        var bundle =  intent.getExtras ();
                        if (bundle == null) return;

                        var thumbnail = (Bitmap) bundle.get ("data");
                        binding.ivPicture.setImageBitmap (thumbnail);
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> lauchSearchActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                var intent = result.getData ();
                if (intent == null) return;

                var bundle = intent.getExtras ();
                if (bundle == null) return;

                var name = bundle.getString ("NAME");
                var message = String.format ("Found: %s", name);
                Toast.makeText (getBaseContext (), message, Toast.LENGTH_LONG).show ();
            }
    );

    @Override
    protected void onStart () {
        super.onStart ();
        Log.d (TAG, "OnStart");
    }

    @Override
    protected void onResume () {
        super.onResume ();
        Log.d (TAG, "OnResume");

        binding.edtName.setText (formViewModel.Name);
        binding.edtLastname.setText (formViewModel.LastName);
        binding.edtPhone.setText (formViewModel.Phone);
    }

    @Override
    protected void onPause () {
        super.onPause ();
        Log.d (TAG, "OnPause");
    }

    @Override
    protected void onStop () {
        super.onStop ();
        Log.d (TAG, "OnStop");
    }

    @Override
    protected void onRestart () {
        super.onRestart ();
        Log.d (TAG, "OnRestart");
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        Log.d (TAG, "OnDestroy");
    }

//    @Override
//    protected void onSaveInstanceState (@NonNull Bundle outState) {
//        super.onSaveInstanceState (outState);
//        Log.d (TAG, "OnSaveInstanceState");
//
////        outState.putString (NAME_KEY, edtName.getText ().toString ());
////        outState.putString (LAST_NAME_KEY, edtLastname.getText ().toString ());
////        outState.putString (PHONE_KEY, edtPhone.getText ().toString ());
//    }

//    @Override
//    protected void onRestoreInstanceState (@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState (savedInstanceState);
//        Log.d (TAG, "OnRestoreInstanceState");
//
////        String name = savedInstanceState.getString (NAME_KEY);
////        edtName.setText (name);
////
////        String lastname = savedInstanceState.getString (LAST_NAME_KEY);
////        edtLastname.setText (lastname);
////
////        String phone = savedInstanceState.getString (PHONE_KEY);
////        edtPhone.setText (phone);
//    }
}
