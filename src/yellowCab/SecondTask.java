package yellowCab;
import java.util.Timer;

public class SecondTask implements Runnable {

	public void run() {
		
		try {
			
			Frequency frequency = new Frequency();
	        //running timer task as daemon thread
	        Timer timer = new Timer(true);
	        timer.scheduleAtFixedRate(frequency, 0, 10*1000);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}