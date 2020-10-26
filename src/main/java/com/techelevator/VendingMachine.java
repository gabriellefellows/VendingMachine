package com.techelevator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {
	private List<Item> itemsInMachine;
	private double currentBalance;
	private File auditFile;
	private PrintWriter outFile;
	private Date date = new Date();
	private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

// constructor
	public VendingMachine() throws FileNotFoundException {
		itemsInMachine = new ArrayList<>();
		currentBalance = 0;
		auditFile = new File("Audit Report.txt");
		outFile = new PrintWriter(auditFile);
	}

	// method for restocking machine
	public List<Item> restockingMachine() throws FileNotFoundException {
		List<Item> inventory = new ArrayList<>();

		File vendingMachineItems = new File("./vendingmachine.csv");
		if (!vendingMachineItems.exists() || !vendingMachineItems.isFile()) {
			System.out.println("path specified is not an existing file");
			System.exit(16);
		}

		Scanner readItems = new Scanner(vendingMachineItems);
		String theLine = ""; // input holds line from file

		while (readItems.hasNextLine()) {
			theLine = readItems.nextLine();
			String[] theCategories = theLine.split("\\|");
			Item itemToAdd = new Item(theCategories[0], theCategories[1], Double.parseDouble(theCategories[2]),
					theCategories[3], 5);
			inventory.add(itemToAdd);
		}

		readItems.close();
		return inventory;
	}

// -------------------------------------------- method for feedingMoney
	public void feedMoney() {
		String amountToAdd;

		System.out.println("Enter amount to add: 1, 2, 5, 10");
		Scanner moneyFeeder = new Scanner(System.in);

		amountToAdd = moneyFeeder.nextLine();
		switch (amountToAdd) {
		case "1":
			currentBalance += 1;
			break;
		case "2":
			currentBalance += 2;
			break;
		case "5":
			currentBalance += 5;
			break;
		case "10":
			currentBalance += 10;
			break;
		default:
			System.out.println("Amount is not valid, try again.");
			break;
		}

		VendingMachine.this.getOutFile()
				.append("\n" + formatter.format(date) + " FEED MONEY: $" + amountToAdd + ".0" + " $" + currentBalance);
	}

	// ------------------------------------------------- method for a user selecting
	// a product
	public void productSelection() {
		System.out.println("Enter your selection: ");
		Scanner readProduct = new Scanner(System.in);

		String userSelection = readProduct.nextLine();

		for (int i = 0; i < itemsInMachine.size(); i++) {
			if (itemsInMachine.get(i).getSlotNumber().contentEquals(userSelection.toUpperCase())) {
				if (currentBalance >= itemsInMachine.get(i).getItemCost()
						&& itemsInMachine.get(i).getStockAmount() >= 1) {

					System.out.println(itemsInMachine.get(i).getSaying() + "\n" + itemsInMachine.get(i).getName() + "\n"
							+ itemsInMachine.get(i).getItemCost());

					currentBalance = currentBalance - itemsInMachine.get(i).getItemCost();
					String currentBalanceFormatter = String.format("%.2f", currentBalance);
					currentBalance = Double.parseDouble(currentBalanceFormatter);
					itemsInMachine.get(i).removeAStock();

					VendingMachine.this.getOutFile()
							.append("\n" + formatter.format(date) + " " + itemsInMachine.get(i).getName() + " "
									+ itemsInMachine.get(i).getSlotNumber() + " $"
									+ (currentBalance + itemsInMachine.get(i).getItemCost()) + " $" + currentBalance);

				} else {
					if (currentBalance < itemsInMachine.get(i).getItemCost()) {
						System.out.println("Insufficient Funds");
					} else {
						if (itemsInMachine.get(i).getStockAmount() == 0) {
							System.out.println("Item Sold Out, Choose Again.");
						}
					}
				}
			}
		}

	}
	

	// ----------------------------------------method for returning change
	public void givingChange() {
		double endMoneyPlaceholder = currentBalance * 100;


		int quartersReturned = (int) (endMoneyPlaceholder / 25);
		endMoneyPlaceholder = endMoneyPlaceholder % 25;
		int dimesReturned = (int) (endMoneyPlaceholder / 10);
		endMoneyPlaceholder = endMoneyPlaceholder % 10;
		int nickelsReturned = (int) endMoneyPlaceholder / 5;
		endMoneyPlaceholder = endMoneyPlaceholder % 5;

		System.out.println("Your change is: " + quartersReturned + " quarters, " + dimesReturned + " dimes, and "
				+ nickelsReturned + " nickels.");

	}

	public void auditReport() throws IOException {
		String addToAudit = "";

		if (auditFile.exists()) { 
			FileWriter writeToAudit = new FileWriter(auditFile, true);
			BufferedWriter bufferForAudit = new BufferedWriter(writeToAudit);

			outFile = new PrintWriter(bufferForAudit);
		} 

	} 

	// ------------ method for creating sales report ------
	public void createSalesReport() {
		LocalDateTime sales = LocalDateTime.now();
		DateTimeFormatter nowFormatter = DateTimeFormatter.ofPattern("MM_dd_yyyy_hh_mm_ss_a");
		String nowFormatted = sales.format(nowFormatter);
		File salesFile = new File("Sales Report" + nowFormatted + ".txt");
		
		try {
			PrintWriter writeFile = new PrintWriter(salesFile); 
			writeFile.println("Sales Report Generated " + formatter.format(date));
			double totalSum = 0.0;

			for (int i = 0; i < itemsInMachine.size(); i++) {
				int numberOfItemsSold = 5 - itemsInMachine.get(i).getStockAmount();
				writeFile.println(itemsInMachine.get(i).getName() + " |" + numberOfItemsSold);
				totalSum += itemsInMachine.get(i).getItemCost() * numberOfItemsSold;

			}
			writeFile.println("\nTotal sales: " + totalSum);
			System.out.println("Sales Report Created");

			writeFile.close();
		} catch (IOException exceptionObject) {
			System.out.println("**Something Went Wrong**");
		}
	}

	// ---------------
	public List<Item> getItemsInMachine() {
		return itemsInMachine;
	}

	public void setItemsInMachine(List<Item> itemsInMachine) {
		this.itemsInMachine = itemsInMachine;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public File getAuditFile() {
		return auditFile;
	}

	public PrintWriter getOutFile() {
		return outFile;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public SimpleDateFormat getFormatter() {
		return formatter;
	}

	public void setFormatter(SimpleDateFormat formatter) {
		this.formatter = formatter;
	}

	public void setOutFile(PrintWriter outFile) {
		this.outFile = outFile;
	}

}
