package next.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {
    @Mock
    private QuestionDao questionDao;
    @Mock
    private AnswerDao answerDao;

    private QnaService qnaService;

    private User user;
    private Question question;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        qnaService = new QnaService(questionDao, answerDao);

        user = new User("1","pw", "name", "email@email");
        question = mock(Question.class);
        when(question.isSameUser(user)).thenReturn(true);
        when(question.getWriter()).thenReturn("writer");
    }

    @Test
    public void deleteQuestion_shouldDeleteWhenNoAnswers() {
        when(questionDao.findById(1L)).thenReturn(question);
        when(answerDao.findAllByQuestionId(1L)).thenReturn(Collections.emptyList());

        qnaService.deleteQuestion(1L, user);

        verify(questionDao).delete(1L);
    }
}
