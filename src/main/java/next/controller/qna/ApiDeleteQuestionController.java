package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import next.controller.UserSessionUtils;
import next.model.Result;
import next.model.User;
import next.service.QnaService;

public class ApiDeleteQuestionController extends AbstractController {
    private QnaService qnaService;

    // 기본 생성자 (기존 코드 호환용)
    public ApiDeleteQuestionController() {
        this.qnaService = QnaService.getInstance();
    }

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return jsonView().addObject("result", Result.fail("Login is required"));
        }

        long questionId = Long.parseLong(req.getParameter("questionId"));
        // 로그인 유저 정보 가져오기
        User user = UserSessionUtils.getUserFromSession(req.getSession());
        qnaService.deleteQuestion(questionId, user);
        return jsonView().addObject("result", Result.ok());
    }
}