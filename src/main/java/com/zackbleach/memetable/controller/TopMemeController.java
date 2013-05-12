package com.zackbleach.memetable.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import com.zackbleach.memetable.domainobject.TopMeme;
import com.zackbleach.memetable.domainobject.TopMemes;

@Controller
@RequestMapping("api/memes")
public class TopMemeController {

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public ResponseEntity<TopMemes> getEffective(UriComponentsBuilder builder) {
    	//Could use response body here as well
    	TopMemes top = new TopMemes();
    	List<TopMeme> memes = new ArrayList<TopMeme>();
    	memes.add(new TopMeme(1, "Actual Advice Mallard", 4930));
    	memes.add(new TopMeme(2, "Success Kid", 3433));
    	memes.add(new TopMeme(3, "Confession Bear", 3344));
    	memes.add(new TopMeme(4, "Overly Attached GF", 2234));
    	memes.add(new TopMeme(5, "First World Problems", 1343));
    	top.setTopMemes(memes);
        return new ResponseEntity<TopMemes>(top, HttpStatus.OK);
    }

    @RequestMapping(value = "/effective", method = RequestMethod.POST)
    public void someUpdate(Example example) {
        //No-op
    }
}
