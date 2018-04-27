package catchat.data.source;

import java.io.IOException;

/**
 * Created by andrew on 4/26/18.
 */
public interface ApiInteractor<T> {
    void disconnect();
    int getResponseCode() throws IOException;
    String getResponseMessage() throws IOException;
    T getContent() throws IOException;
}
