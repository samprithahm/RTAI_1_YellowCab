package yellowCab;
public class ActiveHW {
	
	public static void main (String[] args) throws Exception
	{
		thread(new MessageSender(), false);
        thread(new MessageReceiver(), false); 
        //thread(new SecondTask(), false);
	}
	
	public static void thread(Runnable runnable, boolean daemon) {
		
		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
		
	}
}