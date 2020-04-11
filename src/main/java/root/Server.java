package root;

import root.async.Future;
import root.controllers.Controller;
import root.http.Request;
import root.http.Response;
import root.http.WSClient;

import java.util.ArrayList;
import java.util.List;

import static root.Main.OS;

public class Server {
    private boolean isRunning = true;

    private List<Controller> controllers = new ArrayList<>();
    private List<Future<Response>> responses = new ArrayList<>();

    public void addController(Controller controller) {
        controllers.add(controller);
    }

    private Thread listenThread = new Thread(this::listen);
    private Thread responseThread = new Thread(this::response);

    void start() {
        listenThread.setName("LISTEN");
        listenThread.start();
        responseThread.setName("RESPONSE");
        responseThread.start();
    }

    public void listen() {
        while (isRunning) {
            try {
                Request nextRequest = OS.listen();
                processNextRequest(nextRequest);
            } catch (Throwable err) {
                err.printStackTrace();
            }
        }
    }

    private void response() {
        while (isRunning) {
            synchronized (this) {
                List<Future<Response>> toDelete = new ArrayList<>();
                for (Future<Response> responseFuture : responses) {
                    if (responseFuture.isComplete()) {
                        toDelete.add(responseFuture);
                        OS.sendResponse(responseFuture.get());
                    }
                }
                responses.removeAll(toDelete);
            }
        }
    }

    private void processNextRequest(Request nextRequest) {
        for (Controller c: controllers) {
            if (c.canProcess(nextRequest)) {
                Future<Response> future = c.process(nextRequest);
                synchronized (this) {
                    responses.add(future);
                }
                return;
            }
        }
        Response response = new Response(404, "Not found");
        OS.sendResponse(response);
    }


    public void stopServer() {
        isRunning = false;
    }
}
