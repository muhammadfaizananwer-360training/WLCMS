package com.softech.ls360.lcms.contentbuilder.dao;

import java.util.List;
import java.util.Map;

import com.softech.ls360.lcms.contentbuilder.model.Location;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface LocationDAO {
	
	public List<Location> getLocations( Map<String, Object> queryParams);
	public Location addLocation(Location location);
	public boolean delete(String commaseparatedIds);
	public Location findLocationById(Long id);
	public Location updateLocation(Location location);
        public List<Location> getLocationsByOwnerIdAndName(Integer ownerId, String locationName);
        public Location getLocationByOwnerIdAndName(Integer ownerId, String locationName);

	@Transactional
	List<Location> getLocationsByOwnerId(Integer ownerId);

	public List<Location> getLocationsByOwnerIdAndNames(Integer ownerId, Collection<String> locationNames);
}
