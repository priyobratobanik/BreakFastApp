package tes.com.breakfast.DTO;

import android.net.Uri;

import java.io.File;

/**
 * Created by Aashijit on 28/10/2018.
 */

public class Items
{
    private String itemName;
    private String itemPrice;
    private String itemImage;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
