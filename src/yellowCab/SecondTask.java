package yellowcab;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class SecondTask implements Runnable{

    static int date;
    int startTime;
    int endTime;
    static List<Integer> tripFreq = new ArrayList<>();
    static List<Integer> timeFrame = new ArrayList<>();
    
    public SecondTask(int date, int startTime, int endTime)
    {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public void run() {
        
        try {
            for( int i = startTime; i<= endTime; i++) {
                timeFrame.add(i);
                tripFreq.add(MyListener.dateTime[date][i]);
                System.out.println("Frequency of trip at time " +i+ " = " +MyListener.dateTime[date][i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
