package tes.com.breakfast.DatabaseManagement;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

import tes.com.breakfast.Exception.BreakfastException;



public class ForgotPassword {
    private static String status;
    public ForgotPassword() {
    }

    public static String doChangePassword(final String userName, final String oldPassword, final String newPassword, final Context context) throws BreakfastException, InterruptedException {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference("Users");
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        String userId=dataSnapshot.getKey();
                        String tUsername = postSnapshot.child("mUserName").getValue(String.class);
                        String tPassword = postSnapshot.child("mPassword").getValue(String.class);
                        if(tUsername!=null && tPassword!=null)
                        {
                            if(tUsername.equals(userName) && tPassword.equals(oldPassword))
                            {
                                reference.child(userId).child("mPassword").setValue(newPassword);
                                status="Password Change Successful";
                            }
                            else
                                status="User Not Found !!! ";
                        }
                    }
                    countDownLatch.countDown();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                status="Server Failure !!!";
            }
        });

        countDownLatch.await();
        return status;
    }
}
