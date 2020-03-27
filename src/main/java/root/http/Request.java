package root.http;

import java.util.Random;

public class Request {
    private static Random random = new Random();

    public final String url;
    private final int userId;

    public int getUserId() {
        return userId;
    }

    public Request(String url) {
        this.url = url;
        this.userId = random.nextInt(1000000);
    }

    public Request(String url, int userId) {
        this.url = url;
        this.userId = userId;
    }
}
