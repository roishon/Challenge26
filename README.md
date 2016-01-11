# Challenge26

A few notes regarding the choice of Implementation for the storage 
(class TransactionDBMockSingleton) in respect of performance and asymptotic behaviour.


Although more than one transaction can be linked to a parent transaction, which logically resemble a Tree structure, I chose to store the Transactions in a ConcurrentHashMap and not in a Tree structure.

The big O notion of retrieving a Tree in order to insert or look for a Transaction is O(log2 of N).
The performance will drop by very big amount of stored transactions.
The HashMap has big O notion of O(1) for those actions. 

IThee link between transactions is represented through fields of the Transaction class:
Long id_parent, HashMap<Long> id_children


This data structure is also very helpful for the request  					            
GET transactionservice/sum/{id_root_Transaction}.
First the root Transaction is found with O(1). Then only the relevant Transactions will be retrieved through the field reference to the next child Trasaction . There is no retrieving of irrelevant Transactions (= no retrieving parent Transactions or sibling Transaction of this root Transaction). 

regarding GET transactionservice/types/{type}
In order to avoid long retrieving process of all Transactions => O(N), 
class TransactionDBMockSingleton holds a HashSet<String Type , HashSet<Long ID>> to provide inserting and fetching of information with O(1).

