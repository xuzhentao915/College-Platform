package com.xuzhentao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuzhentao.exception.BusinessException;
import com.xuzhentao.mapper.LibBookMapper;
import com.xuzhentao.model.dto.BookDTO;
import com.xuzhentao.model.entity.LibBook;
import com.xuzhentao.model.vo.BookVO;
import com.xuzhentao.service.LibBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 图书管理服务实现
 */
@Service
@RequiredArgsConstructor
public class LibBookServiceImpl implements LibBookService {

    private final LibBookMapper libBookMapper;

    @Override
    public Page<BookVO> searchBooks(Integer page, Integer size, String keyword) {
        Page<LibBook> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<LibBook> query = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            query.and(w -> w
                    .like(LibBook::getIsbn, keyword)
                    .or().like(LibBook::getTitle, keyword)
                    .or().like(LibBook::getAuthor, keyword)
                    .or().like(LibBook::getPublisher, keyword));
        }
        query.orderByDesc(LibBook::getCreateTime);

        Page<LibBook> result = libBookMapper.selectPage(pageParam, query);
        Page<BookVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::toBookVO).toList());
        return voPage;
    }

    @Override
    public BookVO getBookDetail(Long id) {
        LibBook book = Optional.ofNullable(libBookMapper.selectById(id))
                .orElseThrow(() -> new BusinessException("图书不存在"));
        return toBookVO(book);
    }

    @Override
    public void addBook(BookDTO dto) {
        LibBook book = LibBook.builder()
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publisher(dto.getPublisher())
                .totalStock(dto.getTotalStock())
                .currentStock(dto.getCurrentStock())
                .location(dto.getLocation())
                .build();
        libBookMapper.insert(book);
    }

    @Override
    public void updateBook(Long id, BookDTO dto) {
        LibBook book = Optional.ofNullable(libBookMapper.selectById(id))
                .orElseThrow(() -> new BusinessException("图书不存在"));
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublisher(dto.getPublisher());
        book.setTotalStock(dto.getTotalStock());
        book.setCurrentStock(dto.getCurrentStock());
        book.setLocation(dto.getLocation());
        libBookMapper.updateById(book);
    }

    private BookVO toBookVO(LibBook book) {
        return BookVO.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .totalStock(book.getTotalStock())
                .currentStock(book.getCurrentStock())
                .location(book.getLocation())
                .build();
    }
}
