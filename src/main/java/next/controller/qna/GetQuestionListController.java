package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;

public class GetQuestionListController extends AbstractController {

    QuestionDao questionDao = new QuestionDao();

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Question> questions = questionDao.findAll();

        return jsonView().addObject("questions", questions);
    }
}
