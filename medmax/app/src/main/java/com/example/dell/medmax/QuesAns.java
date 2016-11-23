package com.example.dell.medmax;

import java.util.ArrayList;

/**
 * Created by Chandan Suri on 11/23/2016.
 */
public class QuesAns {
    public static class Faq{
        String ques;
        String  ans;

        public Faq(String ques, String ans) {
            this.ques = ques;
            this.ans = ans;
        }
    }

    public static ArrayList<Faq> getQ (){
        ArrayList<Faq> faqs=new ArrayList<>(10);

        faqs.add(new Faq("What does this app do? ", "Medmax provides the user with the ability to order in case the user is" +
                " a Medically authorized Shopkeeper from a medically authorized Vendor, the vendor has all the inventory " +
                "updated on the application with all the details to contact him/her and shopkeeper also has all the " +
                "information on the application, the shopkeeper can easily place an order and also check its status," +
                " as changed by the vendor. It's basically a shopkeeper vendor cart system for Medical representatives."));

        faqs.add(new Faq("How to use the app? ", "For a Shopkeeper, it has functionality of placing an order, you can go to " +
                "the \"Place Order\" page just by clicking the button on right bottom corner or you can navigate to that page " +
                "through navigation drawer, then everything is simple, just click and select the items you want first by selecting" +
                " the vendor, then after selection of all the items an invoice is generated, before confirmation of the order.\n" +
                "For a Vendor, it has the functionality to update all the inventory on Medmax database and also can see all the " +
                "orders placed and thus can change the status of an order just by clicking on an order."));

        faqs.add(new Faq("Can I update the recent order? ", " No, you can't update the order, new order has to be placed in case of any changes. " +
                "You can see the recent Orders on the main page. "));

        faqs.add(new Faq("Mode of Payment?", "The Mode of payment provided is only Cash On Delivery, you have to call the person and "+
                    "tell all the details, otherwise as the vendor dispatches the status is shown to the shopkeeper as well"));


        return faqs;


    }
}
