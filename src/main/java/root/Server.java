package root;

import root.http.Request;
import root.http.Response;

import static root.Main.OS;

public class Server extends Thread {
    private boolean isRunning = true;

    @Override
    public void run() {
        while (isRunning) {
            try {
                Request nextRequest = OS.listen();
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
                Response steamResponse = OS.sendRequestSync(steamRequest);
                Response serverResponse = new Response(200, steamResponse.getBody());
                OS.sendResponse(nextRequest, serverResponse);
                break;
            default: {
                Response response = new Response(404, "Not found");
                OS.sendResponse(nextRequest, response);
            }
        }

    }


    public void stopServer() {
        isRunning = false;
    }
}
