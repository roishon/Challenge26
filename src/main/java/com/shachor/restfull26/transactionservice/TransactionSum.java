package com.shachor.restfull26.transactionservice;

/**
 * Class TransactionSum serves as an container to hold the sum of the amounts of several transactions
 * Object of this class is sent with the HTTP response to notify the client
 * @author  Roi Shachor on 09.01.16.
 */
public class TransactionSum {

    /**represents the transaction's amount*/
    private double sum;


    /**
     * default constructor, used by Jackson library to create JSON objects
     */
    public TransactionSum() {}


    /**
     * Creates a TransactionSum object and setting the value of the sum
     * @param sum a representation of the transaction's amount with a 'double' value
     */
    public TransactionSum(double sum) {
        this.sum = sum;
    }


    /**
     * Returns the String representation of the status
     * @return the status
     */
    public double getSum() {
        return sum;
    }


    /**
     * Sets the value of the transaction's amount
     * Although its possible to set this property with the constructor,
     * this method is needed for Jackson library to instantiate a JSON object
     * @param sum double representation of the transaction's amount
     */
    public void setSum(double sum) {
        this.sum = sum;
    }


    /**
     * Adds the amount given as parameter to the current amount.
     * @param amount the amount to add
     */
    public void addSum(double amount) {
        this.sum += amount;
    }

}
