package controllers;

import play.*;
import play.mvc.*;

/**
 * Controller class contains application util actions
 */
public class Application extends Controller {

    /**
     * Javascript reverse routing util, that can be called
     * from *.js files to get routers actions as AJAX functions,
     * get their URL and HTTP-method
     */
    public Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
                Routes.javascriptRouter("jsRoutes",
                        controllers.routes.javascript.FieldController.deleteField(),
                        controllers.routes.javascript.FieldController.saveOrUpdateField(),
                        controllers.routes.javascript.ResponseController.responseDataSocket(),
                        controllers.routes.javascript.UserController.login()
                )
        );
    }
}
