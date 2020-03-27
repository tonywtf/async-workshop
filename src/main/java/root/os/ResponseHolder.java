package root.os;

import root.http.Response;

public class ResponseHolder {
    final int id;
    final Response response;
    long timeReady;

    public int getId() {
        return id;
    }

    public Response getResponse() {
        return response;
    }

    public ResponseHolder(int id, Response response) {
        this.id = id;
        this.response = response;
    }
}
