package com.xuzhentao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuzhentao.exception.BusinessException;
import com.xuzhentao.mapper.MarketGoodsMapper;
import com.xuzhentao.mapper.SysUserMapper;
import com.xuzhentao.model.dto.GoodsDTO;
import com.xuzhentao.model.entity.MarketGoods;
import com.xuzhentao.model.entity.SysUser;
import com.xuzhentao.model.vo.GoodsVO;
import com.xuzhentao.service.MarketGoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 二手交易商品服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketGoodsServiceImpl implements MarketGoodsService {

    private final MarketGoodsMapper marketGoodsMapper;
    private final SysUserMapper sysUserMapper;

    @Override
    public void publish(Long sellerId, GoodsDTO dto) {
        MarketGoods goods = MarketGoods.builder()
                .sellerId(sellerId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .images(dto.getImages())
                .status("ON_SALE")
                .build();
        marketGoodsMapper.insert(goods);
        log.info("商品发布成功: id={}, sellerId={}", goods.getId(), sellerId);
    }

    @Override
    public Page<GoodsVO> listGoods(Integer page, Integer size, String keyword, String category) {
        Page<MarketGoods> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<MarketGoods> query = new LambdaQueryWrapper<MarketGoods>()
                .like(StringUtils.hasText(keyword), MarketGoods::getTitle, keyword)
                .eq(StringUtils.hasText(category), MarketGoods::getCategory, category)
                .eq(MarketGoods::getStatus, "ON_SALE")
                .orderByDesc(MarketGoods::getCreateTime);

        Page<MarketGoods> result = marketGoodsMapper.selectPage(pageParam, query);
        Page<GoodsVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::toGoodsVO).toList());
        return voPage;
    }

    @Override
    public GoodsVO getGoodsDetail(Long id) {
        MarketGoods goods = Optional.ofNullable(marketGoodsMapper.selectById(id))
                .orElseThrow(() -> new BusinessException("商品不存在"));
        return toGoodsVO(goods);
    }

    @Override
    public void updateGoods(Long userId, Long goodsId, GoodsDTO dto) {
        MarketGoods goods = getAndCheckOwnership(userId, goodsId);
        goods.setTitle(dto.getTitle());
        goods.setDescription(dto.getDescription());
        goods.setPrice(dto.getPrice());
        goods.setCategory(dto.getCategory());
        goods.setImages(dto.getImages());
        marketGoodsMapper.updateById(goods);
    }

    @Override
    public void deleteGoods(Long userId, Long goodsId) {
        getAndCheckOwnership(userId, goodsId);
        marketGoodsMapper.deleteById(goodsId);
    }

    @Override
    public void changeStatus(Long userId, Long goodsId, String status) {
        MarketGoods goods = getAndCheckOwnership(userId, goodsId);
        goods.setStatus(status);
        marketGoodsMapper.updateById(goods);
    }

    private MarketGoods getAndCheckOwnership(Long userId, Long goodsId) {
        MarketGoods goods = Optional.ofNullable(marketGoodsMapper.selectById(goodsId))
                .orElseThrow(() -> new BusinessException("商品不存在"));
        if (!goods.getSellerId().equals(userId)) {
            throw new BusinessException(403, "无权操作他人商品");
        }
        return goods;
    }

    private GoodsVO toGoodsVO(MarketGoods goods) {
        String sellerName = Optional.ofNullable(sysUserMapper.selectById(goods.getSellerId()))
                .map(SysUser::getName).orElse("未知用户");
        return GoodsVO.builder()
                .id(goods.getId())
                .sellerId(goods.getSellerId())
                .sellerName(sellerName)
                .title(goods.getTitle())
                .description(goods.getDescription())
                .price(goods.getPrice())
                .category(goods.getCategory())
                .images(goods.getImages())
                .status(goods.getStatus())
                .createTime(goods.getCreateTime())
                .build();
    }
}
