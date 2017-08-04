package com.example.divyanshujain.qrcodereaderlib.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.divyanshujain.qrcodereaderlib.BR;

/**
 * Created by divyanshu.jain on 8/4/2017.
 */

public class OtherUser extends BaseObservable {
    String qrCode;
    String name;
    String email;
    String phone;
    private String dob;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    @Bindable
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
        notifyPropertyChanged(BR.dob);
    }
}
