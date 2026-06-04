package com.xuzhentao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuzhentao.model.dto.BookDTO;
import com.xuzhentao.model.vo.BookVO;

/**
 * 图书管理服务接口
 */
public interface LibBookService {

    /** 分页检索图书（支持 ISBN/书名/作者/出版社） */
    Page<BookVO> searchBooks(Integer page, Integer size, String keyword);

    /** 获取图书详情 */
    BookVO getBookDetail(Long id);

    /** 新增馆藏（管理员） */
    void addBook(BookDTO dto);

    /** 更新馆藏（管理员） */
    void updateBook(Long id, BookDTO dto);
}
