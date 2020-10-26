package com.techelevator;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.techelevator.view.Menu; // Bring in Menu class provided

public class VendingMachineApp {
	/*************************************************************************************************************************
	 * This is the application program to instantiate the VendingMachineCLI main
	 * program and start it running * DO NOT PUT ANY NEW CODE HERE WITHOUT CHECKING
	 * WITH YOUR INSTRUCTOR FIRST!
	 * 
	 * @throws IOException
	 ***************************************************************************************************************************/

	public static void main(String[] args) throws IOException {
		Menu vendingMenu = new Menu(System.in, System.out);
		Menu purchaseMenu = new Menu(System.in, System.out); 
																

		VendingMachineCLI vendingCli = new VendingMachineCLI(vendingMenu, purchaseMenu); 
																							
																							
																							
		vendingCli.run();
		

	}
}
