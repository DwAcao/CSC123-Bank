/** Modified by:
 * Alex Cao	(acao4@toromail.csudh.edu)
 * 
 * add support for currencies other than US Dollars
 * 
 * Bank will read and store a file containing foreign exchange rates
 * bank will import read file input output stream, or file input/output.
 * 
 * I tried to do what I could, but to no avail I don't know what I am doing.
 * 
 */
package com.usman.csudh.bank.core;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Bank {
	
	private static final String FilePath = "exchange-rate.csv"; //added line
	private static HashMap<String, Double> exchangeRateMap;		//added line
	private static Map<Integer,Account> accounts=new TreeMap<Integer,Account>();
	
	// added constructor. 
	public Bank() 
	{
		exchangeRateMap = getExchangeRates();
		
		if(exchangeRateMap != null && exchangeRateMap.size()> 0) {
			System.out.println("Currency exchange rates successfully loaded!");
		}
		else {
			System.out.println("Warning: Currency exchange file has not been found or read.");
		}
		
	}// End of added lines
	
	// Assignment6 3 - Open Account
	public static Account openCheckingAccount(String firstName, String lastName, String ssn, double overdraftLimit) {
		Customer c=new Customer(firstName,lastName, ssn);
		Account a=new CheckingAccount(c,overdraftLimit);
		accounts.put(a.getAccountNumber(), a);
		return a;
		
	}
	
	public static Account openSavingAccount(String firstName, String lastName, String ssn) {
		Customer c=new Customer(firstName,lastName, ssn);
		Account a=new SavingAccount(c);
		accounts.put(a.getAccountNumber(), a);
		return a;
		
	}

	
	
	public static Account lookup(int accountNumber) throws NoSuchAccountException{
		if(!accounts.containsKey(accountNumber)) {
			throw new NoSuchAccountException("\nAccount number: "+accountNumber+" nout found!\n\n");
		}
		
		return accounts.get(accountNumber);
	}
	
	public static void makeDeposit(int accountNumber, double amount) throws AccountClosedException, NoSuchAccountException{
		
		lookup(accountNumber).deposit(amount);
		
	}
	
	public static void makeWithdrawal(int accountNumber, double amount) throws InsufficientBalanceException, NoSuchAccountException {
		lookup(accountNumber).withdraw(amount);
	}
	
	public static void closeAccount(int accountNumber) throws NoSuchAccountException {
		lookup(accountNumber).close();
	}

	
	public static double getBalance(int accountNumber) throws NoSuchAccountException {
		return lookup(accountNumber).getBalance();
	}
	
	// Assignment6 4 - List Accounts need to add functionality of allowing user to enter account number,
	// will also output formated information for the user.
	// if the account currency is in USD then both balances will be in USD.
	public static void listAccounts(OutputStream out) throws IOException{
		
		out.write((byte)10);
		Collection<Account> col=accounts.values();
		
		for (Account a:col) {
			out.write(a.toString().getBytes());
			out.write((byte)10);
		}
		
		out.write((byte)10);
		out.flush();
	}
	
	public static void printAccountTransactions(int accountNumber, OutputStream out) throws IOException,NoSuchAccountException{
		
		lookup(accountNumber).printTransactions(out);
	}

	// start of new lines
	public static double getExchangeRateStr(String CurrencySelling, String CurrencyBuying) {
		// From Assignment6 2 - Currency Conversion, need to implement String String some how within
		// HashMap. I don't know what to do, do I create another HashMap?
		return 0.0;
		
	}
	
	// From Assignment6 1 - Foreign Exchange file. I Implemented some of it, but I got lost and don't 
	// know how to do the rest.
	// Method gets exchange rates from csv file and store currency code and exchange rate in a HashMap
	public static HashMap<String, Double> getExchangeRates() {
		
		HashMap<String, Double> exchangeRateMap = new HashMap<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(FilePath))) 
		{
			String line;
			while((line = br.readLine()) != null) {
				String[] values = line.split(",");
				String CurrencyCode = values[0];
				String CurrencyName = values[1];
				double ExchangeRate = Double.parseDouble(values[2]);
			
				exchangeRateMap.put(CurrencyCode, ExchangeRate);
			}
			return exchangeRateMap;
		}
		catch (IOException IOE) {
			System.out.println("**Currency file could not be loaded, "
				+ "Currency Conversion Service and Foreign Currency accounts are not available**");
			return null;
		}
	}// end of HashMap
	
	// method to convert one currency to another
	public static double convertAmount(double amount, double exchangeRate, String currency)
	{
		if(currency.equals("USD")) {
			return amount / exchangeRate;
		}
		else {
			return amount * exchangeRate;
		}
	}
	
	// This method is probably not needed
	public static double ConvertToUSD(String CurrencyCode, double amount) {
		if(exchangeRateMap != null && exchangeRateMap.containsKey(CurrencyCode)) {
			return(amount * exchangeRateMap.get(CurrencyCode));
		}
		else {
			System.out.println("ERROR: Currency Code not found in exchange rate file!");
			return -1;
		}
	}
	
	// This method is probably not needed
	public static double ConvertFromUSD(String CurrencyCode, double amount) {
		if(exchangeRateMap != null && exchangeRateMap.containsKey(CurrencyCode)) {
			return (amount / exchangeRateMap.get(CurrencyCode));
		}
		else {
			System.out.println("ERROR: Currency Code not found in exchange rate file!");
			return -1;
		}
	}//End of Added lines
}