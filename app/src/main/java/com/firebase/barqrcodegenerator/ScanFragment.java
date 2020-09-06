package com.firebase.barqrcodegenerator;

import android.Manifest;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.security.Policy;
import java.util.Objects;

import static com.firebase.barqrcodegenerator.R.drawable.ic_flash_on;


public class ScanFragment extends Fragment {

    String result="";
    SurfaceView surface_view;
    CameraSource cameraSource;
    TextView text_result,txt_copy,txt_goto;
    BarcodeDetector barcodeDetector;
    ClipboardManager clipboardManager;


    public ScanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);



        surface_view = view.findViewById(R.id.surface_view);
        text_result = view.findViewById(R.id.text_result);
        txt_copy = view.findViewById(R.id.txt_copy);
        txt_goto = view.findViewById(R.id.txt_goto);

        clipboardManager=(ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        BarcodeDetector.Builder builder = new BarcodeDetector.Builder(getContext());
        builder.setBarcodeFormats(Barcode.QR_CODE|Barcode.DATA_MATRIX|Barcode.EAN_13|Barcode.EAN_8|Barcode.UPC_A|Barcode.UPC_E|Barcode.CODE_39|Barcode.CODE_93|Barcode.CODE_128|Barcode.ITF|Barcode.CODABAR|Barcode.PDF417|Barcode.AZTEC);
        barcodeDetector = builder.build();

        cameraSource = new CameraSource.Builder(Objects.requireNonNull(getContext()), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();

        surface_view.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                try {
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrcode=detections.getDetectedItems();
                if (qrcode.size()!=0){
                    text_result.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator=(Vibrator) Objects.requireNonNull(getActivity()).getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(500);
                            text_result.setText(qrcode.valueAt(0).displayValue);

                        }
                    });
                }

            }
        });

        txt_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                result=text_result.getText().toString();
                if (!result.equals("")) {
                    ClipData clipData = ClipData.newPlainText("text", result);
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(getContext(), "Text Copied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result=text_result.getText().toString();
                boolean a=URLUtil.isValidUrl(result);
                if (!a){
                    Toast.makeText(getContext(), "It is not a Valid Url....", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!result.equals("") ) {

                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(result));
                    Objects.requireNonNull(getActivity()).startActivity(intent);
                }

            }
        });

        return view;
    }
}