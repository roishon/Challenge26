package com.shachor.restfull26.transactionservice;

/**
 * Class TransactionStatus represents the String representation of a transaction's status.
 * Object of this class is sent with the HTTP response to notify the client
 *
 * @author  Roi Shachor on 09.01.16.
 */
public class TransactionStatus {

    /**The status message of the transaction*/
    private String status;


    /**
     * default constructor, used by Jackson library to create JSON objects
     */
    public TransactionStatus() {}


    /**
     * Creates a TransactionStatus object and sets the status property
     * @param status the status of the transaction
     */
    public TransactionStatus(String status) {
        this.status = status;
    }


    /**
     * Returns the String representation of the status
     * @return the status
     */
    public String getStatus() {
        return status;
    }


    /**
     * Sets the value of the status
     * Although its possible to set this property with the constructor,
     * this method is needed for Jackson library to instantiate a JSON object
     * @param status the status of the transaction
     */
    public void setStatus(String status) {
        this.status = status;
    }




}
