package yellowcab;

public class ActiveHW {

    public static void main(String[] args) throws Exception {
        thread(new MessageSender(), false);
        thread(new MessageReceiver(), false);
        Thread.sleep(60000);
        thread(new SecondTask(1, 12, 17), false);
        thread(new ThirdTask(), false);
    }

    public static void thread(Runnable runnable, boolean daemon) {

        Thread brokerThread = new Thread(runnable);
        //brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
}