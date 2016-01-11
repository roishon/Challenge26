package com.shachor.restfull26.transactionservice;


import org.springframework.web.bind.annotation.*;
import java.util.Set;

/**
 * Class TransactionController handles REST request and generate responses.
 * The Method of this class are not being called directly by the client class,
 * but this is handled by the spring framework according to the @RequestMapping annotation
 * @author  Roi Shachor on 09.01.16.
 */
@RestController
public class TransactionController {

    /**Reference to an Object, where the transactions are stored*/
    private TransactionDBMockSingleton db = TransactionDBMockSingleton.getInstance();


    /**
     * Stores one transaction, which was sent with an HTTP PUT request
     * If the unique ID of the transaction in the request body does not match the ID in the request path
     * the returned TransactionStatus Object will holds the value "error";
     *
     * @param transaction_id the unique id of the transaction
     * @param transaction An object of the class Transaction
     * @return An object of the class TransactionStatus containing a short status message
     */
    @RequestMapping(path = "/transactionservice/transaction/{transaction_id}", method = RequestMethod.PUT)
    public TransactionStatus putTransaction (@PathVariable("transaction_id") Integer transaction_id,
                                             @RequestBody Transaction transaction) {

        if(transaction.getTransaction_id() != transaction_id)
            return new TransactionStatus("error");

        if(db.putTransaction(transaction))
            return new TransactionStatus("ok");


        return new TransactionStatus("error - transaction already exists in the storage");
    }



    /**
     * Returns one transaction according to it's unique ID, which was sent with an HTTP GET request
     * If the transaction does not exists, the method returns pointer to null
     * @param transaction_id the unique id of the transaction
     * @return an object of the class Transaction
     */
    @RequestMapping(path = "/transactionservice/transaction/{transaction_id}",method = RequestMethod.GET)
    @ResponseBody
    public Transaction getTransaction (@PathVariable ("transaction_id") Long transaction_id) {
        return  db.getTransaction(transaction_id);
    }




    /**
     * Returns a Set with the unique ID of all transactions of one type.
     * @param type the type of the transactions
     * @return Set with unique IDs
     */
    @RequestMapping(path = "/transactionservice/types/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Set<Long> getTransactionsByType(@PathVariable ("type") String type) {

        return db.getTransactionsByType(type);
    }



    /**
     * Returns the sum of the transaction's amount. This transactions are all linked eventually to
     * one parent transaction, which is determined by it's unique ID as given as a parameter
     * @param transaction_id the ID of the root parent transaction
     * @return the sum of the transaction's amount
     */
    @RequestMapping(path = "/transactionservice/sum/{transaction_id}", method = RequestMethod.GET)
    @ResponseBody
    public TransactionSum getSumTransactions(@PathVariable ("transaction_id") Long transaction_id) {

        TransactionSum sum = new TransactionSum(0);

        db.getSumTransactions(transaction_id, sum);

        return sum;

    }

}
