package tes.com.breakfast.admin.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import tes.com.breakfast.R;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void doUploadItemFromView(View view)
    {
        Intent transferIntent=new Intent(AdminHomeActivity.this,AdminUploadNewItem.class);
        startActivity(transferIntent);
    }
}
