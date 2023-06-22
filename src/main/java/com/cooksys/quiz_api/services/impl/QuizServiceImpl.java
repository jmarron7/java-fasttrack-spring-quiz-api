package com.cooksys.quiz_api.services.impl;

import java.util.List;
import java.util.Optional;

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
      throw new NotFoundException("Quiz not found with id: " + id);
    }
    return optionalQuiz.get();
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
    return null;
  }

  @Override
  public QuestionResponseDto getQuizQuestion(Long id) throws NotFoundException {
    return null;
  }

  @Override
  public QuizResponseDto addQuestion(Long id, QuestionRequestDto questionRequestDto) throws NotFoundException {
    return null;
  }

  @Override
  public QuestionResponseDto deleteQuestion(Long id, Long questionID) throws NotFoundException {
    return null;
  }

}
