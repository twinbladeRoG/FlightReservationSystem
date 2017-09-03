/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frs;

/**
 *
 * @author Sohan
 */
public interface DisplayManager {

    /**
     * Function for getting search details from the user
     */
    public void showInputScreen();
    /**
     * Function for showing search results
     */
    public void showSearchResultScreen();
    /**
     * Function for getting booking details
     */
    public void showBookingScreen();
    /**
     * Function to display confirmation details
     */
    public void showConfirmationScreen();
}
