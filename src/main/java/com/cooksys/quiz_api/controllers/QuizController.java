package com.cooksys.quiz_api.controllers;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.services.QuizService;

import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

  private final QuizService quizService;

  @GetMapping
  public List<QuizResponseDto> getAllQuizzes() {
    return quizService.getAllQuizzes();
  }
  
  // TODO: Implement the remaining 6 endpoints from the documentation.

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public QuizResponseDto createQuiz(@RequestBody QuizRequestDto quizRequestDto) {
    return quizService.createQuiz();
  }

  @DeleteMapping("/{id}")
  public QuizResponseDto deleteQuiz(@PathVariable Long id) {
    return quizService.deleteQuiz(id);
  }

  @PatchMapping("/{id}/rename/{newName}")
  public QuizResponseDto renameQuiz(@PathVariable Long id, @PathVariable String newName) throws NotFoundException {
    return quizService.renameQuiz(id, newName);
  }

  @GetMapping("/{id}/random")
  public QuestionResponseDto getQuizQuestion(@PathVariable Long id) throws NotFoundException {
    return quizService.getQuizQuestion(id);
  }

  @PatchMapping("/{id}/add")
  public QuizResponseDto addQuestion(@PathVariable Long id, @RequestBody QuestionRequestDto questionRequestDto) throws NotFoundException {
    return quizService.addQuestion(id, questionRequestDto);
  }

  @DeleteMapping("/{id}/delete/{questionID}")
  public QuestionResponseDto deleteQuestion(@PathVariable Long id, @PathVariable Long questionID) throws NotFoundException {
    return quizService.deleteQuestion(id, questionID);
  }
}
