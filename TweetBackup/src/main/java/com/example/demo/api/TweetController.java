package com.example.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ArchTweet;
import com.example.demo.service.TweetGrabbingService;

//@RequestMapping("api/v1/tweet")
//@RestController
@Controller
public class TweetController {
	TweetGrabbingService tweetGrabbingService;
	
	@Autowired
	public TweetController(TweetGrabbingService tweetGrabbingService) {
		this.tweetGrabbingService = tweetGrabbingService;
	}
	
	@GetMapping
	public String getHomepage(Model model){
		List<ArchTweet> archTweets = this.getArchTweets();
		
		model.addAttribute("Tweet", archTweets);
		//inject list into model
		
		//name of html file
		return "index";
	}
	
	

	public List<ArchTweet> getArchTweets(){
		return tweetGrabbingService.getArchTweets();
	}	

	
	
	
	//more get mappings: one for 
}
