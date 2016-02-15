package controllers;

import models.*;
import play.Logger;
import play.data.validation.ValidationError;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.mvc.WebSocket;
import security.ActionAuthenticator;
import services.FieldService;
import services.ResponseService;
import views.html.response_form;
import views.html.responses_all;

import javax.inject.Inject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static play.data.Form.form;
import static play.libs.Json.toJson;

/**
 * Controller class that contains Response actions
 */
public class ResponseController extends Controller {

    /**
     * Web socket connections set
     */
    private static final Set<WebSocket.Out<String>> SOCKETS = new HashSet<>();

    @Inject
    private ResponseService responseService;

    @Inject
    private FieldService fieldService;

    /**
     * Renders response form.
     * This action renders response_form html-form from a List of active fields.
     * It also adds a CSRF token to the rendered page.
     *
     * @return 200-OK and renders a response_form html page
     */
    @AddCSRFToken
    @Transactional
    public Result showResponseForm() {
        Form<Response> responseForm = form(Response.class).bindFromRequest();
        List<Field> activeFields = fieldService.getAllActiveFields();
        Logger.info("Rendering questioner form from active fields: {}", activeFields);
        return ok(response_form.render(responseForm, activeFields));
    }

    /**
     * Saves user response.
     *
     * This action builds a Response object from request by creating and parsing DynamicForm data.
     * Built Response object is then validated and returned back to client as a JSON object.
     *
     * If response object is not valid - returns BAD_REQUEST status code to client
     * as well as JSON object with validation errors.
     *
     * If response data is outdated - returns BAD_REQUEST status code to client
     * as well as JSON object with global errors.
     *
     * This action requires CSRF token.
     *
     * @return  200-OK with saved response object as JSON
     *          400-BAD_REQUEST with errors as JSON
     */
    @RequireCSRFCheck
    @Transactional
    public Result createResponse() {
        DynamicForm form = form().bindFromRequest();
        List<Field> activeFields = fieldService.getAllActiveFields();

        Response response = new Response(form.data(), activeFields);
        Logger.info("User send response: {}.", response);
        List<ValidationError> errors = response.validate();
        if (errors != null) {
            errors.forEach(form::reject);
            Logger.warn("User response contains validation errors: {}.", errors);
            return badRequest(form.errorsAsJson());
        }

        Response result = responseService.saveResponse(response);

        if(result == null) {
            form.reject("global", "Some options are outdated!");
            Logger.warn("Users response contains outdated data.");
            return badRequest(form.errorsAsJson());
        } else {
            Logger.info("Response saved: {}.", response);
            return ok(toJson(response));
        }
    }

    /**
     * Renders all responses page.
     * This action renders response_all html-page from a List of Responses.
     *
     * It also adds a CSRF token to the rendered page.
     *
     * @return 200-OK and renders a responses_all html-page
     */
    @AddCSRFToken
    @Transactional
    @Authenticated(ActionAuthenticator.class)
    public Result getAllResponses() {
        List<Field> fields = fieldService.getAllActiveFields();
        List<Response> responses = responseService.getAllResponses();
        Logger.info("Received all responses: {}.", responses);
        return ok(responses_all.render(fields, responses));
    }

    /**
     * Receives messages from clients and broadcasts them to all its connections.
     *
     * This web socket is implemented on callbacks.
     * <ul>
     *     <li>onCreate - connection WebSocket sends responses count back to the client</li>
     *     <li>onMessage - broadcasts the message to all connected clients</li>
     *     <li>onClose - removes current connection from connections set</li>
     * </ul>
     *
     *
     * @return WebSocket object
     */
    public WebSocket<String> responseDataSocket() {
        return WebSocket.whenReady((in, out) -> {
            SOCKETS.add(out);
            Logger.info("Socket connection added.");
            out.write("{\"responsesCount\":" + responseService.getResponsesCount() + "}");

            in.onMessage((String message) -> {
                    SOCKETS.forEach(s -> s.write(message));
                    Logger.info("Socket message received : {}.", message);
            });

            in.onClose(() -> {
                    SOCKETS.remove(out);
                    Logger.debug("Socket connection closed.");
                });
        });
    }
}