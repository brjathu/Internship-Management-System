package com.msd.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.msd.pool.PoolApplicants;
import com.msd.pool.PoolCompanies;
import com.msd.pool.PoolPasswords;
import com.msd.registers.LoginInfo;

@Controller
@RequestMapping("admin")
public class AdminController {
	
	@Autowired
	PoolPasswords poolPW;
	@Autowired
	PoolApplicants poolApplicants;
	@Autowired
	PoolCompanies poolCompanies;

	@RequestMapping("/")
	public ModelAndView loginAdmin() {
		ModelAndView loginModel = new ModelAndView();
		loginModel.setViewName("logins/login");
		loginModel.addObject("command", new LoginInfo());
		loginModel.addObject("action_url", new String("log_user"));
		loginModel.addObject("principal", "Admin");
		loginModel.addObject("type", true);
		loginModel.addObject("admin", true);
		return loginModel;
	}

	@RequestMapping(value = "/log_user", method = RequestMethod.POST)
	public String logUserIn(LoginInfo info, ModelMap model, RedirectAttributes redirects) {
		if (poolPW.matchThisAndAdmin(info)) {
			model.addAttribute("response", "Successful!");
		} else {
			redirects.addFlashAttribute("error", "Login Failed!");
			return "redirect:/admin/";
		}
		return "logins/admin_home";
	}
	
	@RequestMapping("/view_users")
	public String showAllUsers(Model model) {
		// Add the user list under "users"
		model.addAttribute("users", poolApplicants.getAllApplicants());
		return "displays/list";
	}
	
	@RequestMapping("/view_companies")
	public String showAllCompanies(Model model) {
		// Add the user list under "users"
		model.addAttribute("companies", poolCompanies.getAllCompanies());
		return "displays/list";
	}
}
