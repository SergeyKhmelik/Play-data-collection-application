package services.impl;

import models.QResponse;
import models.Response;
import play.db.jpa.JPA;
import services.ResponseService;

import java.util.List;

import com.mysema.query.jpa.impl.JPAQuery;

public class ResponseServiceImpl implements ResponseService {


    @Override
    public List<Response> getAllResponses() {
    	QResponse response = QResponse.response;
    	JPAQuery query = new JPAQuery(JPA.em());
    	return query.from(response).list(response);
    }

    @Override
    public Response saveResponse(Response response) {
    	try {
            response.answers.forEach(answer -> {
                if (answer.options != null && !answer.options.isEmpty()) {
                    answer.options = mergeOptions(answer.options, answer.field);
                    answer.options.forEach(option -> {
                        if (option.idOption == 0) throw new IllegalArgumentException();
                    });
                }
            });
        } catch (IllegalArgumentException e) {
            return null;
        }
        JPA.em().merge(response);
        return response;
    }

	@Override
    public long getResponsesCount() throws Throwable {
        return JPA.withTransaction( () -> {
        	QResponse response = QResponse.response;
        	JPAQuery query = new JPAQuery(JPA.em());
        	return query.from(response).count();
        });
    }
}