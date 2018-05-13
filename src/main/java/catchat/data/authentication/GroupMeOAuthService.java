package catchat.data.authentication;

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
 *
 * @author Andrew Holman
 * @version %I%, %G%
 * @since 1.0
 */
public class GroupMeOAuthService implements OAuthService {
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

    /**
     * Constructor
     * @param authListener Object that will receive method calls depending on the state of the authentication process
     *                     (Success, Failure, etc).
     */
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

    /**
     * Called by outside classes if the authentication token provided by this class is invalid
     */
    @Override
    public void tokenRejected() {
        authListener.onFailure("Error: Authentication token rejected by GroupMe API");
    }

    /**
     * @see Runnable
     */
    @Override
    public void run() {
        try {
            authenticate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes the authentication process
     * @throws Exception If an error occurs during the authentication process
     */
    private void authenticate() throws Exception {
        initializeCallbackSocket();
        acceptCallbackConnection();
        String requestHeader = parseRequestHeader();
        sendResponseMessage();
        authToken = parseTokenFromHeader(requestHeader);
        authListener.onSuccess();
    }

    /**
     * Loads authorization connection properties from a java properties file
     * @throws Exception If an error occurs while opening or parsing properties file
     */
    private void loadProperties() throws Exception {
        try {
            Properties props = new Properties();
            InputStream in = getClass().getResourceAsStream(PROPERTIES_XML);
            props.loadFromXML(in);
            authURL = props.getProperty(PROP_AUTH_URL) + props.getProperty(PROP_CLIENT_ID);
            callbackPort = Integer.parseInt(props.getProperty(PROP_CALLBACK_PORT));
            in.close();
        } catch (Exception e) {
            String message = "Error: Unable to load authentication properties";
            message += e.getMessage();
            authListener.onFailure(message);
            System.err.println(message);
            throw e;
        }
    }

    /**
     * Attempts to initialize a server socket on the specified port
     * @throws IOException If error occurs while creating server socket
     */
    private void initializeCallbackSocket() throws IOException {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try {
            localCallbackSocket = factory.createServerSocket(callbackPort);
            System.out.println("Server Running on 127.0.0.1:" + callbackPort);
        } catch (IOException e) {
            String message = "Error: Unable to initialize authentication callback server";
            message += e.getMessage();
            authListener.onFailure(message);
            System.err.println(message);
            throw e;
        }
    }

    /**
     * Attempts to accept an incoming connection on the local authorization callback socket
     * @throws IOException If error occurs while attempting to accept an incoming socket connection
     */
    private void acceptCallbackConnection() throws IOException {
        try {
            remoteAuthSocket = localCallbackSocket.accept();
            System.out.println("Accepted Connection");
        } catch (IOException e) {
            String message = "Error: Unable to accept authentication callback connection";
            message += e.getMessage();
            authListener.onFailure(message);
            System.err.println(message);
            throw e;
        }
    }

    /**
     * Attempts to read an incoming http header containing the authentication token from the authentication connection
     * @return String http header for the http request
     * @throws IOException If error occurs while attempting to read from socket
     */
    private String parseRequestHeader() throws IOException {
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
            return header;
        } catch (IOException e) {
            String message = "Error: Unable to read authentication callback http header\n";
            message += e.getMessage();
            authListener.onFailure(message);
            System.err.println(message);
            throw e;
        }
    }

    /**
     * Searches the provided header for the API access token
     * @param header String http header obtained from the authentication callback connection
     * @return String API authorization token
     * @throws Exception If unable to locate access token in header
     */
    private String parseTokenFromHeader(String header) throws Exception {
        String tokenLabel = "access_token=";
        int tokenLabelStartIndex = header.indexOf(tokenLabel);
        if (tokenLabelStartIndex != -1) {
            int tokenStartIndex = tokenLabelStartIndex + tokenLabel.length();
            int tokenEndIndex = header.indexOf(' ', tokenStartIndex);
            return header.substring(tokenStartIndex, tokenEndIndex);
        } else {
            String message = "Error: Unable to locate access token in authentication callback http header\n";
            authListener.onFailure(message);
            System.err.println(message);
            throw new Exception("No access token in http header");
        }
    }

    /**
     * Sends back an web page containing some embedded javascript commanding the browser to close the page.
     * If the browser does not support automatically closing web pages through javascript, a message
     * instructing the user to close the page will be shown.
     */
    private void sendResponseMessage() {
        try {
            InputStream inStream = getClass().getResourceAsStream(SUCCESS_HTML);
            InputStreamReader in = new InputStreamReader(inStream);
            PrintWriter out = new PrintWriter(remoteAuthSocket.getOutputStream());

            // Send http response header
            out.print("HTTP/1.1 200 OK\r\n");
            out.print("Content-Type: text/html\r\n");
            out.print("\r\n");

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
            e.printStackTrace();
        }
    }
}