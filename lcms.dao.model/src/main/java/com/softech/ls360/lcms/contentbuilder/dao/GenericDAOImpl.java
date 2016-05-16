package com.softech.ls360.lcms.contentbuilder.dao;



import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;





import javax.persistence.StoredProcedureQuery;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generic DAO implementation class that provides basic Data access and
 * manipulation functionality
 * 
 
 * 
 */


public abstract class GenericDAOImpl<T> implements GenericDAO<T>
{
	private static Logger logger = Logger.getLogger(GenericDAOImpl.class);
	
	@SuppressWarnings("unchecked")
	protected Class<T> clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	@PersistenceContext
	protected EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {		
		this.entityManager = entityManager;
		System.out.println (entityManager.toString());
	}

	@Override
        @Transactional
	public T save(T entity) throws DataAccessException {
		logger.debug("Merging Object of Type: " + entity.getClass().getCanonicalName());
		try{
			entity = entityManager.merge(entity);
			return entity;
		}
		finally{						
			entityManager.close();
			logger.info("closing entityManager");
		}	
	}

	@Override
        @Transactional
	public void persist(T entity) throws DataAccessException {
		logger.debug("Persisting Object of Type: " + entity.getClass().getCanonicalName());
		System.out.println ("in persist" +  entity.toString());
		try
		{
			entityManager.persist(entity);
			
		}catch (Exception ex)
		{
			logger.debug("GenericDAOImpl::persist Exception " + ex.getMessage());
		}
		finally{						
			entityManager.close();
			logger.info("closing entityManager");
		}	
		
	}

	
	/**
	 * This method update record in database
	 * 
	 * @param updatableObj
	 *            - the updated copy to merge
	 * @return the updated, registered persistent instance
	 * */
	@Override
        @Transactional
	public T update(T entity) {
		return save(entity);
	}

	/**
	 * Delete the given entity.
	 * 
	 * @param entityClass
	 *            - the entityClass to delete
	 * */
	@Override
	public void delete(T entity) {
		logger.debug("Delete entity of Type: " + entity.getClass().getCanonicalName());
		try{
			entityManager.remove(entity);
		}
		finally{						
			entityManager.close();
			logger.info("closing entityManager");
		}	
	}

	/**
	 * Delete all given entities.
	 * 
	 * @param entities
	 *            - the entities to delete
	 * */
	public void deleteAll(List<T> entities) {
		for (T entity : entities) {
			delete(entity);
		}
	}
	
	/**
	 * Delete Entity by Id
	 * @param id
	 * 		- Entity Id 
	 */
	@Override
	
	public int deleteById(Long id) {
		Query deleteQuery = entityManager.createQuery("DELETE FROM " + clazz.getName() + " entity WHERE entity.id=:id");
		deleteQuery.setParameter("id", id);
		return deleteQuery.executeUpdate();		
	}	

	/**
	 * Read the entity instance of the given class with the given id, throwing
	 * an exception if not found. Retrieves read-write objects from the
	 * hibernate UnitOfWork in case of a non-read-only transaction, and
	 * read-only objects else.
	 * @param id
	 *            - Identity key of the desired object
	 * @return the entity instance
	 * */
	@Override
	public T getById(Long id) {
		logger.debug("getById:" + id + ", T class: " + clazz );
		
		try{
			return entityManager.find(clazz, id);	
		}
		finally{						
				entityManager.close();
				logger.info("closing entityManager");
		}	
		
	}
	
	@Override
	public List<T> getResultList(String jpql, Map<String, Object> queryParams) {
		logger.debug("Query: " + jpql);
		Query query = entityManager.createQuery(jpql.toString());
		for(Map.Entry<String, Object> entry : queryParams.entrySet()){
			query.setParameter(entry.getKey(), entry.getValue());
		}
		@SuppressWarnings("unchecked")
		List<T> resultList = (List<T>) query.getResultList();
		return resultList;
	}
	
	/**
	 * Generic search method without sort column and direction which searches
	 * the record depending upon property values set in the passed model bean. 
	 * This method has been deprecated and will be removed soon. Create Query 
	 * specific methods in related Entity DAO's instead.
	 * 
	 * @param entity
	 *            Entity with data set for criteria search
	 * @return List of searched model beans by applying the passed values of
	 *         model bean as criteria
	 */
	@Override
	@Deprecated
	public List<T> search(T entity) {
		return search(entity, null, null);
	}

	/**
	 * Generic search method which searches the record depending upon property
	 * values set in the passed model bean. 
	 * This method has been deprecated and will be removed soon. Create Query 
	 * specific methods in related Entity DAO's instead.
	 * 
	 * @param entity
	 *            Entity with data set for criteria search
	 * @param sortByColumn
	 *            Name of column to be used for sorting result
	 * @param sortDirection
	 *            1: ASC 2:DESC
	 * @return List of searched model beans by applying the passed values of
	 *         model bean as criteria
	 */
	@Override
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<T> search(T entity, String sortByColumn, Long sortDirection) {
		List<Object> params = new ArrayList<Object>();
		String query = "SELECT entity FROM " + clazz.getName() + " entity";
		
		Field[] publicFields = entity.getClass().getDeclaredFields();
		for (Field field : publicFields) {
			field.setAccessible(true);
			Object value = null;
			try {
				value = field.get(entity);
			} catch (IllegalArgumentException e) {
				logger.error("", e);
			} catch (IllegalAccessException e) {
				logger.error("", e);
			}
			if (value == null
					|| Collection.class.isAssignableFrom(field.getType())
					|| java.lang.reflect.Modifier.isStatic(field.getModifiers())
					|| field.getAnnotation(Transient.class) != null) {
				continue;
			}
			if (field.getType().equals(String.class)) {
				if (query.contains("where")) {
					query = query + " and " + field.getName() + " like ?"
							+ params.size();
				} else {
					query = query + " where " + field.getName() + " like ?"
							+ params.size();
				}
				params.add("%" + value.toString() + "%");
			} else if (field.getType().equals(Date.class)) {
				Date startDateTime = null;
				Date endDateTime = null;
				String startTime = DATE_FORMAT.get().format(value) + " 00:00:00";
				String endTime = DATE_FORMAT.get().format(value) + " 23:59:59";
				try {
					startDateTime = DATE_TIME_FORMAT.get().parse(startTime);
					endDateTime = DATE_TIME_FORMAT.get().parse(endTime);
				} catch (ParseException e) {
					logger.error("", e);
				}
				if (startDateTime == null || endDateTime == null) {
					continue;
				}
				if (query.contains("where")) {
					query = query + " and " + field.getName() + " between ?"
							+ params.size() + " and ?" + (params.size() + 1);
				} else {
					query = query + " where " + field.getName() + " between ?"
							+ params.size() + " and ?" + (params.size() + 1);
				}

				params.add(startDateTime);
				params.add(endDateTime);
			} else {
				if (query.contains("where")) {
					query = query + " and " + field.getName() + " = "
							+ params.size();
				} else {
					query = query + " where " + field.getName() + " = ?"
							+ params.size();
				}
				params.add(value);
			}
		}

		Query queryObj = entityManager.createQuery(query);

		// Check is sorting parameters are not not blank or null, apply sorting
		// criteria
		if (sortByColumn != null && sortDirection != null) {
			if (sortDirection == 1L) {
				query = query + " Order By " + sortByColumn + " ASC";
			} else {
				query = query + " Order By " + sortByColumn + " DESC";
			}
			if (params.size() > 0) {
				for (int i = 0; i < params.size(); i++) {
					queryObj.setParameter(i, params.get(i));
				}
			}
		}
		
		return queryObj.getResultList();
	}

	@Override
	public List<T> getAll() {
		return getAll(null, null);
	}
	
	@Override
	public List<T> getAll(String sortByColumn, Long sortDirection) {
		String query = "SELECT entity FROM " + clazz.getName() + " entity";
		if (sortByColumn != null && sortDirection != null) {
			if (sortDirection == 1L) {
				query = query + " Order By " + sortByColumn + " ASC";
			} else {
				query = query + " Order By " + sortByColumn + " DESC";
			}
		}
		try{
			@SuppressWarnings("unchecked")
			List<T> resultList = (List<T>) entityManager.createQuery(query).getResultList();
			return resultList;
		}
		finally{						
			entityManager.close();
			logger.info("closing entityManager");
		}	
	}
	/**
	 * Generic find method which identify the exact records depending upon
	 * property values set in the passed model bean else null will be returned
	 * 
	 * @param entity
	 *            Entity with data set for criteria search
	 * @return List of identified model beans by applying the passed values of
	 *         model bean as criteria
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> find(T entity) {
		List<Object> params = new ArrayList<Object>();
		String query = "SELECT entity FROM " + entity.getClass().getName()
				+ " entity";
		
		Field[] publicFields = entity.getClass().getDeclaredFields();
		for (Field field : publicFields) {
			field.setAccessible(true);
			Object value = null;
			
			try {
				value = field.get(entity);
			} catch (IllegalArgumentException e) {
				logger.error("", e);
			} catch (IllegalAccessException e) {
				logger.error("", e);
			}
			if (value == null
					|| Collection.class.isAssignableFrom(field.getType())
					|| java.lang.reflect.Modifier.isStatic(field.getModifiers())
					|| field.getAnnotation(Transient.class) != null) {
				continue;
			}
			
			if (field.getType().equals(Date.class)) {
				Date startDateTime = null;
				Date endDateTime = null;
				String startTime = DATE_FORMAT.get().format(value) + " 00:00:00";
				String endTime = DATE_FORMAT.get().format(value) + " 23:59:59";
				try {
					startDateTime = DATE_TIME_FORMAT.get().parse(startTime);
					endDateTime = DATE_TIME_FORMAT.get().parse(endTime);
				} catch (ParseException e) {
					logger.error("", e);
				}
				if (startDateTime == null || endDateTime == null) {
					continue;
				}
				if (query.contains("where")) {
					query = query + " and " + field.getName() + " between ?"
							+ params.size() + " and ?" + (params.size() + 1);
				} else {
					query = query + " where " + field.getName() + " between ?"
							+ params.size() + " and ?" + (params.size() + 1);
				}
				params.add(startDateTime);
				params.add(endDateTime);
			} else {
				if (query.contains("where")) {
					query = query + " and " + field.getName() + " = ?"
							+ params.size();
				} else {
					query = query + " where " + field.getName() + " = ?"
							+ params.size();
				}
				params.add(value);
			}
		}
		if (params.size() > 0) {
			try
			{				
				Query queryObj = entityManager.createQuery(query);
	
				for (int i = 0; i < params.size(); i++) {
					queryObj.setParameter(i, params.get(i));
					System.out.println("GenericDAOImpl::find " + params.getClass().getSimpleName() + params.get(i));
				}
				//logger.debug();
				return queryObj.getResultList();
			}
			catch (Exception ex)
			{
				logger.error(ex);
				return null;
			}
			finally{			
				entityManager.close();
				logger.info("closing entityManager");				
			}
		} else {
			return null;
		}
		
	}
	
	@Override
	public  StoredProcedureQuery createQueryParameters (String ProcName, SPCallingParams ... args)
	{
		
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery (ProcName);
		
			for ( SPCallingParams arg : args )
			 {
				query.registerStoredProcedureParameter(arg.Param, arg.ValueType, arg.parameterMode);
				try{
						 if (arg.parameterMode == ParameterMode.IN)
						 {
							 if (arg.ValueType == Long.class)
							 {
								 Long value = Long.parseLong(arg.Value);
								 query.setParameter(arg.Param, value);
							 }
							 else if (arg.ValueType == Integer.class)
							 {
								 Integer value = Integer.parseInt(arg.Value);
								 query.setParameter(arg.Param, value);
							 } else if (arg.ValueType == String.class)
							 {
								 query.setParameter(arg.Param, arg.Value);
							 } else if (arg.ValueType == float.class)
							 {
								 float value = Float.parseFloat(arg.Value);
								 query.setParameter(arg.Param, value);
							 }  else if (arg.ValueType == Float.class)
							 {
								 float value = Float.parseFloat(arg.Value);
								 query.setParameter(arg.Param, value);
							 }
							 else if (arg.ValueType == char.class)
							 {
								 char value = arg.Value.toCharArray()[0];
								 query.setParameter(arg.Param, value);
							 } 
							 else if (arg.ValueType == BigDecimal.class)
							 {
								 BigDecimal value =  new BigDecimal(arg.Value.replaceAll(",", "") );
								 query.setParameter(arg.Param, value);
							 }
							 else if (arg.ValueType == double.class)
							 {
								 double value =  Double.parseDouble(arg.Value);
								 query.setParameter(arg.Param, value);
							 } 
							 else if (arg.ValueType == Boolean.class)
							 {
								 boolean value = Boolean.parseBoolean(arg.Value);
								 query.setParameter(arg.Param, value);
							 }
							 else if (arg.ValueType == boolean.class)
							 {
								 boolean value = Boolean.parseBoolean(arg.Value);
								 query.setParameter(arg.Param, value);
							 }
						 }
				} catch(Exception ex){
						System.out.println (ex.getMessage());
						//System.out.println (ex.getStackTrace());
						System.out.println (org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(ex));
						logger.error(ex.getMessage());						
				}		 
			 }
			return query;		
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public  List<T> callStoredProc (String ProcName, SPCallingParams ... args)
	{
		List<T> list = null;
		
		try
		{	
			//entityManager.getTransaction().begin();
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery (ProcName);
				
			 for ( SPCallingParams arg : args )
			 {
				 query.registerStoredProcedureParameter(arg.Param, arg.ValueType, arg.parameterMode);
				 if (arg.parameterMode == ParameterMode.IN)
				 {
					 if (arg.ValueType == Integer.class)
					 {
						 Integer value = Integer.parseInt(arg.Value);
						 query.setParameter(arg.Param, value);
					 } else if (arg.ValueType == String.class)
					 {
						 query.setParameter(arg.Param, arg.Value);
					 } else if (arg.ValueType == float.class)
					 {
						 float value = Float.parseFloat(arg.Value);
						 query.setParameter(arg.Param, value);
					 } 
					 else if (arg.ValueType == char.class)
					 {
						 char value = arg.Value.toCharArray()[0];
						 query.setParameter(arg.Param, value);
					 } 
					 else if (arg.ValueType == boolean.class)
					 {
						 boolean value = Boolean.parseBoolean(arg.Value);
						 query.setParameter(arg.Param, value);
					 }
					 else if (arg.ValueType == java.sql.Date.class)
					 {
						 java.sql.Date value = java.sql.Date.valueOf(arg.Value);
						 query.setParameter(arg.Param, value);
					 }

				 }
			 }
			 
///			 StoredProcedureResultSetReturn
		//	 list = query.getResultList();
			 list =  query.getResultList();
			
		}
		catch(Exception ex)
		{
			System.out.println (ex.getMessage());
			//System.out.println (ex.getStackTrace());
            System.out.println (org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(ex));
			logger.error(ex.getMessage());
			
		}		
		finally{			
			entityManager.close();
			logger.info("closing entityManager");
			
		}
		return list;
	}
	
	/*@Override
	public <T> T map(Class<T> type, Object[] tuple){
		   List<Class<?>> tupleTypes = new LinkedList<>();
		   for(Object field : tuple){
		      tupleTypes.add(field.getClass());
		   }
		   try {
		      Constructor<T> ctor = type.getConstructor(tupleTypes.toArray(new Class<?>[tuple.length]));
		      return ctor.newInstance(tuple);
		   } catch (Exception e) {
		      throw new RuntimeException(e);
		   }
		}
	*/
}
