package services;

import com.google.inject.ImplementedBy;

import models.Response;

import java.util.List;

import services.impl.ResponseServiceImpl;

/**
 * Response service interface that declares main Response business logic methods.
 * Response business logic contains operations with Option object, therefore
 * this interface extends OptionMerging interface.
 */
@ImplementedBy(ResponseServiceImpl.class)
public interface ResponseService extends OptionMerging{

    /**
     * Returns List of all user responses.
     *
     * @return List of Response objects.
     */
    List<Response> getAllResponses();

    /**
     * Saves Response object in DB.
     * If Response has outdated option - returns null
     *
     * @param response Response object
     * @return persisted Response object or null if response data is outdated
     */
    Response saveResponse(Response response);

    /**
     * Counts user responses in DB.
     *
     * @return long responses count
     * @throws Throwable
     */
    long getResponsesCount() throws Throwable;
}