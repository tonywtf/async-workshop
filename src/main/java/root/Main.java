package root;
import root.os.OperationSystem;

public class Main {

    public static OperationSystem OS = new OperationSystem();

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        server.start();
        Thread.sleep(1000000000);
        server.stopServer();
    }
}
