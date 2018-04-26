package catchat.data.source;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

import java.io.IOException;

/**
 * Created by andrew on 4/26/18.
 */
public interface ApiInteractor<T> {
    HttpRequest getRequest() throws IOException;
    T parseResponse(HttpResponse response) throws HttpResponseException;
}
