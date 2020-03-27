package root.http;

public class Response {
    final int code;
    final String body;

    public String getBody() {
        return body;
    }

    public int getCode() {
        return code;
    }

    public Response(int code, String body) {
        this.code = code;
        this.body = body;
    }
}
