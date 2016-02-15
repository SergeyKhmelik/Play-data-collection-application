package services.impl;

import models.QUser;
import models.User;
import play.db.jpa.JPA;
import services.UserService;


import com.mysema.query.jpa.impl.JPAQuery;

public class UserServiceImpl implements UserService {

    @Override
    public boolean authenticate(User user) {
        QUser qUser = QUser.user;
        JPAQuery query = new JPAQuery(JPA.em());
        return query.from(qUser).where(qUser.username.eq(user.username), qUser.password.eq(user.password)).exists();
    }
}
