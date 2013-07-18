package com.swooli.blimpl.retrieval;

import com.swooli.dao.DaoException;
import java.util.Collection;
import java.util.Map;

public interface DatabaseObjectRetrievalCallback<T> {

    Map<Long, T> retrieve(Collection<Long> ids) throws DaoException;
}
