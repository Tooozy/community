package com.toozy.community.mapper;

import com.toozy.community.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-03-26
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

}
