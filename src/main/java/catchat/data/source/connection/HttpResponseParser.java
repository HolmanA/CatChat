package catchat.data.source.connection;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

/**
 * Created by andrew on 4/21/18.
 */
public interface HttpResponseParser<T> {
    T parseResponse(HttpResponse response) throws HttpResponseException;
}
