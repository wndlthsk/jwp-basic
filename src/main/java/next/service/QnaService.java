package next.service;

import java.util.List;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;

public class QnaService {
    private static QnaService qnaService;

    private QuestionDao questionDao;
    private AnswerDao answerDao;

    private QnaService() {
        this.questionDao = QuestionDao.getInstance();
        this.answerDao = AnswerDao.getInstance();
    }

    public QnaService(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    public static QnaService getInstance() {
        if (qnaService == null) {
            qnaService = new QnaService();
        }
        return qnaService;
    }

    public Question findById(long questionId) {
        return questionDao.findById(questionId);
    }

    public List<Answer> findAllByQuestionId(long questionId) {
        return answerDao.findAllByQuestionId(questionId);
    }

    public void deleteQuestion(long questionId, User user) throws IllegalArgumentException {
        // 질문 정보 가져오기
        Question question = questionDao.findById(questionId);

        if (question == null) {
            throw new IllegalArgumentException("존재하지 않는 질문입니다.");
        }

        // 질문 작성자랑 로그인한 유저 같은지 확인
        if (!question.isSameUser(user)) {
            throw new IllegalArgumentException("다른 사용자가 쓴 글을 삭제할 수 없습니다.");
        }

        // 질문에 달린 답변들 가져오기
        List<Answer> answers = answerDao.findAllByQuestionId(questionId);
        // 답변이 없으면 바로 삭제
        if (answers.isEmpty()) {
            questionDao.delete(questionId);
            return;
        }

        // 답변 작성자가 질문 작성자랑 같은지 확인
        boolean canDelete = true;
        for (Answer answer : answers) {
            String writer = question.getWriter();
            if (!writer.equals(answer.getWriter())) {
                canDelete = false;
                break;
            }
        }

        if (!canDelete) {
            throw new IllegalArgumentException("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
        }

        // 모든 답변의 작성자가 질문자랑 같으면 삭제
        questionDao.delete(questionId);
    }
}