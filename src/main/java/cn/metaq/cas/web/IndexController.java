package cn.metaq.cas.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @Value("${cas.logout-url}")
    private String clientLogoutUrl;
    public final static String CAS = "_const_cas_assertion_";

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model, HttpServletRequest request) {
        Object object = request.getSession().getAttribute(CAS);
        if (null == object) {
            return null;
        }

        Assertion assertion = (Assertion) object;
        String username = assertion.getPrincipal().getName();
        model.addAttribute("username", username);
        return "index";
    }

    @RequestMapping(value = "/accessDenied", method = {RequestMethod.GET, RequestMethod.POST})
    public String accessDenied() {
        return "access_denied";
    }

    @GetMapping(path = "/logout")
    public String logout(HttpSession session, HttpServletRequest request) throws ServletException {
        session.invalidate();//销毁session
        System.out.println("url = " + request.getRequestURI());
        System.out.println("url2 = " + request.getRequestURL().toString());
        return "redirect:" + clientLogoutUrl;
    }

    @RequestMapping("logout/success")
    public String logoutsuccess(HttpSession session) {
        return "logout";
    }
}
