package com.example.digpassmaster;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        qrCodeImageView = findViewById(R.id.qrCodeImageView);

        // Retrieve user details from Intent extras
        String passNumber = getIntent().getStringExtra("pass number");
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String mobile = getIntent().getStringExtra("mobile");
        String reason = getIntent().getStringExtra("reason");
        String leavingTime = getIntent().getStringExtra("leavingTime");
        String date=getIntent().getStringExtra("date");
        String gateid=getIntent().getStringExtra("gateid");

        // Generate and display the QR code
        generateAndDisplayQRCode(gateid);
    }

    private void generateAndDisplayQRCode(String gateid) {
        try {
            // Generate data string containing user details
            String userData = gateid;
            // Generate QR code bitmap
            Bitmap qrCodeBitmap = generateQRCode(userData, 500, 500);

            // Display QR code bitmap
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to generate a QR code using ZXing library
    private Bitmap generateQRCode(String data, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        return bitmap;
    }
}