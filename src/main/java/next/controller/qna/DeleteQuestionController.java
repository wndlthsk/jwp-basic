package next.controller.qna;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.model.User;
import next.service.QnaService;

public class DeleteQuestionController extends AbstractController {
    private QnaService qnaService = QnaService.getInstance();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        long questionId = Long.parseLong(req.getParameter("questionId"));
        // 로그인 유저 정보 가져오기
        User user = UserSessionUtils.getUserFromSession(req.getSession());
        qnaService.deleteQuestion(questionId, user);
        return jspView("redirect:/");
    }
}