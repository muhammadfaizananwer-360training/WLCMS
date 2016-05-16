package com.softech.ls360.lcms.contentbuilder.web.controller;

import com.softech.ls360.lcms.contentbuilder.model.ClassInstructor;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.IClassInstructorService;
import com.softech.ls360.lcms.contentbuilder.utils.TypeConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by muhammad.imran on 4/27/2016.
 */
@Controller
public class ClassInstructorController {

    @Autowired
    public IClassInstructorService classInstructorService;

    private static Logger logger = LoggerFactory
            .getLogger(ClassInstructorController.class);


    @RequestMapping(value = "classInstructors", method = RequestMethod.GET)
    public ModelAndView getVideoAssetMgrPage(HttpServletRequest request) throws Exception {
        VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        /*RestResponse response = new RestResponse();
        Integer pageNumber = 0;
        if (null != request.getParameter("iDisplayStart"))
            pageNumber = (Integer.valueOf(request.getParameter("iDisplayStart"))/10);
        String searchParameter = request.getParameter("sSearch");
        Integer pageDisplayLength = Integer.valueOf(request.getParameter("iDisplayLength"));*/
        List<ClassInstructor> lst = classInstructorService.findByContentOwnerId(principal.getContentOwnerId());
        /*ClassInstructorVO obj = new ClassInstructorVO();
        obj.setiTotalDisplayRecords(20);
        obj.setiTotalRecords(20);
        obj.setAaData(lst);*/
        ModelAndView view = new ModelAndView("classinstructor_list");
        view.addObject("lstclassinstructor",lst);
        //response.setData(lst);


        return view;

    }


    @RequestMapping(value = "addClasInstructor", method = RequestMethod.POST)
    public @ResponseBody ClassInstructor addClasInstructor(
            HttpServletRequest request, HttpServletResponse response) {

        logger.debug("LocationController::addLocation - Start");

        ClassInstructor obj = new ClassInstructor();
        VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        obj.setId(TypeConvertor.AnyToLong(request.getParameter("classInstructorId")));
        obj.setFirstName(request.getParameter("firstName"));
        obj.setLastName(request.getParameter("lastName"));
        obj.setEmail(request.getParameter("email"));
        obj.setPhoneNo(request.getParameter("phoneNo"));
        obj.setContentOwnerId(principal.getContentOwnerId());
        obj.setIsActive(true);
        try {
            return classInstructorService.save(obj);
        }catch(Exception ex){
            obj.setDeleteExceptionMessage(ex.getMessage());
            return obj;
        }

    }

    @RequestMapping(value = "deleteClassInstructions", method = RequestMethod.POST)
    public @ResponseBody String deleteClassInstructions(
            HttpServletRequest request, HttpServletResponse response
    ) {

        logger.debug("LocationController::deleteClassInstructions - Start");

        String commasepareteIds =  request.getParameter("classInstructionIds");


        //location.setAddress(address);
        //location.setId(Long.parseLong(locationId));
        try {
            boolean isInstructionDelete = classInstructorService.deleteInstructors(commasepareteIds) == 0 ? false : true;
        }catch (Exception ex){

            return ex.getMessage();

        }

        return "";




        //return commasepareteIds.split(",");

    }

    @RequestMapping(value = "loadClassInstructor", method = RequestMethod.GET)
    public @ResponseBody ClassInstructor loadInstructorById(
            HttpServletRequest request, HttpServletResponse response) {

             logger.debug("LocationController::addLocation - Start");

        //Location location = new Location();
        //VU360UserDetail principal = (VU360UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();






        long id = TypeConvertor.AnyToLong(request.getParameter("instructorId"));//new Long(request.getParameter("locationId"));
        ClassInstructor obj = classInstructorService.findById(id);
        return obj;

    }





}
