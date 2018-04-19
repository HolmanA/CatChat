package catchat.data.source;

/**
 * Created by andrew on 3/24/18.
 */
public interface BaseCallback {
    void dataNotAvailable();
    void notAuthorized();
    void unknownResponseCode(String code);
}
