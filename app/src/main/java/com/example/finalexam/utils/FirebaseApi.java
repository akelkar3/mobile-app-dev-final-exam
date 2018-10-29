package com.example.finalexam.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.example.finalexam.ChristmasListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

/**
 * Created by akelkar3 on 4/23/2018.
 */

public class FirebaseApi {
    public static final String USER_KEY="user";
    public static final String MESSAGE_KEY="message";
    public Activity activity;
    final String TAG = "test";
    public FirebaseAuth mAuth;
    public FirebaseDatabase mDatabase;


    public FirebaseApi(Activity activity) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    public void login(String username, final String password) {
        Log.d(TAG, "login: "+username + " "+ password);
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            SharedPreferences sharedPref =  activity.getSharedPreferences(
                                    "mypref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            //saving user full name and user Id that might require on threads or messages activity
                            Log.d("tesetdelete", "saveToken: "+  user.getUid()+" name: "+user.getDisplayName());
                           // editor.putString("uid", user.getUid());
                            editor.putString("username",user.getEmail());
                            editor.putString("password", password);
                            editor.apply();
                            //write intent to switch new activity

                            // TODO: 4/23/2018 change SignupActivity to the Inbox activity
                            Intent intent=new Intent(activity, ChristmasListActivity.class);
                            //  intent.putExtra(USER_KEY,new AppUser(user.getUid(),user.getDisplayName(),user.getEmail()));
                            activity.startActivity(intent);
                            activity.finish();
                            Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show();
                            //   updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(activity, "Login Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //   updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void SignUp(final String username, final String password, final String fname, final String lname) {
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            String displayName=user.getEmail().substring(0,user.getEmail().indexOf("@"));
                            UserProfileChangeRequest prof = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName).build();
                            user.updateProfile(prof)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated." + mAuth.getCurrentUser().getDisplayName());
                                                //addAppUser(user.getDisplayName(),user.getEmail());
                                                login(username, password);
                                            }else {
                                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                Toast.makeText(activity, "Login Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });

                            //   updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }



    public void addPerson(String name, String budget) {
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference threadRef = mDatabase.getReference("persons").child(user.getDisplayName());
        Person person = new Person();
        person.id  = threadRef.push().getKey();
        person.name= name;
        person.totalBudget=Integer.parseInt(budget);
        person.giftCount=0;
        person.totalBought=0;
        threadRef.child(person.id).setValue(person);
    }
    public void updatePerson(Person person, int price) {
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference threadRef = mDatabase.getReference("persons").child(user.getDisplayName());
        person.giftCount=person.giftCount+1;
        person.totalBought=person.totalBought+price;
        threadRef.child(person.id).setValue(person);

    }
    public void addGift(Gift gift, Person person) {
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference threadRef = mDatabase.getReference("personGifts/"+user.getDisplayName()+"/"+person.name);

        gift.id  = threadRef.push().getKey();
        threadRef.child(gift.id).setValue(gift);
        updatePerson(person,gift.price);
    }
    public void logout(){
        SharedPreferences sharedPref = activity.getSharedPreferences(
                "mypref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        Log.d(TAG, "loggedOut: ");
    }
    public  void autoLogin(){

        String username,pass;
        SharedPreferences sharedPref =activity.getSharedPreferences(
                "mypref", Context.MODE_PRIVATE);

        username = sharedPref.getString("username",null);
        pass= sharedPref.getString("password",null);

        if (username!= null && !username.isEmpty()&&pass!= null && !pass.isEmpty())
        login(username,pass);

    }
  /*  public  void deleMessage(String messageId){
        DatabaseReference threadRef= mDatabase.getReference("mailbox/"+mAuth.getCurrentUser().getDisplayName());
        threadRef.child(messageId).removeValue();
        Intent intent=new Intent(activity, InboxActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
    public  void messageRead(String messageId){
        DatabaseReference threadRef= mDatabase.getReference("mailbox/"+mAuth.getCurrentUser().getDisplayName());
        threadRef.child(messageId+"/IsRead").setValue(true);

    }*/
}


