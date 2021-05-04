package com.techelevator;

import com.techelevator.view.*;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.*;

public class VendingMachineCLI {

    private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
    private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
    private static final String MAIN_MENU_OPTION_EXIT = "Exit";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT};
    private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
    private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
    private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
    private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION};
    private static final String FEED_MONEY_OPTION = "Enter amount to add to balance (1, 2, 5, 10): ";
    private static final String SELECT_PRODUCT_OPTION = "Enter Slot Number: ";
    private static final String FINISH_TRANSACTION_OPTION = "Select to receive change";
    private static final String[] SELECT_PRODUCTS_OPTIONS = {FEED_MONEY_OPTION, SELECT_PRODUCT_OPTION, FINISH_TRANSACTION_OPTION};

    private Menu menu;
    private List<Inventory> vendingItems = new ArrayList<>();
    private User user = new User();
    private Scanner userInput = new Scanner(System.in);

    public VendingMachineCLI(Menu menu) {

        this.menu = menu;
    }

    public void run() {  // think of this like the main, because it is being called here
        FileInventory file = new FileInventory();
        vendingItems = file.readFile();
        while (true) {
            String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
                displayItems();

            } else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
                purchase();

            } else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        Menu menu = new Menu(System.in, System.out);  // menu object
        VendingMachineCLI cli = new VendingMachineCLI(menu);  // vending machine CLI object, calling the constructor
        cli.run();
    }

    private void displayItems() {
        for (Inventory inv : vendingItems) {
            System.out.println(inv);
        }
    }


    private void purchase() {
        while (true) {
            System.out.println("Current Balance: $" + user.getBalance().setScale(2));
            String purchaseChoice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
            if (purchaseChoice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
                System.out.println(FEED_MONEY_OPTION);
                String feedMoneyString = userInput.nextLine();
                if ((feedMoneyString.equals("1")) || (feedMoneyString.equals("2")) || (feedMoneyString.equals("5")) || (feedMoneyString.equals("10"))) {
                    BigDecimal feedMoney = new BigDecimal((feedMoneyString));
                    user.addToBalance(feedMoney);
                } else {
                    System.out.println("Not a valid entry");
                }
            }
            if (purchaseChoice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
                boolean foundItem = false;
                do { //System.out.printf("%-10s %-25s %-15s %-15s\n","CODE","PRODUCT","PRICE","QTY");
                    //System.out.printf("%-10s %-25s %-15s %-15s\n","----","-------","-----","---");
                    for (Inventory inv : vendingItems) {
                        System.out.println(inv);
                        //System.out.printf("%-10s %-25s %-15s %-15s\n",inv.getSlotNumber(),inv.getItemName(),inv.getPrice(), inv.getQuantity());
                    }
                    System.out.println("\nCurrent balance: $" + user.getBalance().setScale(2));
                    System.out.print("Which item do you wish to buy? ");
                    String itemToPurchase = userInput.nextLine().toUpperCase();
                    for (Inventory inv : vendingItems) {
                            if (inv.getSlotNumber().equals(itemToPurchase)) {

                                if (inv.getQuantity() == 0) {
                                    System.out.println("Item Sold Out. Try Again!");

                                } else {
                                    user.buyItem(inv);
                                    foundItem = true;
                                }
                            }
                        }

                    if (!foundItem) {
                        System.out.println("\n\nInvalid Entry. Try again\n\n");
                        break;
                    }
                }
                while (!foundItem);
            }
            if (purchaseChoice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)) {
                System.out.println(user.giveChange());

                break;  //sends back to main menu
            }
        }
    }
}
