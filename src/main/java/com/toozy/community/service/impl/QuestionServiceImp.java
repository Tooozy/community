package com.toozy.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toozy.community.dto.PaginationDTO;
import com.toozy.community.dto.QuestionDTO;
import com.toozy.community.entity.Question;
import com.toozy.community.entity.User;
import com.toozy.community.mapper.QuestionMapper;
import com.toozy.community.mapper.UserMapper;
import com.toozy.community.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-03-26
 */
@Slf4j
@Service
public class QuestionServiceImp extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public PaginationDTO queryAllQuestionForPage(String search,Integer pageNo, Integer pageSize) {
        Page<Question> page = new Page<>(pageNo,pageSize);

        questionMapper.selectPage(page, new QueryWrapper<Question>().orderByDesc("gmt_modified"));

        List<Question> questionList = page.getRecords();
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {
            User user = userMapper.selectById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setData(questionDTOList);
        Integer totalPage = 0;
        Long aLong = questionMapper.selectCount(new QueryWrapper<>());
        Integer total = aLong.intValue();
        if (total%pageSize == 0){
            totalPage = total/pageSize;
        }else {
            totalPage = total/pageSize +1;
        }
        paginationDTO.setPagination(totalPage,pageNo);

        return paginationDTO;
    }

    @Override
    public void doCreateOrUpdate(Question question) {

        if (question.getId() == null){
            questionMapper.insert(question);
            log.info("问题插入成功问题插入成功问题插入成功问题插入成功问题插入成功问题插入成功问题插入成功");
        }else {
            questionMapper.updateById(question);
            log.info("问题更新成功");
        }
    }

    @Override
    public PaginationDTO queryAllQuestionForId(Integer id, Integer pageNo, Integer pageSize) {
        Page<Question> page = new Page<>(pageNo,pageSize);
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator",id).orderByDesc("gmt_create");
        questionMapper.selectPage(page, queryWrapper);

        List<Question> questionList = page.getRecords();
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {
            User user = userMapper.selectById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setData(questionDTOList);
        Integer totalPage = 0;
        Long aLong = questionMapper.selectCount(new QueryWrapper<>());
        Integer total = aLong.intValue();

        if (total%pageSize == 0){
            totalPage = total/pageSize;
        }else {
            totalPage = total/pageSize +1;
        }
        paginationDTO.setPagination(totalPage,pageNo);

        return paginationDTO;
    }

    @Override
    public void incView(Integer id) {
        Question question = questionMapper.selectById(id);
        question.setViewCount(question.getViewCount()+1);
        questionMapper.updateById(question);
    }

    @Override
    public QuestionDTO getQuestionDTO(Integer id) {
        Question question = questionMapper.selectById(id);
        User user = userMapper.selectById(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);


        return questionDTO;
    }

    @Override
    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        String[] splitTags = StringUtils.split(questionDTO.getTag(), ",");
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.and(questionQueryWrapper1 -> {
            for (String splitTag : splitTags) {
                questionQueryWrapper1.or().
                like("tag",splitTag)
                .ne("id",questionDTO.getId());
            }
        });
        List<Question> questions = questionMapper.selectList(questionQueryWrapper);


        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            QuestionDTO questionDTOInFor = new QuestionDTO();
            User user = userMapper.selectById(question.getCreator());
            BeanUtils.copyProperties(question,questionDTOInFor);
            questionDTOInFor.setUser(user);
            questionDTOList.add(questionDTOInFor);
        }


        return questionDTOList;
    }

    @Override
    public PaginationDTO queryQuestionForSearch(String search, Integer pageNo, Integer pageSize) {
        String[] splitTags = StringUtils.split(search, " ");
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.and(questionQueryWrapperForSearch -> {
            for (String splitTag : splitTags) {
                questionQueryWrapperForSearch.or().
                        like("title",splitTag);
            }
        });


        Page<Question> page = new Page<>(pageNo,pageSize);
        questionMapper.selectPage(page, questionQueryWrapper);
        List<Question> questionList = page.getRecords();

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            User user = userMapper.selectById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setData(questionDTOList);
        Integer totalPage = 0;
        Long aLong = questionMapper.selectCount(questionQueryWrapper);
        Integer total = aLong.intValue();
        if (total%pageSize == 0){
            totalPage = total/pageSize;
        }else {
            totalPage = total/pageSize +1;
        }
        paginationDTO.setPagination(totalPage,pageNo);

        return paginationDTO;
    }

    @Override
    public List<QuestionDTO> selectPop() {
        List<Question> questions = questionMapper.selectList(null);
        Long aLong = questionMapper.selectCount(null);
        Integer i = aLong.intValue();

        Double totalCommentCount = 0d;
        Double totalViewCount = 0d;

        for (Question question : questions) {
            totalCommentCount = totalCommentCount+question.getCommentCount();
            totalViewCount = totalViewCount+question.getViewCount();
        }
        Double meanCommentCount = totalCommentCount/i;
        Double meanViewCount = totalViewCount/i;

        for (Question question : questions) {
            Double d = ((question.getCommentCount()/meanCommentCount) + (question.getViewCount()/meanViewCount));
            question.setLikeCount(d.intValue());
            questionMapper.updateById(question);
        }

        List<Question> questionsFinal = questionMapper.selectList(new QueryWrapper<Question>().orderByDesc("like_count"));

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionsFinal) {
            QuestionDTO questionDTO = new QuestionDTO();
            User user = userMapper.selectById(question.getCreator());
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }


        return questionDTOList;
    }


}
