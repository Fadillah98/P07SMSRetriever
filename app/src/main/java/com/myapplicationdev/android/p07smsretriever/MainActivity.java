package com.myapplicationdev.android.p07smsretriever;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;
    FragmentTransaction ft, ft2;
    Fragment f1, f2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft2 = fm.beginTransaction();

        if(savedInstanceState == null){
            f1 = new FragmentNumber();
            ft.add(R.id.frame1, f1, "TAG").commit();
            f2 = new FragmentWord();
            ft2.add(R.id.frame2, f2, "TAG").commit();
        }else{
            f1 = fm.findFragmentByTag("TAG");
            f2 = fm.findFragmentByTag("TAG");

        }

        int permissionCheck = PermissionChecker.checkSelfPermission
                (MainActivity.this, Manifest.permission.READ_SMS);

        if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_SMS}, 0);
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
