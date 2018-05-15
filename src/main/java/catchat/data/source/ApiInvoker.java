package catchat.data.source;

import catchat.data.authentication.OAuthService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ApiInvoker
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public class ApiInvoker {
    private static ApiInvoker INSTANCE;
    private OAuthService authService;
    private List<ApiCommand> apiCommands;

    private ApiInvoker() {
        apiCommands = new ArrayList<>();
    }

    public static ApiInvoker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApiInvoker();
        }
        return INSTANCE;
    }

    public void setAuthService(OAuthService authService) {
        this.authService = authService;
    }

    public void add(ApiCommand command) {
        apiCommands.add(command);
    }

    public void execute() {
        for (ApiCommand command : apiCommands) {
            execute(command);
        }
        apiCommands.clear();
    }

    public void execute(ApiCommand command) {
        if (authService == null || authService.getAPIToken() == null) {
            throw new RuntimeException("AuthToken not set on ApiInvoker");
        }
        try {
            command.buildCommand(authService.getAPIToken());
            command.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        apiCommands.clear();
    }
}
