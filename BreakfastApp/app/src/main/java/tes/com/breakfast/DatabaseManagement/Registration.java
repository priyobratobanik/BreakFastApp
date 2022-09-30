package tes.com.breakfast.DatabaseManagement;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;

import tes.com.breakfast.DTO.Order;
import tes.com.breakfast.DTO.User;
import tes.com.breakfast.Exception.BreakfastException;
import tes.com.breakfast.Util.DataValidation;
import tes.com.breakfast.Util.MessageDisplay;

/**
 * Created by Aashijit on 16/10/2018.
 */

public class Registration
{
    private User mUser;
    private Context mAppContext;
    private boolean mFlag;

    public Registration(User user,Context context) {
        this.mUser=user;
        this.mAppContext=context;
    }

    /**
     * Registers a particular User
     * @throws BreakfastException Common Exception Class
     */
    public void register() throws BreakfastException, InterruptedException {
        //Check if the User already exists

        Timestamp currentTime=new Timestamp(System.currentTimeMillis());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        String userID=reference.push().getKey();

        if(!checkIfUserPresent(mUser.getmUserName())) {
            User validatedUser = new User();

            if (DataValidation.validateUserName(mUser.getmUserName()))
                validatedUser.setmUserName(mUser.getmUserName());
            else
                throw new BreakfastException("Invalid Username");

            if (DataValidation.validatePassword(mUser.getmPassword()))
                validatedUser.setmPassword(mUser.getmPassword());
            else
                throw new BreakfastException("Invalid Password");

            if (DataValidation.validateAddress(mUser.getmAddress()))
                validatedUser.setmAddress(mUser.getmAddress());
            else
                throw new BreakfastException("Invalid Password");

            validatedUser.setmLastLoginTime(currentTime.toString());
            validatedUser.setmOrders(mUser.getmOrders());

            //Ready to insert into database
            reference.child(userID).setValue(mUser);


            //Ready to set User Status
            UserStatus userStatus = new UserStatus(mAppContext);
            userStatus.setLogin("LoggedIn");
        }
        else
            throw new BreakfastException("Exists");
    }


    private boolean checkIfUserPresent(final String userName) throws BreakfastException, InterruptedException {
        mFlag=false;
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String tUsername = postSnapshot.child("mUserName").getValue(String.class);
                        if (tUsername != null)
                            if (tUsername.equals(userName)) {
                                mFlag=true;
                            }
                    }
                    countDownLatch.countDown();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        countDownLatch.await();

        return mFlag;
    }

}