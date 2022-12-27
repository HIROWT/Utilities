package com.example.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.ArchTweet;
import com.example.demo.model.ArchTweet2;
import com.example.demo.model.PassForm;
import com.example.demo.service.ForgottenPostingService;
import com.example.demo.service.TweetGrabbingService;
//@RequestMapping("api/v1/tweet")
//@RestController
@Controller
public class TweetController {
	TweetGrabbingService tweetGrabbingService;
	ForgottenPostingService forgottenPostingService;
	
	@Autowired
	public TweetController(TweetGrabbingService tweetGrabbingService, ForgottenPostingService forgottenPostingService) {
		this.tweetGrabbingService = tweetGrabbingService;
		this.forgottenPostingService = forgottenPostingService;
	}
	
	@GetMapping
	public String getHomepage(Model model){
		List<ArchTweet> archTweets = this.getArchTweets();
		
		model.addAttribute("Tweet", archTweets);
		model.addAttribute("passForm", new PassForm("",""));
		//inject list into model
		
		//name of html file
		return "index";
	}
	

	public List<ArchTweet> getArchTweets(){
		return tweetGrabbingService.getArchTweets();
	}	
	
	@GetMapping(path = "/forgotten")
	public String getForgottenTweets(Model model){

		model.addAttribute("Tweet", forgottenPostingService.getForgottenTweets());
		model.addAttribute("passForm", new PassForm("",""));
		//inject list into model
		
		//name of html file
		return "index";  
		    
	}
	
	@GetMapping(path = "/about")
	public String getAbout() {
		return "about";
	}
	  
	@GetMapping(path = "/test")
	public String getTest() { 
		return "testpg";
	}
	
	@GetMapping(path = "/addtweet")
	public ModelAndView getEnc() {
		return new ModelAndView("insert","passForm", new PassForm(null, null));
	}
	//admin panel for emergency manual entry
	@PostMapping("/addt")
	public ModelAndView getEc(@ModelAttribute("passForm") PassForm pass) {
		if(pass.getId().equals("")) {
			return new ModelAndView("insert2","archTweet", new ArchTweet("","","",""));
		}
		else {
			return new ModelAndView("insert","passForm", new PassForm(null, null));
		}
	}
	
	@PostMapping("/addtt")
	public ModelAndView insertTweet(@ModelAttribute("archTweet") ArchTweet2 archTweet) {
		tweetGrabbingService.insertTweet(new ArchTweet(archTweet.tweetText, archTweet.tweetId, archTweet.createdAt, archTweet.quotedTweet));
		return new ModelAndView("insert2","archTweet", new ArchTweet2("","","",""));
	}
	
	@PostMapping("/search")
	public ModelAndView searchTweet(@ModelAttribute("passForm") PassForm passForm) {
		List<ArchTweet> archTweets = tweetGrabbingService.searchTweets(passForm.getKey());
		return new ModelAndView("index", "Tweet", archTweets);
	}
	
}
