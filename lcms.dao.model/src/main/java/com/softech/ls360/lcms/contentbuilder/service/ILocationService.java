package com.softech.ls360.lcms.contentbuilder.service;

import java.util.List;
import java.util.Map;

import com.softech.ls360.lcms.contentbuilder.model.Location;

public interface ILocationService {
	
	public List<Location> getLocations( Map<String, Object> queryParams);
	public Location addLocation(Location location);
	public boolean delete(String commaseparatedIds);
	public Location findLocationById(Long id);
	public Location updateLocation(Location location);
}
