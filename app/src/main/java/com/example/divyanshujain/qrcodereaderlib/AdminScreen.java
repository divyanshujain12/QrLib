package com.example.divyanshujain.qrcodereaderlib;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.divyanshujain.qrcodereaderlib.Constants.Constants;
import com.example.divyanshujain.qrcodereaderlib.FirebaseDatabase.FirebaseDatabaseConnections;
import com.example.divyanshujain.qrcodereaderlib.Models.OtherUser;
import com.example.divyanshujain.qrcodereaderlib.Utilities.CommonFunctions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;
import com.neopixl.pixlui.components.textview.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AdminScreen extends AppCompatActivity {

    @InjectView(R.id.toolbarView)
    Toolbar toolbarView;
    @InjectView(R.id.clickToScanTV)
    TextView clickToScanTV;
    @InjectView(R.id.nameET)
    EditText nameET;
    @InjectView(R.id.emailET)
    EditText emailET;
    @InjectView(R.id.phoneET)
    EditText phoneET;
    @InjectView(R.id.dobTV)
    TextView dobTV;
    @InjectView(R.id.submitBT)
    Button submitBT;
    @InjectView(R.id.activity_admin_screen)
    RelativeLayout activityAdminScreen;
    private IntentIntegrator qrScan;
    Calendar myCalendar = Calendar.getInstance();
    private String username;
    private String name, email, phone, dob, qrCodeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);
        ButterKnife.inject(this);

        InitViews();
    }

    private void InitViews() {
        username = getIntent().getStringExtra(Constants.USERNAME);
        CommonFunctions.getInstance().configureToolbarWithOutBackButton(this, toolbarView, username);
        qrScan = new IntentIntegrator(this);
    }

    @OnClick({R.id.clickToScanTV, R.id.submitBT, R.id.dobTV})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dobTV:
                new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.clickToScanTV:
                qrScan.initiateScan();
                break;
            case R.id.submitBT:
                name = nameET.getText().toString();
                email = emailET.getText().toString();
                phone = phoneET.getText().toString();
                dob = dobTV.getText().toString();
                OtherUser otherUser = createJsonForSubmitUserData();
                FirebaseDatabaseConnections.getInstance().addOtherUser(otherUser);
                break;
        }
    }

    private OtherUser createJsonForSubmitUserData() {
        OtherUser otherUser = new OtherUser();
        try {
            otherUser.setName(name);
            otherUser.setEmail(email);
            otherUser.setPhone(phone);
            otherUser.setDob(dob);
            otherUser.setQrCode(qrCodeData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return otherUser;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                qrCodeData = result.getContents();
                clickToScanTV.setText(qrCodeData);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dobTV.setText(sdf.format(myCalendar.getTime()));
    }
}
