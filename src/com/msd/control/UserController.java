package com.msd.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.msd.pool.PoolApplicants;
import com.msd.pool.PoolPasswords;
import com.msd.registers.LoginInfo;
import com.msd.users.Applicant;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	PoolPasswords poolPW;
	@Autowired
	PoolApplicants poolApplicants;

	// This view will display the correctness of the user credentials
	@RequestMapping(value = "/log", method = RequestMethod.POST, params = "login")
	public String logUserIn(@ModelAttribute("command") LoginInfo info, ModelMap model, RedirectAttributes redirects) {
		if (poolPW.matchThisAndThat(info)) {
			Applicant user = poolApplicants.fetchApplicant(info.getUsername());
			model.addAttribute("user", user);
		} else {
			redirects.addFlashAttribute("error", "Username or Password is wrong!");
			return "redirect:/user_login";
		}
		return "displays/show_user";
	}

	// This view will direct to the register view
	@RequestMapping(value = "/log", method = RequestMethod.POST, params = "register")
	public String register(LoginInfo info, ModelMap model, RedirectAttributes redirects) {
		poolPW.addPassword(info);
		return "redirect:/reg/user/";
	}

	// This will display all the users and their passwords <REMOVE>
	@RequestMapping("/user_list")
	public ModelAndView loadUsersList() {
		List<LoginInfo> list = poolPW.listTypeOfPWs(false);
		return new ModelAndView("displays/user_list", "list", list);
	}

	// This will delete the current password
	@RequestMapping(value = "/deletepw/{name}", method = RequestMethod.GET)
	public String delete(@PathVariable String name) {
		poolPW.deletePassword(name);
		return ("redirect:/user/user_list");
	}
}
