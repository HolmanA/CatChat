package catchat.data.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ServerSocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * OAuthService capable of obtaining an API token to GroupMe's chats servers through OAuth2.0 Implicit Grant
 * authorization.
 */
public class GroupMeOAuthService implements OAuthService {
    private static final Logger log = LoggerFactory.getLogger(GroupMeOAuthService.class);

    // Resource paths
    private static final String HTML_ROOT = "/data/authentication/web/html/";
    private static final String SUCCESS_HTML = HTML_ROOT + "authentication_successful.html";
    private static final String PROPERTIES_XML = "/data/authentication/config/auth_properties.xml";


    // Property Keys
    private static final String PROP_AUTH_URL = "base_auth_url";
    private static final String PROP_CLIENT_ID = "client_id";
    private static final String PROP_CALLBACK_PORT = "callback_port";

    private String authURL;
    private int callbackPort;
    private Socket remoteAuthSocket;
    private ServerSocket localCallbackSocket;
    private AuthListener authListener;
    private String authToken;

    public GroupMeOAuthService(OAuthService.AuthListener authListener) {
        this.authListener = authListener;
        try {
            loadProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAuthURL() {
        return authURL;
    }

    @Override
    public String getAPIToken() {
        return authToken;
    }

    @Override
    public void tokenRejected() {
        log.error("Authentication token rejected by GroupMe API {}", authToken);
        authListener.onFailure();
    }

    @Override
    public void run() {
        try {
            authenticate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void authenticate() throws Exception {
        log.info("Authenticating");
        initializeCallbackSocket();
        acceptCallbackConnection();
        String requestHeader = parseRequestHeader();
        sendResponseMessage();
        authToken = parseTokenFromHeader(requestHeader);
        log.info("Authentication Successful");
        authListener.onSuccess();
    }

    /**
     * Loads authorization connection properties from a java properties file
     */
    private void loadProperties() throws Exception {
        log.debug("Parsing {}", PROPERTIES_XML);
        try {
            Properties props = new Properties();
            InputStream in = getClass().getResourceAsStream(PROPERTIES_XML);
            props.loadFromXML(in);
            String authURLProp = props.getProperty(PROP_AUTH_URL);
            String clientIdProp = props.getProperty(PROP_CLIENT_ID);
            String callbackPortProp = props.getProperty(PROP_CALLBACK_PORT);
            authURL = authURLProp + clientIdProp;
            callbackPort = Integer.parseInt(callbackPortProp);
            in.close();

            log.trace("{} = {}", PROP_AUTH_URL, authURLProp);
            log.trace("{} = {}", PROP_CLIENT_ID, clientIdProp);
            log.trace("{} = {}", PROP_CALLBACK_PORT, callbackPortProp);
        } catch (Exception e) {
            log.error("Unable to load authentication properties");
            authListener.onFailure();
            throw e;
        }
    }

    private void initializeCallbackSocket() throws IOException {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try {
            localCallbackSocket = factory.createServerSocket(callbackPort);

            log.trace("Server running on 127.0.0.1:{}", callbackPort);
        } catch (IOException e) {
            log.error("Unable to initialize authentication callback server");
            authListener.onFailure();
            throw e;
        }
    }

    /**
     * Attempts to accept an incoming connection on the local authorization callback socket
     */
    private void acceptCallbackConnection() throws IOException {
        log.debug("Listening for incoming connections");
        try {
            remoteAuthSocket = localCallbackSocket.accept();

            log.debug("Accepted connection on Server Socket");
            log.trace("Local Address {}", remoteAuthSocket.getLocalAddress().getHostAddress());
            log.trace("Remote Address {}", remoteAuthSocket.getInetAddress().getHostAddress());
        } catch (IOException e) {
            log.error("Unable to accept authentication callback connection");
            authListener.onFailure();
            throw e;
        }
    }

    /**
     * Attempts to read an incoming http header containing the authentication token from the authentication connection
     *
     * @return String http header for the http request
     */
    private String parseRequestHeader() throws IOException {
        log.debug("Parsing HTTP request header");
        try {
            int length;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream in = remoteAuthSocket.getInputStream();

            if ((length = in.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            String header = baos.toString("UTF-8");
            baos.close();

            log.trace("Header: {}", header);
            return header;
        } catch (IOException e) {
            log.error("Unable to read authentication callback http header");
            authListener.onFailure();
            throw e;
        }
    }

    /**
     * Searches the provided header for the API access token
     *
     * @param header String http header obtained from the authentication callback connection
     * @return String API authorization token
     */
    private String parseTokenFromHeader(String header) throws Exception {
        String tokenLabel = "access_token=";
        int tokenLabelStartIndex = header.indexOf(tokenLabel);
        if (tokenLabelStartIndex != -1) {
            int tokenStartIndex = tokenLabelStartIndex + tokenLabel.length();
            int tokenEndIndex = header.indexOf(' ', tokenStartIndex);
            return header.substring(tokenStartIndex, tokenEndIndex);
        } else {
            log.error("Unable to locate access token in authentication callback http header");
            authListener.onFailure();
            throw new Exception("No access token in http header");
        }
    }

    /**
     * Sends back an web page containing some embedded javascript commanding the browser to close the page.
     * If the browser does not support automatically closing web pages through javascript, a message
     * instructing the user to close the page will be shown.
     */
    private void sendResponseMessage() {
        log.debug("Sending response message");
        try {
            InputStream inStream = getClass().getResourceAsStream(SUCCESS_HTML);
            InputStreamReader in = new InputStreamReader(inStream);
            PrintWriter out = new PrintWriter(remoteAuthSocket.getOutputStream());

            String header = "HTTP/1.1 200 OK\r\n";
            header += "Content-Type: text/html\r\n";
            header += "\r\n";

            // Send http response header
            out.print(header);

            // Send html file
            int length;
            char[] buffer = new char[1024];
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            out.flush();
            out.close();
            inStream.close();
            in.close();
            remoteAuthSocket.close();
            localCallbackSocket.close();
        } catch (IOException e) {
            log.error("Unable to send response message");
            e.printStackTrace();
        }
    }
}