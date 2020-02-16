package yellowcab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageSender implements Runnable {

    public void run() {

        try {

            // Administrative object
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Administrative object
            Destination dest = session.createTopic("myTopic");

            MessageProducer producer = session.createProducer(dest);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            String fileName = "F:\\NUIG AI Semester 2\\Research Topics in AI\\Assignment 1\\SortedTaxiData.csv";
            try (Stream<String> data = Files.lines(Paths.get(fileName)).filter(s -> !s.isEmpty())) {
                data.limit(300000).skip(1).map(s -> new AbstractMap.SimpleEntry<>(s, s.split(",")))
                        .filter(s -> (s.getValue()[1].split(" ")[0].split("-")[1].equals("01")
                        && s.getValue()[1].split(" ")[0].split("-")[0].equals("2018")))
                        .forEach(s -> {
                            try {
                                //messageArray.add(s.getValue()[1] + "," + s.getValue()[2] + "," + s.getValue()[7]);
                                Message message = null;
                                message = session.createTextMessage(s.getValue()[1] + "," + s.getValue()[2] + "," + s.getValue()[7]);
                                producer.send(message);
                            } catch (Exception e) {
                                Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, e);
                            }
                        });
            }
            producer.send(session.createTextMessage("eof"));
            connection.close();
        } 
        
        catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
    }
}
