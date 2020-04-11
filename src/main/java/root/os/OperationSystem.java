package root.os;

import root.http.Request;
import root.http.Response;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OperationSystem {
    private final List<ResponseHolder> responses = new ArrayList<>();
    private final Random random = new Random();
    private int currentId = 0;
    private long nextRequestTime = System.currentTimeMillis();

    public ResponseHolder epoll() throws InterruptedException {
        while (true) {
            synchronized (this) {
                long now = System.currentTimeMillis();
                for (ResponseHolder holder : responses) {
                    if (holder.timeReady <= now) {
                        responses.remove(holder);
                        return holder;
                    }
                }
            }
            Thread.sleep(10);
        }
    }

    public int sendRequest(Request request) {
        synchronized (this) {
            long now = System.currentTimeMillis();
            Response response = generateResponse(request);
            ResponseHolder holder = new ResponseHolder(currentId, response);
            holder.timeReady = now + 50 + random.nextInt(25);
            responses.add(holder);
            return currentId++;
        }
    }

    public Response sendRequestSync(Request request) throws InterruptedException {
        Thread.sleep(50 + random.nextInt(2500));
        return generateResponse(request);
    }

    public Request listen() throws InterruptedException {
        while (true) {
            long now = System.currentTimeMillis();
            if (nextRequestTime <= now) {
                nextRequestTime = now + random.nextInt(10);
                return new Request("/");
            }
            Thread.sleep(10);
        }
    }

    private Response generateResponse(Request request) {
        switch (request.url) {
            case "games":
                return new Response(200, "USER " + request.getUserId() + " HAS GAMES (game1, game2)");
            case "achievements":
                return new Response(200, "achievements");
            case "schema":
                return new Response(200, "schema");
            default:
                return new Response(404, "url not found");
        }
    }

    private AtomicInteger count = new AtomicInteger();
    private long start = System.currentTimeMillis();

    public void sendResponse(Response response) {
        double real = count.incrementAndGet();
        double rps = 1000 * real / (System.currentTimeMillis() - start);
        System.out.println("RPS: " + rps);
        System.out.println(response.getCode() + " -- " + response.getBody());
    }
}
