package com.shachor.restfull26;


import com.shachor.restfull26.transactionservice.Transaction;
import com.shachor.restfull26.transactionservice.TransactionStatus;
import com.shachor.restfull26.transactionservice.TransactionSum;
import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;


/**
 * This class tests the the methods of class TransactionController.
 *
 * @author Roi Shachor on 09.01.16.
 */
@SpringBootApplication
public class AppTest
    extends TestCase
{

    /**Transaction to be used as test data*/
    private  Transaction t10, t20, t21, t22, t30, t31, t40, t41, tExtra;

    /**List to hold the transactions used as test data*/
    private List<Transaction> transactions;

    /**Reference to an RestTemplate object. This object is used to create the HTTP Requests*/
    private RestTemplate restTemplate;


    /**The root URL of the RESTful Service */
    private String api = "http://localhost:8080/transactionservice";


    @Rule
    public  ExpectedException thrown = ExpectedException.none();


    @BeforeClass
    public void setUp() {

        restTemplate = new TestRestTemplate();

        generateTestData();
    }



    /**
     * Tests the controller method @link com.shachor.restfull26.transactionservice.putTransaction(Long).
     * Creates a Transaction, stores it with calling the method under test, and assert that
     * the response message containing the message "ok";
     */
    @Test
    public void testPutTransaction() {

        assertTrue(putTransaction(tExtra).getStatus().equals("ok"));
    }




    /**
     * Not a test method. Puts a Transaction in the storage.
     * @param transaction the transaction to store.
     * @return an Object of the class TransactionStatus
     */
    private TransactionStatus putTransaction(Transaction transaction)  {

        String url = api + "/transaction/" + transaction.getTransaction_id();

        HttpEntity<Transaction> requestEntity = new HttpEntity<>(transaction);

        ResponseEntity<TransactionStatus> response =  restTemplate.exchange(url, HttpMethod.PUT, requestEntity, TransactionStatus.class);

        return response.getBody();

    }




    /**
     * Tests the controller method @link com.shachor.restfull26.transactionservice.putTransaction(Long).
     * Tests the behaviour of the method under test in case the transaction ID does not match the ID in the URL path.
     * Expect an Object of TransactionStatus with the message "error";
     */
    @Test
    public void testPutTransactionWithUnMatchingId() {

        String url = api + "/transaction/" + t20.getTransaction_id();

        HttpEntity<Transaction> requestEntity = new HttpEntity<>(t10);

        ResponseEntity<TransactionStatus> response =  restTemplate.exchange(url, HttpMethod.PUT, requestEntity, TransactionStatus.class);

        assertTrue(response.getBody().getStatus().equals("error"));

    }



    /**
     * Tests the controller method @link com.shachor.restfull26.transactionservice.putTransaction(Long).
     * Tests the behaviour of the method under test in case the transaction already exists in the storage.
     * Expect an Object of TransactionStatus with the message "error - transaction already exists in the storage";
     */
    @Test
    public void testPutTransactionAlreadyStored() {

        assertTrue(putTransaction(t10).getStatus().equals("error - transaction already exists in the storage"));

    }




    /**
     * Tests the controller method @link com.shachor.restfull26.transactionservice.getTransactions(Long).
     * Calls for a transaction based on the unique ID of a give transaction and asserts that the returned
     * Transaction is equal to the given one.
     */
    @Test
    public void testGetTransaction() {

        String url = api + "/transaction/" + t10.getTransaction_id();

        assertTrue(restTemplate.getForObject(url , Transaction.class).equals(t10));

    }



    /**
     * Tests the controller method @link com.shachor.restfull26.transactionservice.getTransactions(Long).
     * Tests the behaviour of the method under test when a transaction with the given ID does not exits in the storage.
     * Expects a NullPointerException.
     */
    @Test

    public void testGetNotExistingTransaction() {

        long idNotYetStored = 999L;
        String url = api + "/transaction/" + idNotYetStored;

        thrown.expect(NullPointerException.class);
        restTemplate.getForObject(url, Transaction.class);
    }



    /**
     * Tests the controller method @link com.shachor.restfull26.transactionservice.getSumTransactions(Long).
     * Creates various transaction which are linked to each other, calls the a method under test and assert
     * that the sum of the amount of all transactions is correct, depending of course of which transaction
     * is used as the parent transaction for each method call.
     */
    @Test
    public void testGetTransactionsSum() {

        String url = api + "/sum/";

        double expectedT10sum = transactions.stream().mapToDouble(t -> t.getAmount()).sum();


        //When using Transaction t20 as root parent, the amount of its siblings t21, t22
        // and of parent t10 are not calculated.
        double expectedT20sum  = expectedT10sum - t10.getAmount() - t21.getAmount() - t22.getAmount();


        double sumT10 = restTemplate.getForObject((url + 10), TransactionSum.class).getSum();
        double sumT20 = restTemplate.getForObject((url + 20), TransactionSum.class).getSum();

        assertEquals(sumT10, expectedT10sum);
        assertEquals(sumT20, expectedT20sum);

    }



    /**
     * Tests the controller method @link com.shachor.restfull26.transactionservice.getSumTransactions(Long).
     * Tests the behaviour of the method when receiving an ID reference, which does not exits in the storage.
     * Expects 0 as returned value.
     */
    @Test
    public void testGetSumOfNotExistingTransaction() {

        String url = api + "/sum/";

        Long notExisting = 654L;

        assertEquals(0.0, restTemplate.getForObject((url + notExisting), TransactionSum.class).getSum());
    }



    /**
     * Tests the controller method @link com.shachor.restfull26.transactionservice.getTransactionsByType(String)
     * Creates various transaction of different types, stores them, call the a method under test and assert
     * is the returned set contains the IDs only of the transactions with the relevant type
     */
    @Test
    public void testGetTransactionsByType() {


        String url = api + "/types/" + "Tested type";


        ParameterizedTypeReference<Set<Long>> parameterType = new ParameterizedTypeReference<Set<Long>>() {};

        Set<Long> transactions = restTemplate.exchange(url, HttpMethod.GET, null, parameterType).getBody();

        //Transactions with type "Tested Type"
        Arrays.asList(t21, t31, t41).stream().forEach(t ->
                assertTrue(transactions.contains(t.getTransaction_id())));

        //Transactions with other types
        Arrays.asList(t10, t20, t22, t30, t40).stream().forEach(t ->
                assertFalse(transactions.contains(t.getTransaction_id())));

    }



    /**
     * Tests the controller method @link com.shachor.restfull26.transactionservice.getTransactionsByType(String)
     * Tests the behaviour of the method when receiving a type which not yet exits.
     * Expecting an empty Set.
     */
    @Test
    public void testGetTransactionsByNotExistingType() {

        String url = api + "/types/" + "This type does not exits";

        ParameterizedTypeReference<Set<Long>> parameterType = new ParameterizedTypeReference<Set<Long>>() {};

        Set<Long> transactionsIDs = restTemplate.exchange(url, HttpMethod.GET, null, parameterType).getBody();

        assertEquals(transactionsIDs.size(), 0);
    }



    /**
     * Generates test data.
     */
    private void generateTestData() {

        String testedType = "Tested type";

        //first level - root parent
        t10 = new Transaction(10L, 10.0 , "shopping");

        //second level
        t20 = new Transaction(20L, 20.0 , "shopping", 10L);
        t21 = new Transaction(21L, 21.0 , testedType, 10L);
        t22 = new Transaction(22L, 22.0 , "shopping", 10L);

        //third level
        t30 = new Transaction(30L, 30.0 , "shopping", 20L);
        t31 = new Transaction(31L, 31.0 , testedType, 20L);

        //fourth level
        t40 = new Transaction(40L, 40.0 , "shopping", 30L);
        t41 = new Transaction(41L, 41.0 , testedType, 30L);

        transactions = Arrays.asList(t10, t20, t21, t22, t30, t31, t40, t41);

        transactions.stream().forEach( t -> putTransaction(t));


        tExtra = new Transaction(99L, 10.0, " ");

    }
}
