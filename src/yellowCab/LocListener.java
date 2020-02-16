/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yellowcab;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.json.JSONObject;

/**
 *
 * @author Saurabh
 */
public class LocListener implements MessageListener {

    //Multimap<Integer, Multimap<Integer, Integer>> locationID = ArrayListMultimap.create();
    static Multimap<String, Integer> locationID = ArrayListMultimap.create();

    Connection connection = null;
    Session session = null;
    MessageConsumer consumer2 = null;

    public LocListener(Connection connection, Session session, MessageConsumer consumer2) {

        this.connection = connection;
        this.session = session;
        this.consumer2 = consumer2;
    }

    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                if (textMessage.getText().equals("eof")) {
                    consumer2.close();
                    session.close();
                    connection.close();

//                    // get all the set of keys
//                    Set<String> keys = locationID.keySet();
//                    // iterate through the key set and display key and values
//                    for (String key : keys) {
//                        System.out.println("Day and Time= " + key);
//
//                        System.out.println("Location ID = " + locationID.get(key));
//                    }

                } else {

                    int day = Integer.parseInt(textMessage.getText().split(",")[0].split(" ")[0].split("-")[2]);
                    int time = Integer.parseInt(textMessage.getText().split(",")[0].split(" ")[1].split(":")[0]);
                    int locID = Integer.parseInt(textMessage.getText().split(",")[2]);

                    String key = day + "," + time;

                    locationID.put(key, locID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
