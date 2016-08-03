package com.softech.ls360.lcms.contentbuilder.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softech.ls360.lcms.contentbuilder.dao.LocationDAO;
import com.softech.ls360.lcms.contentbuilder.model.Location;
import com.softech.ls360.lcms.contentbuilder.service.ILocationService;


public class LocationServiceImpl implements ILocationService{

	@Autowired
	public LocationDAO locationDAO;
	
	@Override
	public List<Location> getLocations(Map<String, Object> queryParams) {
		// TODO Auto-generated method stub
		return locationDAO.getLocations(queryParams);
		
	}

	@Override
	public Location addLocation(Location location) {
		List<Location> locations = locationDAO.getLocationsByOwnerIdAndName(location.getContentownerId(), location.getLocationname());
		if(locations.size() > 0) {
			throw new RuntimeException("duplicate");
		}
		return locationDAO.addLocation(location);
	}

	@Override
	public boolean delete(String commaseparatedIds) {
		// TODO Auto-generated method stub
		return locationDAO.delete(commaseparatedIds);
	}

	@Override
	public Location findLocationById(Long id) {
		// TODO Auto-generated method stub
		Location obj = locationDAO.findLocationById(id);
		obj.setStreetAddress(obj.getAddress().getStreetAddress());
		return obj;
	}

	@Override
	public Location updateLocation(Location location) {


		List<Location> locations = locationDAO.getLocationsByOwnerIdAndName(location.getContentownerId(), location.getLocationname());
		if(locations.size() > 0) {
			if(locations.size() > 1 || !locations.get(0).getId().equals(location.getId())) {
				throw new RuntimeException("duplicate");
			}
		}
		return locationDAO.updateLocation(location);
	}
	
	
	
	
	
	
	

}
