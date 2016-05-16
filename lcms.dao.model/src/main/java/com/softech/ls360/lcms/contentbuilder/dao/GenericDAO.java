package com.softech.ls360.lcms.contentbuilder.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Map;

import javax.persistence.StoredProcedureQuery;


public interface GenericDAO<T>{
	//Constants below

	static final String dateFormatString = "yyyy-MM-dd";
	static final String dateTimeFormatString = "yyyy-MM-dd hh:mm:ss";

	ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>(){


		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat(dateFormatString);
		}


	}; //new SimpleDateFormat("yyyy-MM-dd");

	ThreadLocal<DateFormat> DATE_TIME_FORMAT = new ThreadLocal<DateFormat>(){

		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat(dateTimeFormatString);
		}

	};
	//SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	//Methods below
	T save(T entity);
	void persist(T entity);
	T update(T entity);
	void delete(T entity);
	void deleteAll(List<T> entities);
	int deleteById(Long id);

	T getById(Long id);
	List<T> find(T entity);
	@Deprecated
	List<T> search(T entity);
	@Deprecated
	List<T> search(T entity, String sortByColumn, Long sortDirection);
	List<T> getAll();
	List<T> getAll(String sortByColumn, Long sortDirection);
	List<T> getResultList(String jpql, Map<String, Object> queryParams);
	
	List<T> callStoredProc (String ProcName, SPCallingParams ... args);
	StoredProcedureQuery createQueryParameters (String ProcName, SPCallingParams ... args);
	
	//<T> T map(Class<T> type, Object[] tuple);	 
}
