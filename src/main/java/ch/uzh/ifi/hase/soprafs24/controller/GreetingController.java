package ch.uzh.ifi.hase.soprafs24.controller;

import org.springframework.stereotype.Controller;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;


@Controller
public class GreetingController {

	@MessageMapping("/live")
	@SendTo("/topic/greetings")
	public String handle(String greeting) {
		System.out.println("Hello from Java!");
		return "[" + getTimestamp() + "] " + greeting;
	}

	private String getTimestamp() {
		return new SimpleDateFormat("MM/dd/yyyy h:mm:ss a").format(new Date());
	}

}