package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;



/** comments by Archana **/
@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    //The method getUserByUserName fetches the user based on userName and if no record is found in the database it returns null
    //This method is called in the UserBusinessService class for the functionality of registering the new user with userName
    // and compared with the existing user having the same userName
    //if the existing user with userName matches the new user userName then application should throw an error
    public UserEntity getUserByUserName(final String userName) throws NoResultException  {
        try {
            return entityManager.createNamedQuery("userByName", UserEntity.class).setParameter("userName", userName)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }

    //The method getUserByEmail fetches the user based on email and if no record is found in the database it returns null
    //This method is called in the UserBusinessService class for the functionality of registering the new user with email
    // and compared with the existing user having the same email
    //if the existing user with email matches the new user email then application should throw an error
    public UserEntity getUserByEmail(final String email) throws NoResultException {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
