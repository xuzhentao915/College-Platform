package com.xuzhentao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuzhentao.exception.BusinessException;
import com.xuzhentao.mapper.JournalArticleMapper;
import com.xuzhentao.model.dto.JournalDTO;
import com.xuzhentao.model.entity.JournalArticle;
import com.xuzhentao.model.vo.JournalVO;
import com.xuzhentao.service.JournalArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 学术期刊服务实现
 */
@Service
@RequiredArgsConstructor
public class JournalArticleServiceImpl implements JournalArticleService {

    private final JournalArticleMapper journalArticleMapper;

    @Override
    public Page<JournalVO> listArticles(Integer page, Integer size, String keyword) {
        Page<JournalArticle> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<JournalArticle> query = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            query.and(w -> w
                    .like(JournalArticle::getTitle, keyword)
                    .or().like(JournalArticle::getAuthors, keyword));
        }
        query.orderByDesc(JournalArticle::getPublishDate);

        Page<JournalArticle> result = journalArticleMapper.selectPage(pageParam, query);
        Page<JournalVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::toJournalVO).toList());
        return voPage;
    }

    @Override
    public JournalVO getArticleDetail(Long id) {
        JournalArticle article = Optional.ofNullable(journalArticleMapper.selectById(id))
                .orElseThrow(() -> new BusinessException("文献不存在"));
        return toJournalVO(article);
    }

    @Override
    public void addArticle(JournalDTO dto) {
        JournalArticle article = JournalArticle.builder()
                .title(dto.getTitle())
                .authors(dto.getAuthors())
                .abstractText(dto.getAbstractText())
                .sourceUrl(dto.getSourceUrl())
                .publishDate(dto.getPublishDate())
                .build();
        journalArticleMapper.insert(article);
    }

    private JournalVO toJournalVO(JournalArticle article) {
        return JournalVO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .authors(article.getAuthors())
                .abstractText(article.getAbstractText())
                .sourceUrl(article.getSourceUrl())
                .publishDate(article.getPublishDate())
                .createTime(article.getCreateTime())
                .build();
    }
}
