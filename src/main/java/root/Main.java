package root;
import root.async.FutureImpl;
import root.controllers.GamesController;
import root.os.OperationSystem;

public class Main {

    public static OperationSystem OS = new OperationSystem();

    public static void main(String[] args) throws InterruptedException {
        FutureImpl.MapThread.setName("FUTURE_MAP");
        FutureImpl.MapThread.start();
        Server server = new Server();
        server.addController(new GamesController());
        server.start();
        Thread.sleep(1000000000);
        server.stopServer();
    }
}
