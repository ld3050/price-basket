PriceBasket 
===========

Description
-----------

PriceBasket takes items to be purchased as command line arguments, then sums their total cost according to their pre-configured prices. It then calculates any discounts applied to the purchased items and prints out a the subtotal, discounts and totals. 
 
eg:
<pre>
Subtotal: £5.40
Bread 50% off: -40p
Apples 10% off: -20p
Total price: £4.80
</pre>
 
The goods that can be purchased, together with their normal prices are: 

- Soup – 65p per tin 
- Bread – 80p per loaf 
- Milk – £1.30 per bottle
- Apples – £1.00 per bag

Current special offers: 

- Apples have a 10% discount off their normal price this week 
- Buy 2 tins of soup and get a loaf of bread for half price 

The program should accept a list of items in the basket and output the subtotal, the special offer discounts and the final price. Input should be via the command line in the form <code>PriceBasket item1 item2 item3 … </code>

For example: 
<pre>
PriceBasket Apple Milk Bread 
</pre>

Output should be to the console, for example: 
<pre>
Subtotal: £3.10 
Apples 10% off: -10p 
Total: £3.00 
</pre>
If no special offers are applicable the code should output: 
<pre>
Subtotal: £1.30 
(No offers available) 
Total price: £1.30
</pre>

How to Run
----------

You must have Maven and Java installed and configured. Unzip the PriceBasket project and build the project using Maven:
<pre>
mvn clean package
</pre>
You can now run the application:
<pre>
cd target/
java -jar PriceBasket-0.1.jar Apple Milk Bread
</pre>

Implementation Details
----------------------

The project defines the Item catalogue in an XML file: <code>src/main/resources/META-INF/basket-config.xml</code>. This file describes the items currently available along with their price. It also describes any special offers currently in effect.

The program relies on Spring's parsing capabilities to turn basket-config.xml into Java objects (Items and Offers). Spring is also used to inject a number of Services used throughout the application:

- ItemService - for looking up registered Items and Offers
- BasketService - for calculating the totals of a Basket
- BasketPrinterService - for outputting the Basket totals
- MsgService - for localising messages output to the user

The interfaces on these services are kept simple so that they can be replaced easily by different implementations if required.

The Offer interface is also designed so that different kinds of offers can be added to the system easily (eg. Fixed price offers, Buy one get one free, etc.).



Assumptions
-----------

- Items added by the user on the command-line are case-insensitive. ie. "Apple" and "apple" will resolve to the same item. This also means that the editor of basket-config.xml must not specify items with the same name and conflicting case.

- Attempts to add invalid Items to the Basket are ignored.

- Expiry dates are inclusive. If the expiry date is today, the offer will still be applicable.

- The Pound symbol (£) is sensitive to encoding issues. The application is currently set to build and run using CP850 which is the default command line encoding for Windows environments. 
