package catchat.data.source;

import java.io.IOException;

/**
 * Created by andrew on 4/27/18.
 */
public interface ApiCommand {
    interface Listener<T> {
        void complete(T result);
    }

    void buildCommand(String authToken) throws IOException;

    void execute() throws IOException, RuntimeException;
}
