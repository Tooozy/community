package com.toozy.community.service;

import com.toozy.community.dto.PaginationDTO;
import com.toozy.community.dto.QuestionDTO;
import com.toozy.community.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-03-26
 */
public interface QuestionService extends IService<Question> {

    PaginationDTO queryAllQuestionForPage(String search,Integer page, Integer size);

    void doCreateOrUpdate(Question question);

    PaginationDTO queryAllQuestionForId(Integer id, Integer page, Integer size);

    void incView(Integer id);

    QuestionDTO getQuestionDTO(Integer id);

    List<QuestionDTO> selectRelated(QuestionDTO questionDTO);

    PaginationDTO queryQuestionForSearch(String search, Integer page, Integer size);

    List<QuestionDTO> selectPop();
}
