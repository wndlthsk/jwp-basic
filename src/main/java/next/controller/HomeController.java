package next.controller;

import core.mvc.AbstractController;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.View;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.Controller;
import next.dao.QuestionDao;

public class HomeController extends AbstractController {
    private final QuestionDao questionDao = new QuestionDao();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return jspView("home.jsp").addObject("questions", questionDao.findAll());
    }
}
