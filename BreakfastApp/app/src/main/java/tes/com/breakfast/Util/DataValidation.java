package tes.com.breakfast.Util;

/**
 * Created by Aashijit on 16/10/2018.
 */

public class DataValidation
{
    public DataValidation()
    {
    }

    /**
     * Returns true if the username is not empty
     * @param userName
     * @return
     */
    public static boolean validateUserName(String userName)
    {
        return (!userName.isEmpty());
    }

    /**
     * Returns true if the password matches the criteria
     * characters must be between [A-Z, a-z, 0-9] and minimum length of 8
     * @param password
     * @return
     */
    public static boolean validatePassword(String password)
    {
        return (password.length()>8 && password.matches("^[A-Za-z0-9]*"));
    }

    /**
     * Returns true if the password matches confirm password
     * @param password
     * @param confirmPassword
     * @return
     */
    public static boolean validateConfirmPassword(String password, String confirmPassword)
    {
        return (password.equals(confirmPassword));
    }

    /**
     * Returns true if the address is not empty
     * @param address
     * @return
     */
    public static boolean validateAddress(String address)
    {
        return (!address.isEmpty());
    }
}
