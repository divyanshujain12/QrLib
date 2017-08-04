package com.example.divyanshujain.qrcodereaderlib;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.divyanshujain.qrcodereaderlib.Callbacks.GetOtherUserCallback;
import com.example.divyanshujain.qrcodereaderlib.FirebaseDatabase.FirebaseDatabaseConnections;
import com.example.divyanshujain.qrcodereaderlib.Models.OtherUser;
import com.example.divyanshujain.qrcodereaderlib.databinding.ActivityUserScreenBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.neopixl.pixlui.components.button.Button;

public class UserScreen extends AppCompatActivity implements GetOtherUserCallback {

    OtherUser otherUser = new OtherUser();
    ActivityUserScreenBinding activityUserScreenBinding;
    String qrCodeData;
    RelativeLayout otherUserDataRL;
    Button scanBT;
    private IntentIntegrator qrScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qrScan = new IntentIntegrator(this);
        activityUserScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_screen);
        otherUserDataRL = activityUserScreenBinding.otherUserDataRL;
        scanBT = activityUserScreenBinding.scanBT;

    }

    public void openQrScanner(View v) {
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                qrCodeData = result.getContents();
                FirebaseDatabaseConnections.getInstance().getOtherUser(qrCodeData, this);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onGetOtherUser(OtherUser otherUser) {
        otherUserDataRL.setVisibility(View.VISIBLE);
        scanBT.setVisibility(View.VISIBLE);

        this.otherUser.setQrCode(otherUser.getQrCode());
        this.otherUser.setDob(otherUser.getDob());
        this.otherUser.setName(otherUser.getName());
        this.otherUser.setEmail(otherUser.getEmail());
        this.otherUser.setPhone(otherUser.getPhone());

    }
}
