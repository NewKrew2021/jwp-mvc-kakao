package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import core.mvc.view.DefaultView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UsersFormController {

    @RequestMapping(value = "/users/form")
    public ModelAndView usersForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return new ModelAndView(new DefaultView("/user/form.jsp"));
    }

    @RequestMapping(value = "/users/loginForm")
    public ModelAndView usersLoginForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return new ModelAndView(new DefaultView("/user/login.jsp"));
    }
}
