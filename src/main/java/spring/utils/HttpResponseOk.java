package spring.utils;

/**
 * Created by Djowood on 26/10/2016.
 */
public class HttpResponseOk<T> {
    public final T data;
    public final boolean status = true;

    public HttpResponseOk(T dt) {
        data = dt;
    }
}
