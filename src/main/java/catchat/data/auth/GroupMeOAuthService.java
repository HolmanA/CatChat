package catchat.data.auth;

import javax.net.ServerSocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * OAuthService capable of obtaining an API token to GroupMe's chat servers through OAuth2.0 Implicit Grant
 * authorization.
 * TODO: Call authListener.onFailure() when auth token is not found for some reason
 * TODO: Gracefully handle errors in authentication with a call to the authListener to restart authentication
 */
public class GroupMeOAuthService implements OAuthService {
    private static GroupMeOAuthService INSTANCE;

    private static final String AUTH_PROP_PATH = "/auth.properties";
    private static final String AUTH_URL_PROP_KEY = "base_auth_url";
    private static final String CLIENT_ID_PROP_KEY = "client_id";
    private static final String CALLBACK_PORT_PROP_KEY = "callback_port";

    private String authURL;
    private int callbackPort;
    private Socket remoteAuthSocket;
    private ServerSocket localCallbackSocket;
    private AuthListener authListener;
    private String authToken;

    private GroupMeOAuthService() {
        loadProperties();
    }

    public static GroupMeOAuthService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GroupMeOAuthService();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void setAuthListener(AuthListener listener) {
        authListener = listener;
    }

    @Override
    public void removeAuthListener() {
        authListener = null;
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
    public void run() {
        authenticate();
    }

    /**
     * Loads authorization connection properties from a java properties file
     */
    private void loadProperties() {
        try {
            Properties props = new Properties();
            InputStream in = getClass().getResourceAsStream(AUTH_PROP_PATH);
            props.loadFromXML(in);
            authURL = props.getProperty(AUTH_URL_PROP_KEY) + props.getProperty(CLIENT_ID_PROP_KEY);
            callbackPort = Integer.parseInt(props.getProperty(CALLBACK_PORT_PROP_KEY));
        } catch (Exception e) {
            System.err.println("FATAL ERROR: Unable to load authentication properties");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void authenticate() {
        initializeCallbackSocket();
        acceptCallbackConnection();
        String requestHeader = parseRequestHeader();
        sendResponseMessage();
        authToken = parseTokenFromHeader(requestHeader);
        authListener.onSuccess();
    }

    private void initializeCallbackSocket() {
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try {
            localCallbackSocket = factory.createServerSocket(callbackPort);
            System.out.println("Server Running on 127.0.0.1:" + callbackPort);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Attempts to accept an incoming connection on the local authorization callback socket
     */
    private void acceptCallbackConnection() {
        try {
            remoteAuthSocket = localCallbackSocket.accept();
            System.out.println("Accepted Connection");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Attempts to read an incoming http header containing the auth token from the authentication connection
     * @return String http header for the http request
     */
    private String parseRequestHeader() {
        String header = "";
        try {
            int length;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if ((length = remoteAuthSocket.getInputStream().read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            header = baos.toString("UTF-8");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
        return header;
    }

    /**
     * Searches the provided header for the API access token
     * @param header String http header obtained from the authentication callback connection
     * @return String API authorization token
     */
    private String parseTokenFromHeader(String header) {
        String token = "";
        String tokenLabel = "access_token=";
        int tokenLabelStartIndex = header.indexOf(tokenLabel);
        if (tokenLabelStartIndex != -1) {
            int tokenStartIndex = tokenLabelStartIndex + tokenLabel.length();
            int tokenEndIndex = header.indexOf(' ', tokenStartIndex);
            token = header.substring(tokenStartIndex, tokenEndIndex);
        } else {
            System.err.println("Unable to locate access token in response header");
            System.exit(-1);
        }
        return token;
    }

    /**
     * Sends back an html page containing some embedded javascript commanding the browser to close the page.
     * If the browser does not support automatically closing web pages through javascript, a message
     * instructing the user to close the page will be shown.
     * TODO: Refactor the html page into a separate file to increase maintainability
     */
    private void sendResponseMessage() {
        try {
            PrintWriter out = new PrintWriter(remoteAuthSocket.getOutputStream());
            out.print("HTTP/1.1 200 OK\r\n");
            out.print("Content-Type: text/html\r\n");
            out.print("\r\n");
            out.print("<html>\r\n");
            out.print("</head>\r\n");
            out.print("<body>\r\n");
            out.print("<p>Authentication Successful. You may now close this page</p>\r\n");
            out.print("<script type=\"text/javascript\">window.close();</script>\r\n");
            out.print("</body>\r\n");
            out.print("</html>\r\n");
            out.flush();
            remoteAuthSocket.close();
            localCallbackSocket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}