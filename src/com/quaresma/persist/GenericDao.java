package com.quaresma.persist;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Order;

public interface GenericDao<T, ID extends Serializable> {

	void save(T object);

	void delete(T object);

	T findById(ID id);

	List<T> listAll();

	List<T> findAll();

	List<T> findAll(Order order);

	List<T> findAllBy(String property, List<?> values, Order order);

	List<T> findAllBy(String property, List<?> values);
	
	void close();
}
