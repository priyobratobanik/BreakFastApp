package tes.com.breakfast.DatabaseManagement;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

import tes.com.breakfast.DTO.Order;
import tes.com.breakfast.DTO.User;
import tes.com.breakfast.Exception.BreakfastException;



public class Login {
    private Context mAppContext;
    private User mCurrentUser = null;

    public Login(Context context) {
        this.mAppContext = context;
    }

    public User doLogin(final String userName, final String password) throws BreakfastException, InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean flag=false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String tUsername = postSnapshot.child("mUserName").getValue(String.class);
                        String tPassword = postSnapshot.child("mPassword").getValue(String.class);
                        String tAddress = postSnapshot.child("mAddress").getValue(String.class);
                        String tTimestamp = postSnapshot.child("mLastLoginTime").getValue(String.class);
                        Order[] tOrders = postSnapshot.child("mOrders").getValue(Order[].class);
                        if (tUsername != null && tPassword != null)
                            if (tUsername.equals(userName) && tPassword.equals(password)) {
                                mCurrentUser = new User(tUsername, tPassword, tAddress, tTimestamp, tOrders);
                                flag=true;
                            }
                    }

                    if(!flag)
                    {
                        mCurrentUser=new User("NotPresent","","","",null);
                    }

                    countDownLatch.countDown();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        countDownLatch.await();

        if(mCurrentUser!=null)
        Log.d("CheckHere", mCurrentUser.getmUserName());
        return mCurrentUser;
    }
}
