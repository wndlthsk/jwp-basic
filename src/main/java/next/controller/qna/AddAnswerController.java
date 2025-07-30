package next.controller.qna;

import core.mvc.JsonView;
import core.mvc.View;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import core.mvc.Controller;
import next.dao.AnswerDao;
import next.model.Answer;

public class AddAnswerController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(AddAnswerController.class);

    @Override
    public View execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Answer answer = new Answer(req.getParameter("writer"), req.getParameter("contents"),
                Long.parseLong(req.getParameter("questionId")));
        log.debug("answer : {}", answer);

        AnswerDao answerDao = new AnswerDao();
        Answer savedAnswer = answerDao.insert(answer);
        req.setAttribute("answer", savedAnswer);
        // JsonView에서 request에 저장된 attribute들을 다 꺼내서 json으로 만들기 때문에
        // request에다가 미리 추가해둬야한다.
        return new JsonView();
    }
}
