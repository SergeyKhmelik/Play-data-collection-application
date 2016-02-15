package controllers;

import com.google.inject.Inject;

import models.User;
import play.Logger;
import play.data.Form;
import play.db.jpa.Transactional;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

/**
 * Controller class that contains User actions.
 */
public class UserController extends Controller{

    @Inject
    private UserService userService;

    /**
     * Authenticates user from request.
     *
     * @return  200-OK if user is authenticated and authorized
     *          400-BAD_REQUEST if user credentials are wrong
     */
    @RequireCSRFCheck
    @Transactional
    public Result login() {
        Form<User> form = Form.form(User.class).bindFromRequest();
        if(form.hasErrors()) {
            Logger.warn("User login form has errors: {}.", form.errors());
            return badRequest(form.errorsAsJson());
        }

        User user = form.get();
        if(userService.authenticate(user)){
            session().clear();
            session("user", user.username);
            Logger.info("User '{}' has been successfully logged in.", user.username);
            return ok();
        } else {
            form.reject("global", "Credentials are wrong.");
            Logger.warn("Trying to log in user '{}', but credentials are wrong.", user.username);
            return badRequest(form.errorsAsJson());
        }
    }

    /**
     * Removes user from PLAY_SESSION cookie and redirects to start page.
     *
     * @return redirect to start page.
     */
    public Result logout(){
        session().remove("user");
        Logger.info("User logged out.");
        return redirect(routes.ResponseController.showResponseForm());
    }

}
