package yellowCab;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyListener implements MessageListener {

	public void onMessage(Message message) {

		try {
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;

				String day = textMessage.getText().split(",")[0];
				String time = textMessage.getText().split(",")[1];
				String freq = textMessage.getText().split(",")[2];
				System.out.println("Trip frequency on " + day + " , time " + time + " : " + freq);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
