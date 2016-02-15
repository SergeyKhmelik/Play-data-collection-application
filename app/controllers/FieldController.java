package controllers;

import models.Field;
import models.FieldType;
import models.dto.FieldDTO;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.db.jpa.Transactional;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import security.ActionAuthenticator;
import services.FieldService;
import views.html.*;
import views.html.errors.error_not_found;

import javax.inject.Inject;

import java.util.EnumSet;
import java.util.List;

import static play.data.Form.form;

/**
 * Controller class that contains Field actions
 */
public class FieldController extends Controller {

    @Inject
    private FieldService fieldService;

    /**
     * Saves or updates field from request depending on field identifier.
     * <p>
     * This action builds FieldDTO object from request, which is then validated.
     * If FieldDTO object is not valid - returns BAD_REQUEST status code to client
     * as well as JSON object with validation errors.
     * <p>
     * If inserting field violates unique label constraint - returns BAD_REQUEST with label
     * validation error as JSON.
     * <p>
     * If updating field updates unupdatable field properties - returns BAD_REQUEST with
     * global error as JSON
     * <p>
     * If field save/update is successful - returns OK.
     * <p>
     * This action requires CSRF token.
     *
     * @param idField field object identifier
     * @return 200-OK when save/update is successfully done.
     * 400-BAD_REQUEST when save/update cannot be done due to validation problems.
     */
    @RequireCSRFCheck
    @Transactional
    @Authenticated(ActionAuthenticator.class)
    public Result saveOrUpdateField(int idField) {
        Form<FieldDTO> form = Form.form(FieldDTO.class).bindFromRequest();

        if (form.hasErrors()) {
            Logger.warn("Field save/update request form for field {} has errors: {}.", idField, form.errors());
            return badRequest(form.errorsAsJson());
        }

        FieldDTO fieldDTO = form.get();
        fieldDTO.idField = idField;
        Field field = new Field(fieldDTO);
        Logger.info("Field save/update controller received field data: {}", field.toString());

        if (field.idField == 0) {
            if (fieldService.saveField(field) == null) {
                Logger.warn("Inserted field label violates unique constraint.");
                form.reject(new ValidationError("label", "Field label should be unique."));
                return badRequest(form.errorsAsJson());
            }
        } else {
            if (fieldService.updateField(field) == null) {
                Logger.warn("Field {} tries to update some unupdatable fields.", idField);
                form.reject("global",
                        "You cannot update label, type or options if the field has been already answered.");
                return badRequest(form.errorsAsJson());
            }
        }

        Logger.info("Field saved or updated successfully.");
        return ok();
    }

    /**
     * Renders field form by field identifier.
     * <p>
     * This action renders field_form html-form from Field object,
     * that is retrieved from DB.
     * <p>
     * If there is no field with such identifier - renders NOT_FOUND page.
     * <p>
     * It also adds a CSRF token to the rendered page.
     *
     * @return 200-OK and renders field_current html-page if field is present
     * 404-NOT_FOUND and renders error_not_found html-page if field is absent
     */
    @AddCSRFToken
    @Transactional
    @Authenticated(ActionAuthenticator.class)
    public Result getFieldById(int idField) {
        Form<FieldDTO> form = form(FieldDTO.class);
        EnumSet<FieldType> fieldTypes = EnumSet.allOf(FieldType.class);

        if (idField == 0) {
            return ok(field_current.render(form, idField, fieldTypes));
        } else {
            Field field = fieldService.getFieldById(idField);
            if (field == null) {
                Logger.warn("Requested field (idField = {}) is not found.");
                return notFound(error_not_found.render());
            }

            FieldDTO fieldDTO = new FieldDTO(field);
            Logger.info("Field selected by id {}: {}", idField, field);
            return ok(field_current.render(form.fill(fieldDTO), idField, fieldTypes));
        }
    }

    /**
     * Renders all fields page.
     * <p>
     * This action renders fields_all html-page from a Field objects list
     * that is retrieved from DB.
     * <p>
     * It also adds a CSRF token to the rendered page.
     *
     * @return ok status code and renders a fields_all html-page
     */
    @AddCSRFToken
    @Transactional
    @Authenticated(ActionAuthenticator.class)
    public Result getAllFields() {
        List<Field> result = fieldService.getAllFields();
        Logger.info("All fields are selected: {}", result);
        return ok(fields_all.render(result));
    }

    /**
     * Deletes by its identifier.
     * <p>
     * If field is absent in DB - returns BAD_REQUEST
     * <p>
     * This action requires CSRF token.
     *
     * @param idField field object identifier
     * @return 200-OK if field is successfully deleted.
     * 400-BAD_REQUEST if deleting field is absent.
     */
    @RequireCSRFCheck
    @Transactional
    @Authenticated(ActionAuthenticator.class)
    public Result deleteField(int idField) {
        if (fieldService.deleteField(idField) == null) {
            Logger.warn("Field {} has been already deleted.", idField);
            return badRequest();
        } else {
            Logger.info("Field has been successfully deleted.", idField);
            return ok();
        }
    }
}