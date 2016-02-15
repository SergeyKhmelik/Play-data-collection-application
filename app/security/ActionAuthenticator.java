package security;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.errors.error_unauthorized;

/**
 * Security authentication action implementation
 */
public class ActionAuthenticator extends Security.Authenticator{

    /**
     * Gets user from session
     */
    @Override
    public String getUsername(Http.Context ctx) {
        return ctx.session().get("user");
    }

    /**
     * If user is not authorized - renders error_unauthorised page
     */
    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return ok(error_unauthorized.render());
    }
}
