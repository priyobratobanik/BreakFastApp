package tes.com.breakfast.admin.DTO;

import android.net.Uri;


public class AdminItem
{
    private String itemName;

    private String downloadUri;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }
}
