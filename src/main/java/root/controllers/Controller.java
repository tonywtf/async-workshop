package root.controllers;

import root.async.Future;
import root.http.Request;
import root.http.Response;

public interface Controller {
    boolean canProcess(Request request);

    Future<Response> process(Request request);
}
