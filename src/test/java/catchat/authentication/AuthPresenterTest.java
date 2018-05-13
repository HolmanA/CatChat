package catchat.authentication;

import catchat.data.authentication.OAuthService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by andrew on 3/28/18.
 */
public class AuthPresenterTest {
    @Mock
    private OAuthService authService;

    @Mock
    private AuthContract.View authView;

    private AuthPresenter authPresenter;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void start() {
        when(authService.getAuthURL()).thenReturn("test.url");
        authPresenter = new AuthPresenter(authService, authView);
        authPresenter.start();

        verify(authView).openWebPage(any(String.class));
    }
}
