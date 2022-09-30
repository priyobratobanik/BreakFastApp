package tes.com.breakfast.DTO;





public class Order
{
    private String mProductName;
    private String mProductPrice;
    private String mOrderDate;
    private boolean mDeliveryStatus;
    private int mQuantity;

    public int getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getmProductName() {
        return mProductName;
    }

    public void setmProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public String getmProductPrice() {
        return mProductPrice;
    }

    public void setmProductPrice(String mProductPrice) {
        this.mProductPrice = mProductPrice;
    }

    public String getmOrderDate() {
        return mOrderDate;
    }

    public void setmOrderDate(String mOrderDate) {
        this.mOrderDate = mOrderDate;
    }

    public boolean ismDeliveryStatus() {
        return mDeliveryStatus;
    }

    public void setmDeliveryStatus(boolean mDeliveryStatus) {
        this.mDeliveryStatus = mDeliveryStatus;
    }
}
