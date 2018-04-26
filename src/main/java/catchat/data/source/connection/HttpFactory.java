package catchat.data.source.connection;

import com.google.api.client.http.HttpRequest;

import java.io.IOException;

/**
 * Created by andrew on 4/21/18.
 */
public interface HttpFactory<T> {
    HttpRequest getRequest() throws IOException;
    HttpResponseParser<T> getResponseParser();
}
