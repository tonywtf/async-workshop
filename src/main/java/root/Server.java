package root;

import root.http.Request;
import root.http.Response;
import root.http.WSClient;

import static root.Main.OS;

public class Server extends Thread {
    private boolean isRunning = true;

    private WSClient steamClient = new WSClient();

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

    private void processNextRequest(Request nextRequest) {
        switch (nextRequest.url) {
            case "/":
                Request gamesRequest = new Request("games", nextRequest.getUserId());
                Request achievementRequest = new Request("achievements", nextRequest.getUserId());
                steamClient.sendRequest(gamesRequest, gamesResponse -> {
                    steamClient.sendRequest(achievementRequest, achievementResponse -> {
                        Response serverResponse = new Response(200, gamesResponse.getBody() + achievementResponse.getBody());
                        OS.sendResponse(nextRequest, serverResponse);
                    });
                });
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
