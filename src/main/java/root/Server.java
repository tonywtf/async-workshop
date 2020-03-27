package root;

import root.http.Request;
import root.http.Response;
import root.http.WSCallback;
import root.http.WSClient;
import root.os.ResponseHolder;

import java.util.concurrent.atomic.AtomicInteger;

public class Server extends Thread {
    private boolean isRunning = true;

    @Override
    public void run() {
        start = System.currentTimeMillis();
        while (isRunning) {
            try {
                Request nextRequest = Main.OS.listen();
                processNextRequest(nextRequest);
            } catch (Throwable err) {
                err.printStackTrace();
            }
        }
    }

    private void processNextRequest(Request nextRequest) throws InterruptedException {

        switch (nextRequest.url) {
            case "/":
                Request steamRequest = new Request("games", nextRequest.getUserId());
                Response steamResponse = Main.OS.sendRequestSync(steamRequest);
                Response serverResponse = new Response(200, steamResponse.getBody());
                sendResponse(nextRequest, serverResponse);
                break;
            default: {
                Response response = new Response(404, "Not found");
                sendResponse(nextRequest, response);
            }
        }

    }

    private AtomicInteger count = new AtomicInteger();
    private long start = System.currentTimeMillis();

    private void sendResponse(Request request, Response response) {
        double real = count.incrementAndGet();
        double rps = 1000 * real / (System.currentTimeMillis() - start);
        System.out.println("RPS: " + rps);
        System.out.println("USER " + request.getUserId() + " ASKED FOR " + request.url + ":. Response is: " + response.getCode() + " -- " + response.getBody());
    }

    public void stopServer() {
        isRunning = false;
    }
}
