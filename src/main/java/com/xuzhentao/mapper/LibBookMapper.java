package com.xuzhentao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuzhentao.model.entity.LibBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图书馆藏 Mapper
 */
@Mapper
public interface LibBookMapper extends BaseMapper<LibBook> {
}
