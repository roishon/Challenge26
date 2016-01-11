package com.shachor.restfull26.transactionservice;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;



/**
 * Class TransactionDBMockSingleton mocks the repository functionality of a database
 * for the use of testing the class TransactionController.
 *
 * All transactions are being stored in an HashMap where Key = ID, Value = Transaction;
 * Another Map holds the all transactions types where Key = type, Value = Set of IDs.
 * With Big O notion of O(1), the Hash function enables tremendous scalability of storage with rapid storing and finding
 * of transactions and transaction's type
 * In order to keep the storage management Thread save it uses ConcurrentHashMap objects for both Maps.
 *
 * @author  Roi Shachor on 09.01.16.
 */
public class TransactionDBMockSingleton {


    /**A reference to the one possible instance of this class*/
    private static TransactionDBMockSingleton db;


    /**A Map storing all transactions, mapped by they unique ID*/
    private ConcurrentMap<Long, Transaction> transactions;

    /**A Map string all the unique transaction's ID, mapped by the type of the transaction */
    private ConcurrentMap<String, Set<Long>> transactionsTypes;


    /**
     * This constructor is private so Object of this class can only be created with the method getInstance().
     * Instantiate ths Maps, which are needed to store the transactions
     */
    private TransactionDBMockSingleton() {
        transactions = new ConcurrentHashMap<>();
        transactionsTypes = new ConcurrentHashMap<>();
    }


    /**
     * Returns the instance of this class.  Creates an instance of this class in case there isn't any.
     * @return instance of this class
     */
    public static TransactionDBMockSingleton getInstance() {

        if(db == null)
            db = new TransactionDBMockSingleton();

        return db;
    }



    /**
     * Returns a transaction according to the transaction id
     * @param id the unique ID of the transaction
     * @return an object of the class transaction
     */
    public Transaction getTransaction(long id) {
        if(id <= 0L) //Todo: change to Obtional
            throw new IllegalArgumentException("ID must be a bigger than 0");

        return transactions.get(id);
    }




    /**
     * Stores one Transaction object.
     * If the transaction has a parent transaction, the ID of the current transaction will be saved in the
     * parent transaction as a reference to its child transaction
     * If the transaction already stored, the method will return false;
     * @param transaction an object of the class Transaction
     * @return true if the transaction was successfully stored, false if it already was stored.
     * @throws java.lang.IllegalArgumentException if the transaction already exists in the storage,
     * or the parameter value is null
     */
    public boolean putTransaction(Transaction transaction) {

        if(transaction == null)
            throw new IllegalArgumentException("Transaction must not point at NULL");

        long id = transaction.getTransaction_id();


        if(transactions.containsKey(id))
            return false;


        transactions.put(id, transaction);


        if(transaction.getParent_id() != 0L)
            transactions.get(transaction.getParent_id()).setChild_id(id);

        manageTypeRecord(transaction);

        return true; //Todo: several codes

    }


    /**
     * Manage the record of all the Transaction's types.
     * This record link each type to all transactions of this type.
     * @param transaction an Object of the class Transaction
     */
    private void manageTypeRecord(Transaction transaction) {

        String type = transaction.getType();

        if(!transactionsTypes.containsKey(type)) {
            Set<Long> ids = new HashSet<>();
            transactionsTypes.put(type, ids);
        }

        transactionsTypes.get(type).add(transaction.getTransaction_id());
    }




    /**
     * Returns a Set of the unique ID's of all transaction of a certain type.
     * If the type does not exists yet, returns an empty Set.
     * @param type the type of the transactions
     * @return Set with ID of the transactions, or an empty Set in case the type does not exists yet
     */
    public Set<Long> getTransactionsByType(String type) {

        if(type == null)
            throw new IllegalArgumentException("Transaction type must not point at NULL");

        if(!transactionsTypes.containsKey(type))
            return new HashSet<Long>();

        return transactionsTypes.get(type);
    }



    /**
     * Returns the sum of all transaction's amount for a transaction and all it's child transactions.
     * If the transaction does not exists in the storage, method returns value 0.
     * @param id the unique ID of the parent transaction
     * @param sum A TransactionSum object which will eventually sum up the amounts of all transactions
     * @return sum of all transaction's amount, or 0 if the transaction does not exists in the storage
     */
    public TransactionSum getSumTransactions(Long id, TransactionSum sum) {

        Transaction transaction = transactions.get(id);

        sum.addSum(transaction.getAmount());

        Set<Long> child_ids = transaction.getChild_ids();
        if(child_ids != null)
            for(Long child_id : child_ids)
                    getSumTransactions(child_id, sum);


        return sum;
    }


}
