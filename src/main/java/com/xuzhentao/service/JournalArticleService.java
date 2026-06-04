package com.xuzhentao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuzhentao.model.dto.JournalDTO;
import com.xuzhentao.model.vo.JournalVO;

/**
 * 学术期刊服务接口
 */
public interface JournalArticleService {

    /** 分页浏览文献 */
    Page<JournalVO> listArticles(Integer page, Integer size, String keyword);

    /** 获取文献详情 */
    JournalVO getArticleDetail(Long id);

    /** 新增文献 */
    void addArticle(JournalDTO dto);
}
