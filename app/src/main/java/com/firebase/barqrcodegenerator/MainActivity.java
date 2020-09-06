package com.firebase.barqrcodegenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RelativeLayout layout_scan,layout_generate;
    ImageView image_generate,generate_active,scan_active,image_scan;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        init();

        toolbar=findViewById(R.id.toolbar);

        generateFragment();

        layout_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanFragment();
            }
        });

        layout_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                generateFragment();
            }
        });
    }


    private void generateFragment() {

        image_generate.setVisibility(View.GONE);
        scan_active.setVisibility(View.GONE);
        generate_active.setVisibility(View.VISIBLE);
        image_scan.setVisibility(View.VISIBLE);

        toolbar.setTitle("Generate Code");


        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framlayout,new GenerateFragment());
        fragmentTransaction.commit();
    }

    private void scanFragment() {
        image_scan.setVisibility(View.GONE);
        scan_active.setVisibility(View.VISIBLE);
        image_generate.setVisibility(View.VISIBLE);
        generate_active.setVisibility(View.GONE);

        toolbar.setTitle("Scan Code");

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framlayout,new ScanFragment());
        fragmentTransaction.commit();

    }

    private void init() {

        layout_generate=findViewById(R.id.layout_generate);
        layout_scan=findViewById(R.id.layout_scan);


        image_generate=findViewById(R.id.image_generate);
        image_scan=findViewById(R.id.image_scan);
        image_scan=findViewById(R.id.image_scan);
        scan_active=findViewById(R.id.scan_active);
        generate_active=findViewById(R.id.generate_active);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

}