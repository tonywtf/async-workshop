package root.controllers;

import root.async.Future;
import root.http.Request;
import root.http.Response;
import root.http.WSClient;

import static root.Main.OS;

public class GamesController implements Controller {
    private WSClient steamClient = new WSClient();

    @Override
    public boolean canProcess(Request request) {
        return request.url.equals("/");
    }

    @Override
    public Future<Response> process(Request request) {
        Request gamesRequest = new Request("games", request.getUserId());
        Request achievementRequest = new Request("achievements", request.getUserId());

        Future<Response> gamesPromise = steamClient.sendRequest(gamesRequest);
        return gamesPromise.map((gamesResponse) -> {
            return "USER " + request.getUserId() + " ASKED FOR " + request.url + ":. Response is: " + gamesResponse.getBody();
        }).map((responseString) -> {
            return new Response(200, responseString);
        });
    }
}
