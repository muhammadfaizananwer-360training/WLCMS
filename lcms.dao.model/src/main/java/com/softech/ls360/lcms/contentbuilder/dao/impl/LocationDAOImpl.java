package com.softech.ls360.lcms.contentbuilder.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;


import com.softech.ls360.lcms.contentbuilder.dao.GenericDAOImpl;
import com.softech.ls360.lcms.contentbuilder.dao.LocationDAO;
import com.softech.ls360.lcms.contentbuilder.model.Location;
import java.util.Collection;
import javax.persistence.TypedQuery;

public class LocationDAOImpl extends GenericDAOImpl<Location> implements LocationDAO {

    @Override
    @Transactional
    public List<Location> getLocations(Map<String, Object> queryParams) {
        // TODO Auto-generated method stub

        StringBuilder query = new StringBuilder("from Location l where l.contentownerId = :contentownerId");
        /*StringBuilder query = new StringBuilder("from Location ");
		boolean isFirst = true; 
		if(location.getLocationname()!=null || !location.getLocationname().trim().equals("")){
				
				query.append(" where locationname="+location.getLocationname());
				isFirst=false;
		}
		if(location.getCountry()!=null || !location.getCountry().trim().equals("")){
			if(isFirst){
				query.append(" where country="+location.getCountry());
				isFirst=false;
			}else{
				query.append(" and country="+location.getCountry());
			}
		}*/

        List<Location> locations = (List) getResultList(query.toString(), queryParams);

        return locations;
    }

    @Override
    @Transactional
    public Location addLocation(Location location) {
        // TODO Auto-generated method stub
        Location obj = null;
        try {
            obj = entityManager.merge(location);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return obj;
    }

    @Override
    @Transactional
    public boolean delete(String commaseparatedIds) {
        // TODO Auto-generated method stub

        String[] arr = commaseparatedIds.split(",");
        List<Long> ids = new ArrayList();
        for (int i = 0; i < arr.length; i++) {
            ids.add(Long.parseLong(arr[i]));

        }
        Query queryForSynchronousClass = entityManager.createQuery("from SynchronousClass where location.id in :commaseparatedIds");
        queryForSynchronousClass.setParameter("commaseparatedIds", ids);
        List result = queryForSynchronousClass.getResultList();
        if (result.size() <= 0) {
            Query query = entityManager.createQuery("delete from Location where id in :commaseparatedIds");
            query.setParameter("commaseparatedIds", ids);
            query.executeUpdate();
            return true;
        }
        //deleteAll(locations);
        return false;

    }

    @Override
    @Transactional
    public Location findLocationById(Long id) {
        // TODO Auto-generated method stub

        Location obj = (Location) entityManager.find(Location.class, id);
        return obj;
    }

    @Override
    @Transactional
    public Location updateLocation(Location location) {
        // TODO Auto-generated method stub

        Location persistLocation = findLocationById(location.getId());
        persistLocation.setLocationname(location.getLocationname());
        persistLocation.getAddress().setStreetAddress(location.getAddress().getStreetAddress());
        persistLocation.setCity(location.getCity());
        persistLocation.setCountry(location.getCountry());
        persistLocation.setState(location.getState());

        return super.save(location);

    }

    @Override
    @Transactional
    public List<Location> getLocationsByOwnerIdAndName(Integer ownerId, String locationName) {
        TypedQuery<Location> query = entityManager.createQuery("select l from Location l where locationname =:locationName and contentownerId = :ownerId", Location.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("locationName", locationName);
        return query.getResultList();
    }

    @Override
    @Transactional
    public List<Location> getLocationsByOwnerId(Integer ownerId) {
        TypedQuery<Location> query = entityManager.createQuery("select l from Location l where contentownerId = :ownerId", Location.class);
        query.setParameter("ownerId", ownerId);
        return query.getResultList();
    }

    @Override
    @Transactional
    public List<Location> getLocationsByOwnerIdAndNames(Integer ownerId, Collection<String> locationNames) {
        TypedQuery<Location> query = entityManager.createQuery("select l from Location l where locationname in :locationNames and contentownerId = :ownerId", Location.class);
        query.setParameter("ownerId", ownerId);
        query.setParameter("locationNames", locationNames);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Location getLocationByOwnerIdAndName(Integer ownerId, String locationName) {
        List<Location> locs = getLocationsByOwnerIdAndName(ownerId, locationName);
        if(locs.size() > 0) {
          return locs.get(0);
        } 
        return null;
    }

}
