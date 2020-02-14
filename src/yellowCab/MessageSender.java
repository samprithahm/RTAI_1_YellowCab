package yellowCab;

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

public class MessageSender implements Runnable{

	int dateTime[][] = new int[31][24];
	List<String> messageArray = new ArrayList<String>();
	
	public int[][] getDateTime() {
		return dateTime;
	}
	public void setDateTime(int[][] dateTime) {
		this.dateTime = dateTime;
	}
	
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

			String fileName = "C:\\Users\\sampr\\Downloads\\yellow_tripdata_2018-01.csv";
			try (Stream<String> data = Files.lines(Paths.get(fileName)).filter(s -> !s.isEmpty())) {
				data.limit(1000).skip(1).map(s -> new AbstractMap.SimpleEntry<>(s, s.split(",")))
				.filter(s -> (s.getValue()[1].split(" ")[0].split("-")[1].equals("01") &&
						s.getValue()[1].split(" ")[0].split("-")[0].equals("2018")))
				.forEach(s -> {
					try {
						int day = Integer.parseInt(s.getValue()[1].split(" ")[0].split("-")[2]);
						
						int time = Integer.parseInt(s.getValue()[1].split(" ")[1].split(":")[0]);
						dateTime[day][time] += 1;
						messageArray.add(s.getValue()[1] + "," + s.getValue()[2] + "," + s.getValue()[7]);
					} catch (Exception e) {
						Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, e);
					}
				});
			} 
			
			try
			{
				messageArray.stream().forEach(s -> 
				{
					int freq = 0;
					Message message = null;
					try {
						// TODO generate proper message
						message = session
								.createTextMessage();

						producer.send(message);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				});
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			connection.close();
		}
		catch (JMSException e) {
			System.out.println("caught " + e);
			e.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
}