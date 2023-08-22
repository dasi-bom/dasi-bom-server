package com.example.server.global.restdocs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RestDocsController {

	// @GetMapping("/admin/docs")
	// public String restDocsRequest() {
	//     return "redirect:/src/main/resources/static/docs/index.html";
	// }

	@GetMapping("/redirect-to-docs")
	public RedirectView redirectToDocs() {
		return new RedirectView("/docs/index.html");
	}
}

