/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.raneesh.csacwk.webservice.utility;


/**
 *
 * @author Raneesh Gomez
 */
public class RegistrationValidation {
    
    public static boolean validatePassword(String password, String confirmPassword) {
        boolean isPasswordValid = false;
        
        if (password.equals(confirmPassword)) {
            isPasswordValid = true;
        }
        
        return isPasswordValid;
    }
    

    public static boolean validateNickname(String nickname) {
        boolean isNicknameValid = false;

        if (nickname.length() < 20) {
            isNicknameValid = true;
        } 
        return isNicknameValid;
    }   
    
}
