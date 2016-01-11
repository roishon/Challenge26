package com.shachor.restfull26.transactionservice;


import java.util.HashSet;
import java.util.Set;

/**
 * Class transaction represent one Money Transaction, and maintains information about the amount, type and unique ID
 * of this transaction, as well as the unique ID of possible parent and children transactions, to which the transaction
 * is linked (each transaction can be linked to one parent transaction and various children transactions).
 *
 * @author Roi Shachor on 09.01.16.
 */

public class Transaction {

    /**The unique ID of this transaction*/
    private long transaction_id;

    /**Represent the type of this transaction*/
    private String type;

    /**The amount of this transaction*/
    private double amount;

    /**The unique ID of a parent transaction of this transaction*/
    private long parent_id;

    /**The unique ID of a child transaction of this transaction*/
    private Set<Long> child_ids;

    /**
     * default constructor, used by Jackson library to create JSON objects
     */
    public Transaction() {}


    /**
     * Creates an object of this class and initiates the properties transaction_id, amount, type and parent_id.
     * @param transaction_id - the unique ID of this transaction
     * @param amount - the amount of this transaction
     * @param type - type of this transaction
     * @param parent_id - reference to the unique ID of a parent transaction.
     *                  If this transaction does not have a parent transaction,
     *                  parent_id should have the value 0, or the constructor with signature
     *                  Transaction(long, double , String) should be used
     */
    public Transaction(long transaction_id, double amount, String type, long parent_id) {


        if(transaction_id <= 0)
            throw new IllegalArgumentException("transaction_id must have value bigger than 0");

        if(parent_id < 0L)
            throw new IllegalArgumentException("parent id can not be a negative number");

        if(parent_id >= transaction_id)
            throw new IllegalArgumentException("parent_id must have a bigger value then the transaction_id of the same object");


        if(type == null || type.length() == 0)
            throw new IllegalArgumentException("type must contain a valid String value");


        this.transaction_id = transaction_id;
        this.amount = amount;
        this.type = type;
        this.parent_id = parent_id;
    }

    /**
     * Creates an object of this class as the constructor above, with the difference, that this
     * constructor does not have a parameter for parent_id, which means that this transaction is not linked
     * to any parent transaction
     * @param transaction_id - the unique ID of this transaction
     * @param amount - the amount of this transaction
     * @param type - type of this transaction
     */
    public Transaction(long transaction_id, double amount, String type) {
       this(transaction_id, amount, type, 0);
    }


    /**
     * Returns the unique ID of this transaction
     * @return ID of this transaction
     */
    public long getTransaction_id() {
        return transaction_id;
    }

    /**
     * Returns the amount of this transaction
     * @return amount of this transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns a String representation of the type of this transaction
     * @return Type of this transaction
     */
    public String getType() {
        return type;
    }


    /**
     * Returns the unique ID of the parent transaction of this transaction.
     * If there isn't any parent transaction, returns 0.
     * @return id of the parent transaction
     */
    public long getParent_id() {
        return parent_id;
    }


    /**
     * Returns the a Set with all unique IDs of the children transactions of this transaction.
     * If there isn't any child transaction, returns 0.
     * @return ID of this transaction
     */
    public Set<Long> getChild_ids() {
        return child_ids;
    }


    /**
     * Sets the child_id property, which is a reference to the unique id of one of the children transactions
     * of this transaction
     * @param child_id - the unique ID of the child transaction
     */
    public void setChild_id(long child_id) {
        if(child_ids == null)
            child_ids = new HashSet<>();

        child_ids.add(child_id);
    }


    /**
     * Uses the unique ID of a transaction to override Object class method equals()
     * see (@link java.lang.Object)
     * @param o  the reference object with which to compare.
     * @return true if both objects have the same unique ID
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (transaction_id != that.transaction_id) return false;

        return true;
    }


    /**
     * Uses the unique ID of a transaction to override Object class method hashCode()
     * see (@link java.lang.Object)
     * @return a hash code value for this object based on it's unique ID
     */
    @Override
    public int hashCode() {
        return (int) (transaction_id ^ (transaction_id >>> 32));
    }

}
