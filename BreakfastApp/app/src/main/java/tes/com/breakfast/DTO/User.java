package tes.com.breakfast.DTO;

import android.os.health.TimerStat;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by Aashijit on 16/10/2018.
 */

public class User
{
    private String mUserName;
    private String mPassword;
    private String mAddress;
    private String mLastLoginTime;
    private Order[] mOrders;

    public User()
    {

    }

    public User(String username, String password, String address, String timestamp, Order[] orders)
    {
        this.mUserName=username;
        this.mPassword=password;
        this.mAddress=address;
        this.mLastLoginTime=timestamp;
        this.mOrders=orders;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmLastLoginTime() {
        return mLastLoginTime;
    }

    public void setmLastLoginTime(String mLastLoginTime) {
        this.mLastLoginTime = mLastLoginTime;
    }

    public Order[] getmOrders() {
        return mOrders;
    }

    public void setmOrders(Order[] mOrders) {
        this.mOrders = mOrders;
    }
}
