package com.xuzhentao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuzhentao.model.dto.GoodsDTO;
import com.xuzhentao.model.vo.GoodsVO;

/**
 * 二手交易商品服务接口
 */
public interface MarketGoodsService {

    /** 发布商品 */
    void publish(Long sellerId, GoodsDTO dto);

    /** 分页查询商品 */
    Page<GoodsVO> listGoods(Integer page, Integer size, String keyword, String category);

    /** 获取商品详情 */
    GoodsVO getGoodsDetail(Long id);

    /** 修改商品信息（仅卖家） */
    void updateGoods(Long userId, Long goodsId, GoodsDTO dto);

    /** 逻辑删除（仅卖家） */
    void deleteGoods(Long userId, Long goodsId);

    /** 修改商品状态（上下架/售出） */
    void changeStatus(Long userId, Long goodsId, String status);
}
