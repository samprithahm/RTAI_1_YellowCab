package yellowCab;

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
			
			MessageConsumer consumer = session.createConsumer(dest);
			
			MyListener myListener = new MyListener();
			
			consumer.setMessageListener(myListener);
			//myListener.getFreq();
			connection.start();
			
			//consumer.close();
			//session.close();
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
}