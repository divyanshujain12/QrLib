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
import com.example.divyanshujain.qrcodereaderlib.Models.ValidationModel;
import com.example.divyanshujain.qrcodereaderlib.Utilities.MySharedPereference;
import com.example.divyanshujain.qrcodereaderlib.Utilities.Validation;
import com.neopixl.pixlui.components.button.Button;
import com.neopixl.pixlui.components.edittext.EditText;

import java.util.HashMap;

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
    private static String ADMIN = "admin";
    private static String USER = "user";
    User user;
    private String USER_TYPE = "";

    Validation validation;
    HashMap<View, String> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MySharedPereference.getInstance().getBoolean(this, Constants.LOGGED_IN)) {
            USER_TYPE = MySharedPereference.getInstance().getString(this, Constants.USER_TYPE);
            goToNextActivity();
            return;
        }
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        InitViews();
    }

    private void InitViews() {
        validation = new Validation();
        validation.addValidationField(new ValidationModel(nameET, Validation.TYPE_EMAIL_VALIDATION, "Invalid Email"));
        validation.addValidationField(new ValidationModel(passwordET, Validation.TYPE_PASSWORD_VALIDATION, "Invalid Password"));
    }

    @OnClick({R.id.loginAsAdminBT, R.id.loginAsUserBT})
    public void onClick(View view) {
        hashMap = validation.validate(this);
        if (hashMap != null) {

            switch (view.getId()) {
                case R.id.loginAsAdminBT:
                    USER_TYPE = ADMIN;
                    user = new User(ADMIN, hashMap.get(passwordET), hashMap.get(nameET));
                    //FirebaseDatabaseConnections.getInstance().addUser(user);
                     getDataFromFCM(ADMIN);
                    break;
                case R.id.loginAsUserBT:
                    USER_TYPE = USER;
                    user = new User(USER, hashMap.get(passwordET), hashMap.get(nameET));
                    //FirebaseDatabaseConnections.getInstance().addUser(user);
                     getDataFromFCM(USER);
                    break;
            }
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
                    saveUserDataToPreference(user);
                    goToNextActivity();
                    Toast.makeText(this, R.string.successfull_login, Toast.LENGTH_SHORT).show();
                }

            } else
                Toast.makeText(this, R.string.invalid_user, Toast.LENGTH_SHORT).show();

        }
    }

    private void goToNextActivity() {
        Intent intent = null;
        if (USER_TYPE.equals(ADMIN))
            intent = new Intent(this, AdminScreen.class);
        else
            intent = new Intent(this, UserScreen.class);
        startActivity(intent);
        finish();
    }

    private void saveUserDataToPreference(User user) {
        MySharedPereference.getInstance().setString(this, Constants.EMAIL, user.getEmail());
        MySharedPereference.getInstance().setString(this, Constants.USER_TYPE, user.getUserType());
        MySharedPereference.getInstance().setBoolean(this, Constants.LOGGED_IN, true);
    }
}
