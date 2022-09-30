package tes.com.breakfast.Exception;

/**
 * Created by Aashijit on 16/10/2018.
 */

public class BreakfastException extends java.lang.Exception
{
    private String mMessage;

    public BreakfastException(String s)
    {
        this.mMessage=s;
    }

    @Override
    public String getMessage()
    {
            return mMessage;
    }
}
