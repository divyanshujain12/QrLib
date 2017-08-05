package com.example.divyanshujain.qrcodereaderlib.FirebaseDatabase;

import android.util.Log;

import com.example.divyanshujain.qrcodereaderlib.Callbacks.GetOtherUserCallback;
import com.example.divyanshujain.qrcodereaderlib.Callbacks.GetUserCallback;
import com.example.divyanshujain.qrcodereaderlib.Models.OtherUser;
import com.example.divyanshujain.qrcodereaderlib.Models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by divyanshu.jain on 8/4/2017.
 */

public class FirebaseDatabaseConnections implements ChildEventListener, ValueEventListener {
    private DatabaseReference mFirebaseDatabase;
    private static FirebaseDatabase mFirebaseInstance;
    private String userId = "userTypes";
    private GetUserCallback getUserCallback;
    private GetOtherUserCallback otherUserCallback;
    private static FirebaseDatabaseConnections firebaseDatabaseConnections = new FirebaseDatabaseConnections();

    private FirebaseDatabaseConnections() {


    }

    public static FirebaseDatabaseConnections getInstance() {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        return firebaseDatabaseConnections;
    }

    public void addUser(User user) {
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        mFirebaseDatabase.child(user.getUserType()).setValue(user);
        addUserChangeListener(user.getUserType());
    }

    public void addOtherUser(OtherUser user) {
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        String id = removeUnwantedSymbols(user.getQrCode());
        mFirebaseDatabase.child(id).setValue(user);
        addUserChangeListener(id);
    }

    private String removeUnwantedSymbols(String id) {
        return id.replaceAll("[\\[;\\/:#.$'\\]]", "");
    }


    public void getUser(String id, GetUserCallback getUserCallback) {
        this.getUserCallback = getUserCallback;
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        DatabaseReference adminUser = mFirebaseDatabase.child(id);
        Query citiesQuery = adminUser.orderByKey();
        citiesQuery.addValueEventListener(this);

    }

    public void getOtherUser(String id, GetOtherUserCallback getOtherUserCallback) {
        this.otherUserCallback = getOtherUserCallback;
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        id = removeUnwantedSymbols(id);
        DatabaseReference adminUser = mFirebaseDatabase.child(id);
        Query citiesQuery = adminUser.orderByKey();
        citiesQuery.addValueEventListener(this);

    }

    private void addUserChangeListener(String id) {
        // User data change listener
        mFirebaseDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("OnValueEventListener :-", dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        if (getUserCallback != null) {
            User user = dataSnapshot.getValue(User.class);
            getUserCallback.getUserOnSuccess(user);
            getUserCallback = null;
        } else if (otherUserCallback != null) {
            OtherUser otherUser = dataSnapshot.getValue(OtherUser.class);
            otherUserCallback.onGetOtherUser(otherUser);
            otherUserCallback = null;
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
