package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import com.softech.ls360.lcms.contentbuilder.web.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.model.Address;
import com.softech.ls360.lcms.contentbuilder.model.Location;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.ILocationService;
import com.softech.ls360.lcms.contentbuilder.service.ISynchronousClassService;
import com.softech.ls360.lcms.contentbuilder.web.vo.LocationVO;


@Controller
public class LocationController {

	

	
	@Autowired
	ILocationService ls;
	
	private static Logger logger = LoggerFactory
			.getLogger(CourseController.class);
	
	@RequestMapping(value = "addLocation", method = RequestMethod.POST)
	public @ResponseBody RestResponse addLocation(
			HttpServletRequest request) {
		RestResponse response =  new RestResponse();
		logger.debug("LocationController::addLocation - Start");
		
		Address objAddress = new Address();
		Location location = new Location();
		VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//location.setAddress(address);
		location.setLocationname(request.getParameter("locationName"));
		location.setCity(request.getParameter("city"));
		location.setCountry(request.getParameter("country"));
		location.setDescription(request.getParameter("desc"));
		location.setPhone(request.getParameter("phone"));
		location.setState(request.getParameter("state"));
		location.setZip(request.getParameter("zip"));
		
		location.setEnabledtf("1");
		
		objAddress.setCity(location.getCity());
	    objAddress.setStreetAddress(request.getParameter("address"));
	    objAddress.setZipcode(location.getZip());
	    objAddress.setState(location.getState());
	    objAddress.setCountry(location.getCountry());
		
		location.setContentownerId((int)principal.getContentOwnerId());
		location.setAddress(objAddress);


		try {
			Location obj = ls.addLocation(location);
			response.setData(obj);
		} catch(Exception ex) {
			response.setError(ex.getMessage());
		}
		
		return response;
		
	}
	
	@RequestMapping(value = "deleteLocation", method = RequestMethod.POST)
	public @ResponseBody boolean deleteLocation(
			HttpServletRequest request, HttpServletResponse response
			) {
		
		logger.debug("LocationController::deleteLocation - Start");

		String commasepareteIds =  request.getParameter("locationIds");
		
	
		//location.setAddress(address);
		//location.setId(Long.parseLong(locationId));
		boolean isLocationDelete = ls.delete(commasepareteIds);
		
		return isLocationDelete;
		
		

		
		//return commasepareteIds.split(",");
		
	}
	
	@RequestMapping(value = "locationList", method = RequestMethod.GET)
	public ModelAndView getLocations(
			HttpServletRequest request,HttpServletResponse reponse
			) {
		
		logger.debug("LocationController::getLocations - Start");
		ModelAndView locationModelView=null;
		String renderVmDecisionParam = request.getParameter("renderVmDecisionParam");
		if(renderVmDecisionParam == null){
			locationModelView = new ModelAndView("location");
			return locationModelView;
		}
		
		//Location location = new Location();
		Map<String,Object> locationMap = new HashMap();
		VU360UserDetail user = (VU360UserDetail) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		locationMap.put("contentownerId", (int)user.getContentOwnerId());
		
		List<Location>locationList = ls.getLocations(locationMap);
		
		locationModelView = new ModelAndView("location_list");
		locationModelView.addObject("lstLocation",locationList);
		
		
		return locationModelView;
		
	}
	
	@RequestMapping(value = "loadLocation", method = RequestMethod.GET)
	public @ResponseBody LocationVO loadLocationById(
			HttpServletRequest request, HttpServletResponse response) {
		
		logger.debug("LocationController::addLocation - Start");
		
		//Location location = new Location();
		//VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		
		
		
		
		long id = TypeConvertor.AnyToLong(request.getParameter("locationId"));//new Long(request.getParameter("locationId"));
		
		Location locationObj = ls.findLocationById(id);
		
		LocationVO locationVo = new LocationVO();
		
		locationVo.setId(locationObj.getId());
		locationVo.setCity(locationObj.getCity());
		locationVo.setStreetAddress(locationObj.getAddress().getStreetAddress());
		locationVo.setCountry(locationObj.getCountry());
		locationVo.setState(locationObj.getState());
		locationVo.setPhone(locationObj.getPhone());
		locationVo.setDescription(locationObj.getDescription());
		locationVo.setLocationname(locationObj.getLocationname());
		locationVo.setZip(locationObj.getZip());
		
		

		
		return locationVo;
		
	}
	
	
	@RequestMapping(value = "updateLocation", method = RequestMethod.POST)
	public @ResponseBody RestResponse updateLocation(
			@RequestParam("loc_id") String id,
			@RequestParam("locationName") String locationName,
			@RequestParam("city") String city,
			@RequestParam("country") String country,
			@RequestParam("desc") String desc,
			@RequestParam("phone") String phone,
			@RequestParam("state") String state,
			@RequestParam("zip") String zip,
			@RequestParam("address") String address) {
		
		logger.debug("LocationController::addLocation - Start");
		RestResponse response =  new RestResponse();
		Address objAddress = new Address();
		Location location = new Location();
		VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//location.setAddress(address);
		location.setLocationname(locationName);
		location.setCity(city);
		location.setCountry(country);
		location.setDescription(desc);
		location.setPhone(phone);
		location.setState(state);
		location.setZip(zip);
		location.setId(Long.parseLong(id));
		location.setEnabledtf("1");
		
		objAddress.setCity(location.getCity());
	    objAddress.setStreetAddress(address);
	    objAddress.setZipcode(location.getZip());
	    objAddress.setState(location.getState());
	    objAddress.setCountry(location.getCountry());
		
		location.setContentownerId((int)principal.getContentOwnerId());
		location.setAddress(objAddress);
		


		try {
			Location locationObj = ls.updateLocation(location);
			response.setData(locationObj);
		} catch(Exception ex) {
			response.setError(ex.getMessage());
		}

		return response;

	}

	
	
}


