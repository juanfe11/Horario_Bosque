package co.edu.unbosque.armadorhorarios.services;

import java.util.List;

public interface CRUDOperation<T> {

    public int create(T data) throws Exception;

    public List<T> getAll();

    public int deleteByID(Long id) throws Exception;

    public int updateByID(Long id, T data) throws Exception;

    public long count();

    public boolean exist(Long id);
}
