package tes.com.breakfast.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tes.com.breakfast.DTO.Items;
import tes.com.breakfast.DatabaseManagement.UserStatus;
import tes.com.breakfast.Exception.BreakfastException;
import tes.com.breakfast.R;
import tes.com.breakfast.Util.HomePageListAdapter;
import tes.com.breakfast.Util.MessageDisplay;
import tes.com.breakfast.admin.DTO.AdminItem;

public class HomeActivity extends AppCompatActivity {

    private ListView mListView;
    private HomePageListAdapter homePageListAdapter;
    private DatabaseReference mDataReference;
    private List<Items> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Variables are to be initialized
        doVariableInitialization();

        //Download the items here in this activity
        mDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    AdminItem item = dsp.getValue(AdminItem.class);
                    String[] name_price = item.getItemName().split("_");
                    Items viewItem=new Items();
                    viewItem.setItemImage(item.getDownloadUri());
                    viewItem.setItemName(name_price[0]);
                    viewItem.setItemPrice("Rs "+name_price[1].substring(0,name_price[1].length()-3)+" /-");

                    items.add(viewItem);
                }

                //set up adapter here
                if (items != null) {
                    homePageListAdapter = new HomePageListAdapter(items, HomeActivity.this);
                    mListView.setAdapter(homePageListAdapter);
                } else
                    MessageDisplay.errorMessage("No AdminItem Present", HomeActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                MessageDisplay.errorMessage(databaseError.getMessage(), HomeActivity.this);
            }
        });
    }

    /**
     * Open the orders of the particular person
     *
     * @param view
     */
    public void doShowShoppingCart(View view) {

    }

    /**
     * Member variables initialized here
     * Objects are created
     */
    private void doVariableInitialization() {
        mListView = (ListView) findViewById(R.id.homePageItemListView);
        mDataReference = FirebaseDatabase.getInstance().getReference("AdminItem");
        items = new ArrayList<>();
    }


    public void doSignOut(View view) {
        UserStatus status = new UserStatus(HomeActivity.this);
        try {
            status.setLogin("LoggedOut");
        } catch (BreakfastException ex) {
            MessageDisplay.errorMessage(ex.getMessage(), HomeActivity.this);
        }

        //Successful log out
        //Transfer Control to Login Screen
        MessageDisplay.successMessage("Logged Out", HomeActivity.this);
        Intent transferIntent = new Intent(HomeActivity.this, AppLaunchActivity.class);
        startActivity(transferIntent);
        finish();
    }
}