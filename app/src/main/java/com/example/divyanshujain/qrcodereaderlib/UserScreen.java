package com.example.divyanshujain.qrcodereaderlib;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.divyanshujain.qrcodereaderlib.Callbacks.GetOtherUserCallback;
import com.example.divyanshujain.qrcodereaderlib.Constants.Constants;
import com.example.divyanshujain.qrcodereaderlib.FirebaseDatabase.FirebaseDatabaseConnections;
import com.example.divyanshujain.qrcodereaderlib.Models.OtherUser;
import com.example.divyanshujain.qrcodereaderlib.Utilities.CommonFunctions;
import com.example.divyanshujain.qrcodereaderlib.Utilities.MySharedPereference;
import com.example.divyanshujain.qrcodereaderlib.databinding.ActivityUserScreenBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.textview.TextView;

public class UserScreen extends AppCompatActivity implements GetOtherUserCallback {

    OtherUser otherUser = new OtherUser();
    ActivityUserScreenBinding activityUserScreenBinding;
    String qrCodeData;
    RelativeLayout otherUserDataRL;
    Button scanBT;
    private IntentIntegrator qrScan;
    private TextView qrCode;
    private Toolbar toolbarView;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qrScan = new IntentIntegrator(this);


        activityUserScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_screen);
        activityUserScreenBinding.setPresenter(this);
        activityUserScreenBinding.setOtherUser(otherUser);

        otherUserDataRL = activityUserScreenBinding.otherUserDataRL;
        scanBT = activityUserScreenBinding.scanBT;
        qrCode = activityUserScreenBinding.clickToScanTV;
        toolbarView = (Toolbar) findViewById(R.id.toolbarView);
        username = MySharedPereference.getInstance().getString(this, Constants.EMAIL);
        CommonFunctions.getInstance().configureToolbarWithOutBackButton(this, toolbarView, username);

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
        scanBT.setVisibility(View.GONE);

        this.otherUser.setQrCode(otherUser.getQrCode());
        this.otherUser.setDob(otherUser.getDob());
        this.otherUser.setName(otherUser.getName());
        this.otherUser.setEmail(otherUser.getEmail());
        this.otherUser.setPhone(otherUser.getPhone());
        qrCode.setText(otherUser.getQrCode());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logount_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_logout:
                MySharedPereference.getInstance().clearSharedPreference(this);
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

}
