package com.swooli.bl;

import com.swooli.dao.DaoException;

public interface FacebookService {

    public void createLoginState(String stateId, String state) throws DaoException;

    public String retrieveLoginState(String stateId) throws DaoException;

    public void deleteLoginState(String stateId) throws DaoException;
}
