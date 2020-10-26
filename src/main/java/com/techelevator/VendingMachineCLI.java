package com.techelevator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**************************************************************************************************************************
*  This is your Vending Machine Command Line Interface (CLI) class
*
*  It is the main process for the Vending Machine
*
*  THIS is where most, if not all, of your Vending Machine interactions should be coded
*  
*  It is instantiated and invoked from the VendingMachineApp (main() application)
*  
*  Your code vending machine related code should be placed in here
***************************************************************************************************************************/
import com.techelevator.view.Menu; // Gain access to Menu class provided for the Capstone

public class VendingMachineCLI {

	// Main menu options defined as constants

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_SALES_REPORT = "Sales Report";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_SALES_REPORT };

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY,
			PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION };

	private Menu vendingMenu; 

	private Menu purchaseMenu;
	private Date theDate = new Date();
	private SimpleDateFormat theFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

	public VendingMachineCLI(Menu vendingMenu, Menu purchaseMenu) { // Constructor - user will pas a menu for this class
																	
		this.vendingMenu = vendingMenu;
		this.purchaseMenu = purchaseMenu;
	}

	/**************************************************************************************************************************
	 * VendingMachineCLI main processing loop
	 * 
	 * Display the main menu and process option chosen
	 *
	 * It is invoked from the VendingMachineApp program
	 *
	 * THIS is where most, if not all, of your Vending Machine objects and
	 * interactions should be coded
	 *
	 * Methods should be defined following run() method and invoked from it
	 * 
	 * @throws IOException
	 *
	 ***************************************************************************************************************************/

	public void run() throws IOException {
		VendingMachine vendingMachine = new VendingMachine();
		vendingMachine.setItemsInMachine(vendingMachine.restockingMachine());

		boolean shouldProcess = true; 

		while (shouldProcess) { 

			String choice = (String) vendingMenu.getChoiceFromOptions(MAIN_MENU_OPTIONS); // Display menu and get choice

			switch (choice) { // Process based on user menu choice

			case MAIN_MENU_OPTION_DISPLAY_ITEMS:
				displayItems(vendingMachine); // invoke method to display items in Vending Machine
				break;

			case MAIN_MENU_OPTION_PURCHASE:
				purchaseItems(vendingMachine); // invoke method to purchase items from Vending Machine
				break; 

			case MAIN_MENU_OPTION_EXIT:
				endMethodProcessing(vendingMachine);
				shouldProcess = false; // Set variable to end loop
				break; 

			case MAIN_MENU_OPTION_SALES_REPORT:
				endMethodProcessing(vendingMachine); // Invoke method to perform end of method processing
				vendingMachine.createSalesReport();
				shouldProcess = false; // Set variable to end loop
				break; 
			}
		}
		return; // End method and return to caller
	}

	/********************************************************************************************************
	 * Methods used to perform processing Main Menu Actions
	 ********************************************************************************************************/
	public void displayItems(VendingMachine vendingMachine) {
		for (Item item : vendingMachine.getItemsInMachine()) {
			System.out.println(item.toString());
		}
		
	}

	// -----------------------------------------------

	public void purchaseItems(VendingMachine vendingMachine) throws IOException {   
																					
																					
		boolean shouldProcess = true; 

		while (shouldProcess) { // Loop until user indicates they want to exit
			String balanceFormatter = String.format("%.2f", vendingMachine.getCurrentBalance());
			System.out.println("\nCurrent Balance is: " + balanceFormatter);
			String choice = (String) purchaseMenu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS); // Display menu and get
																								

			switch (choice) { // Process based on user menu choice

			case PURCHASE_MENU_OPTION_FEED_MONEY:
				vendingMachine.feedMoney();

				break; // Exit switch statement

			case PURCHASE_MENU_OPTION_SELECT_PRODUCT:
				for (Item item : vendingMachine.getItemsInMachine()) {
					System.out.println(item.selectProductToString());
				}
				vendingMachine.productSelection();
				break; // Exit switch statement

			case PURCHASE_MENU_OPTION_FINISH_TRANSACTION:
				endMethodProcessing(vendingMachine); // Invoke method to perform end of method processing
				vendingMachine.givingChange();
				vendingMachine.auditReport();
				vendingMachine.getOutFile().append("\n" + theFormatter.format(theDate) + " GIVE CHANGE: $"
						+ vendingMachine.getCurrentBalance() + " $0.00");
				vendingMachine.setCurrentBalance(0);
				shouldProcess = false; // Set variable to end loop
				break; // Exit switch statement
			}
		}
	}

	public void endMethodProcessing(VendingMachine vendingMachine) {
		vendingMachine.getOutFile().close();

		

	}

}
