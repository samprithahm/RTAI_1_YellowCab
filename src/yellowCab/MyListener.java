package yellowcab;

import java.util.Arrays;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Session;

public class MyListener implements MessageListener {

    Connection connection = null;
    Session session = null;
    MessageConsumer consumer1 = null;

    public MyListener(Connection connection, Session session, MessageConsumer consumer1) {
        
        this.connection = connection;
        this.session = session;
        this.consumer1 = consumer1;
    }
    static int[][] dateTime = new int[32][24];

    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                if (textMessage.getText().equals("eof")) {
//                    consumer1.close();
//                    session.close();
//                    connection.close();     
                } else {
                    int dayFreq = 0, hourFreq = 0;
                    String day = textMessage.getText().split(",")[0].split(" ")[0];
                    int dd = Integer.parseInt(day.split("-")[2]);
                    int mm = Integer.parseInt(day.split("-")[1]);
                    int yyyy = Integer.parseInt(day.split("-")[0]);
                    String time = textMessage.getText().split(",")[0].split(" ")[1];
                    int hh = Integer.parseInt(time.split(":")[0]);
                    if (dateTime[dd][hh] == 0) {
                        dateTime[dd][hh] += 1;
                        if (hh == 00) {
                            if (dd >= 02) {
                                hourFreq = dateTime[dd - 1][23];
                                System.out.println("Total Trips made at hour 23 = " + hourFreq);
                            }
                            for (int i = 0; i < 24; i++) {
                                dayFreq += dateTime[dd - 1][i];
                            }
                            System.out.println("Total Trips made on " + (dd - 1) + "-" + mm + "-" + yyyy + " : " + dayFreq);
                        } else if (hh != 00) {
                            hourFreq = dateTime[dd][hh - 1];
                            System.out.println("Total Trips made at hour " + (hh - 1) + " = " + hourFreq);
                        }
                    } else {
                        dateTime[dd][hh] += 1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
