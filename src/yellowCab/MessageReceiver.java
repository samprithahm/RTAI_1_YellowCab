package yellowcab;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageReceiver implements Runnable {

    public void run() {

        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            Connection connection = connectionFactory.createConnection();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination dest = session.createTopic("myTopic");
            
            // Consumer1 subscribes to customerTopic
            MessageConsumer consumer1 = session.createConsumer(dest);
            consumer1.setMessageListener(new MyListener(connection, session, consumer1));
             
            // Consumer2 subscribes to customerTopic
            MessageConsumer consumer2 = session.createConsumer(dest);
            consumer2.setMessageListener(new LocListener(connection, session, consumer2));
             
            connection.start();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
