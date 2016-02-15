package services;

import services.impl.UserServiceImpl;

import com.google.inject.ImplementedBy;

import models.User;

/**
 * User service interface that declares main User business logic methods
 */
@ImplementedBy(UserServiceImpl.class)
public interface UserService {

    /*
    * Checks whether user with such credentials is present in the DB
    *
    * @param user User object
    * @return true - if user with such credentials exists in the DB;
    *         false - if user with such credentials is absent in the DB
    */
    boolean authenticate(User user);
}
