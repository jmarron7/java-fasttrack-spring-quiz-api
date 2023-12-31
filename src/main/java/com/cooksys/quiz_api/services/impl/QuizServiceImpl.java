package com.cooksys.quiz_api.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.mappers.QuestionMapper;
import com.cooksys.quiz_api.mappers.QuizMapper;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.repositories.QuizRepository;
import com.cooksys.quiz_api.services.QuizService;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

  private final QuizRepository quizRepository;
  private final QuizMapper quizMapper;
  private final QuestionRepository questionRepository;
  private final QuestionMapper questionMapper;

  @Override
  public List<QuizResponseDto> getAllQuizzes() {
    return quizMapper.entitiesToDtos(quizRepository.findAll());
  }

  private Quiz getQuiz(Long id) throws NotFoundException {
    Optional<Quiz> optionalQuiz = quizRepository.findById(id);

    if (optionalQuiz.isEmpty()) {
      throw new NotFoundException("Quiz with id { " + id + " } not found.");
    }
    return optionalQuiz.get();
  }

  private Question getQuestion(Long id, Long questionID) throws NotFoundException {
    Optional<Question> optionalQuestion = questionRepository.findByIdAndQuizId(questionID, id);

    if (optionalQuestion.isEmpty()) {
      throw new NotFoundException("Question with id { " + questionID + " } not found.");
    }
    return optionalQuestion.get();
  }

  @Override
  public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
    Quiz quiz = quizMapper.requestEntity(quizRequestDto);

    for (Question question : quiz.getQuestions()) {
      question.setQuiz(quiz);

      for (Answer answer : question.getAnswers()) {
        answer.setQuestion(question);
      }
    }

    Quiz result = quizRepository.saveAndFlush(quiz);
    return quizMapper.entityToDto(result);
  }

  @Override
  public QuizResponseDto deleteQuiz(Long id) throws NotFoundException {
    Quiz result = getQuiz(id);
    quizRepository.delete(result);
    return quizMapper.entityToDto(result);
  }

  @Override
  public QuizResponseDto renameQuiz(Long id, String name) throws NotFoundException {
    Quiz result = getQuiz(id);
    result.setName(name);
    return quizMapper.entityToDto(quizRepository.saveAndFlush(result));
  }

  @Override
  public QuestionResponseDto getQuizQuestion(Long id) throws NotFoundException {
    Quiz result = getQuiz(id);
    Object[] questions = result.getQuestions().toArray();
    Question rand = (Question) questions[new Random().nextInt(result.getQuestions().size())];
    return questionMapper.entityToDto(rand);
  }

  @Override
  public QuizResponseDto addQuestion(Long id, QuestionRequestDto questionRequestDto) throws NotFoundException {
    Quiz quiz = getQuiz(id);

    for (Question question : quiz.getQuestions()) {
      question.setQuiz(quiz);
      question.setText(questionRequestDto.getText());

      for (Answer answer : question.getAnswers()) {
        answer.setQuestion(question);
      }
    }

    quiz.getQuestions().add(questionMapper.questionDtoToEntity(questionRequestDto));
    return quizMapper.entityToDto(quizRepository.saveAndFlush(quiz));
  }

  @Override
  public QuestionResponseDto deleteQuestion(Long id, Long questionID) throws NotFoundException {
    Question question = getQuestion(id, questionID);
    questionRepository.delete(question);
    return questionMapper.entityToDto(question);
  }
}
