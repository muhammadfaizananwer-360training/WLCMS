package com.softech.ls360.lcms.contentbuilder.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;

@Controller
public class HTTPErrorHandler{

    //String path = "/error";//unused field

    @RequestMapping(value="/404")
    public ModelAndView error404(){
       ModelAndView error404 = new ModelAndView("error");
       
       return error404;
    }
}