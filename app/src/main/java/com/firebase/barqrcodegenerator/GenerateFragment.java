package com.firebase.barqrcodegenerator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.firebase.barqrcodegenerator.R.*;


public class GenerateFragment extends Fragment {

    EditText edit_text_content;
    Spinner spinner_types;
    ImageView img_code;
    TextView text_view_generate,txt_save;
    String code="";
    OutputStream outputStream;


    public GenerateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(layout.fragment_generate, container, false);

        edit_text_content=view.findViewById(id.edit_text_content);
        spinner_types=view.findViewById(id.spinner_types);
        img_code=view.findViewById(id.img_code);
        text_view_generate=view.findViewById(id.text_view_generate);
        txt_save=view.findViewById(id.txt_save);



        Context context=getContext();

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(context, array.code_types,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_types.setAdapter(adapter);



        text_view_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a=spinner_types.getSelectedItem().toString().trim();
                if (a.equals("QR Code"))
                    generateQrCode();
                else if (a.equals("Bar Code")) {
                    generateBarCode();
                }
                else
                    Toast.makeText(getContext(), "Select Code Type", Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

    private void generateBarCode() {

        code=edit_text_content.getText().toString().trim();

        if (!code.equals("")) {

            MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.CODE_128, 250, 150);
                BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                img_code.setImageBitmap(bitmap);

//                txt_save.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        File filePath= Environment.getExternalStorageDirectory();
//                        File dir=new File(filePath.getAbsolutePath()+"/Qr Scanner/");
//                        dir.mkdirs();
//                        File file=new File(dir,System.currentTimeMillis()+".png");
//                        try {
//                            outputStream=new FileOutputStream(file);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
//                        Toast.makeText(getContext(), "Image Saved......", Toast.LENGTH_SHORT).show();
//                        try {
//                            outputStream.flush();
//                            outputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });

            } catch (WriterException e) {
                e.printStackTrace();
            }
            Toast.makeText(getContext(), "Bar Code Generated", Toast.LENGTH_SHORT).show();
        }
        else
        Toast.makeText(getContext(), "Enter Text", Toast.LENGTH_SHORT).show();
    }

    private void generateQrCode() {
        code=edit_text_content.getText().toString().trim();

        if (!code.equals("")) {

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE, 250, 250);
                Bitmap bitmap = Bitmap.createBitmap(250, 250, Bitmap.Config.RGB_565);

                for (int x = 0; x < 250; x++) {
                    for (int y = 0; y < 250; y++) {
                        bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }

                img_code.setImageBitmap(bitmap);


//                txt_save.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                        File filePath= Environment.getExternalStorageDirectory();
//                        File dir=new File(filePath.getAbsolutePath()+"/Qr & Bar Scanner/");
//                        dir.mkdir();
//                        File file=new File(dir,System.currentTimeMillis()+".png");
//                        try {
//                           outputStream=new FileOutputStream(file);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
//                        Toast.makeText(getContext(), "Image Saved......", Toast.LENGTH_SHORT).show();
//                        try {
//                            outputStream.flush();
//                            outputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });

            } catch (WriterException e) {
                e.printStackTrace();
            }
            Toast.makeText(getContext(), "QR Code Generated", Toast.LENGTH_SHORT).show();
        }
        else
        Toast.makeText(getContext(), "Enter Text", Toast.LENGTH_SHORT).show();
    }

}