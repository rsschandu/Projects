package com.noob.image.appender.controller;

import com.noob.image.appender.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;


@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {
    @Autowired
    private DataUtils dataUtils;
    @CrossOrigin
    @RequestMapping(value ="/getData", method = RequestMethod.GET)
    public ArrayList<String> getData(@RequestParam String url) throws MalformedURLException, IOException{
        return dataUtils.getData(url);
    }



}

