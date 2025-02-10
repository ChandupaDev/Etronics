package model;

public class Validation {
    
    public static boolean isEmailValid(String email) {
        return email.matches("^[A-Za-z0-9\\._%+\\-]+@[A-Za-z0-9\\.\\-]+\\.[A-Za-z]{2,}");
    }
    
    public static boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");
    }
    
    public static boolean isDouble(String text){
        return text.matches("^\\d+(\\.\\d{2})?$");
    }
    
     public static boolean isInteger(String text){
        return text.matches("^\\d+$");
    }
     
     public static boolean isMobileNumberValid(String mobile) {
    
        return mobile.matches("^07[01245678]{1}[0-9]{7}$");
    
    }
}
