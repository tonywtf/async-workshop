public class WaitNotifyMain {
    static String turn = "ping";
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread pingThread = new PingPongThread("ping", "pong");
        Thread pongThread = new PingPongThread("pong", "donk");
        Thread donkThread = new PingPongThread("donk", "ping");

        pingThread.start();
        pongThread.start();
        donkThread.start();

        pingThread.join();
        pongThread.join();
        donkThread.join();
    }

    public static void doTurn(PingPongThread thread) {
        synchronized (lock) {
            if (thread.type.equals(turn)) {
                System.out.println(thread.type);
                thread.countdown--;
                turn = thread.next;
                lock.notifyAll();
            } else {
                try {
                    lock.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}

class PingPongThread extends Thread {
    final String type;
    final String next;
    int countdown = 3;

    PingPongThread(String type, String next) {
        this.type = type;
        this.next = next;
        this.setName(type);
    }

    @Override
    public void run() {
        while (countdown > 0) {
            WaitNotifyMain.doTurn(this);
        }
    }
}