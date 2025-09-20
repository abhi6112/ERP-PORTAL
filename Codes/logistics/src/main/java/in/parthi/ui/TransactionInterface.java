package in.parthi.ui;

import java.time.LocalDate;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import in.parthi.common.PaymentMode;
import in.parthi.common.Properties;
import in.parthi.common.TransactionCategory;
import in.parthi.common.TransactionType;
import in.parthi.core.model.transaction.Transaction;
import in.parthi.core.service.TransactionService;

public class TransactionInterface {
    private static final Logger logger = LoggerFactory.getLogger(TransactionInterface.class);
    TransactionService transactionService = new TransactionService();

    /**
     * This method take the transaction id and call service class to retrieve the corresponding
     * transaction.
     * 
     * @return Returns a transaction matching with transaction ID
     */

    public Transaction gettransaction() {
        Transaction transaction = null;
        Scanner sc = Properties.getSacnnerInstance();
        sc.nextLine();
        logger.info("Transaction id is entered which needs to be retrive");
        System.out.println("Please enter the ID: ");
        String id = sc.nextLine();
        transaction = transactionService.getTransaction(id);
        return transaction;

    }



    /**
     * This method is called from the add Products with a transaction instance to make a consolidated AddProduct Object.
     * 
     * @return Returns a responce message for the addition action of transaction
     */
    public void addTransaction(Transaction transaction) {
        // String response = null;
        logger.info("Start taking transaction details from user");
        Scanner sc = Properties.getSacnnerInstance();
        sc.nextLine();
        try {
            // Take transaction details from user
            LocalDate today = LocalDate.now();
            System.out.print("Enter Transaction Date e.g 2025-09-23 (default: " + today.toString() + "): ");
            String strDate = sc.nextLine();
            if (strDate.length() == 0) {
                transaction.setTransactionDate(today);
            } else {
                transaction.setTransactionDate(LocalDate.parse(strDate));
            }

            //
            System.out.print("Enter Invoice Id(if any): ");
            transaction.setInvoice(sc.nextLine());

            //
            System.out.print("Enter Particular (if any): ");
            transaction.setParticular(sc.nextLine());

            // Setting the transaction type when user select the correct option between 1-2
            // Usend enum class for transaction type
            System.out.println("Enter Transaction Type: ");
            transaction.setTxnType(TransactionType.DEBIT.name());
            System.out.print("Transaction type is : DEBIT ");
            sc.nextLine();

            // Setting the Payment mode when the user select the options
            // Usend Enum for the payment mode.
            System.out.println("Enter Payment Mode: ");
            PaymentMode.choose(transaction);

            // Setting the Transaction category when the user select the options
            // Usend Enum for the payment mode.
            System.out.println("Enter Transaction Category: ");
            TransactionCategory.choose(transaction);


            // flushing the extra enter
            sc.nextLine();
            //
            System.out.print("Enter Description: ");
            transaction.setDescription(sc.nextLine());

            //
            System.out.print("Enter Amount: ");
            transaction.setAmount(sc.nextDouble());

            // response = transactionService.addTransaction(transaction);
            transactionService.addTransaction(transaction);
        } catch (RuntimeException e) {
            // response = e.getLocalizedMessage();
            e.getLocalizedMessage();
            logger.error("Exception occured while adding transaction: " + e.getLocalizedMessage());
        }
    }

    /**
     * This method take a transaction category or a blank string to add the transaction.
     * 
     * @return Returns a responce message for the addition action of transaction
     */
    public String addTransaction(String txnCategory) {
        String response = "";
        logger.info("Start taking transaction details from user");
        Scanner sc = Properties.getSacnnerInstance();
        sc.nextLine();
        LocalDate today = LocalDate.now();
        LocalDate inDate;
        Transaction transaction = new Transaction();

        // Setting the Transaction category when the user select the options
        // Usend Enum for the payment mode.
        if (txnCategory == null || txnCategory.length() == 0) {
            System.out.println("Enter Transaction Category: ");
            TransactionCategory.choose(transaction);
        }else{
            transaction.setTxnCategory(txnCategory);
        }


        try {
            // Take transaction details from user
            do {
                System.out.print("Enter Stockin Date e.g 2025-09-23 (default: " + today.toString() + "): ");
                sc.nextLine();// to flush the extra enter
                String strDate = sc.nextLine();
                if (strDate.length() == 0) {
                    transaction.setTransactionDate(today);
                    sc.nextLine();
                    break;
                } else {
                    inDate = LocalDate.parse(strDate);
                    if (inDate.compareTo(today) <= 0) {
                        transaction.setTransactionDate(inDate);
                        break;
                    } else {
                        System.out.println("Please enter a valid date. Future date is not a valid input");
                    }

                }
            } while (true);

            //
            System.out.print("Enter Invoice Id(if any): ");
            transaction.setInvoice(sc.nextLine());

            //
            System.out.print("Enter Particular (if any): ");
            transaction.setParticular(sc.nextLine());

            // Setting the transaction type when user select the correct option between 1-2
            // Usend enum class for transaction type
            // System.out.println("Enter Transaction Type: ");
            // TransactionType.choose(transaction);

            if(transaction.getTxnCategory().equalsIgnoreCase("sales") || transaction.getTxnCategory().equalsIgnoreCase("installment")){
                transaction.setTxnType(TransactionType.CREDIT.name());
                System.out.print("Transaction type is : CREDIT ");
                sc.nextLine();
            } else {
                transaction.setTxnType(TransactionType.DEBIT.name());   // "DEBIT"
                System.out.print("Transaction type is : DEBIT ");
                sc.nextLine();
            }

            // Setting the Payment mode when the user select the options
            // Usend Enum for the payment mode.
            System.out.println("Enter Payment Mode: ");
            PaymentMode.choose(transaction);

            // flushing the extra enter
            sc.nextLine();
            //
            System.out.print("Enter Description: ");
            transaction.setDescription(sc.nextLine());

            //
            System.out.print("Enter Amount: ");
            transaction.setAmount(sc.nextDouble());

            response = transactionService.addTransaction(transaction);
        } catch (RuntimeException e) {
            response = e.getLocalizedMessage();
            logger.error("Exception occured while adding transaction: " + e.getLocalizedMessage());
        }
        return response;
    }
}
