package com.example.divyanshujain.qrcodereaderlib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.divyanshujain.qrcodereaderlib.Callbacks.GetUserCallback;
import com.example.divyanshujain.qrcodereaderlib.Constants.Constants;
import com.example.divyanshujain.qrcodereaderlib.FirebaseDatabase.FirebaseDatabaseConnections;
import com.example.divyanshujain.qrcodereaderlib.Models.User;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements GetUserCallback {

    @InjectView(R.id.nameET)
    EditText nameET;
    @InjectView(R.id.passwordET)
    EditText passwordET;

    @InjectView(R.id.activity_login)
    FrameLayout activityLogin;
    @InjectView(R.id.loginAsAdminBT)
    Button loginAsAdminBT;
    @InjectView(R.id.loginAsUserBT)
    Button loginAsUserBT;

    private String username, password;
    private static String ADMIN = "admin";
    private static String USER = "user";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        InitViews();
    }

    private void InitViews() {

    }

    @OnClick({R.id.loginAsAdminBT, R.id.loginAsUserBT})
    public void onClick(View view) {
        username = nameET.getText().toString();
        password = passwordET.getText().toString();

        switch (view.getId()) {
            case R.id.loginAsAdminBT:
                user = new User(ADMIN, username, password);
                getDataFromFCM(ADMIN);
                break;
            case R.id.loginAsUserBT:
                user = new User(USER, username, password);
                getDataFromFCM(USER);
                break;
        }
    }

    private void getDataFromFCM(String userType) {
        FirebaseDatabaseConnections.getInstance().getUser(userType, this);
    }

    @Override
    public void getUserOnSuccess(User user) {
        if (user != null) {
            if (this.user.equals(user)) {
                if (this.user.getPassword().equals(user.getPassword())) {
                    Intent intent = new Intent(this, UserScreen.class);
                    intent.putExtra(Constants.USERNAME, user.getEmail());
                    startActivity(intent);
                    Toast.makeText(this, "Successfully Login", Toast.LENGTH_SHORT).show();
                }

            } else
                Toast.makeText(this, "Invalid User!", Toast.LENGTH_SHORT).show();

        }
    }
}
