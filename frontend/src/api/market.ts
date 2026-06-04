import request from '@/utils/request';

/**
 * 二手交易商品接口定义
 */
export interface GoodsDTO {
  title: string;
  description?: string;
  price: number;
  category?: string;
  images?: string;
}

export interface GoodsVO {
  id: number;
  sellerId: number;
  sellerName: string;
  title: string;
  description: string;
  price: number;
  category: string;
  images: string;
  status: 'ON_SALE' | 'SOLD' | 'OFF_SHELF';
  createTime: string;
}

export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
}

/**
 * 分页查询商品列表
 */
export function listMarketGoods(params: {
  page?: number;
  size?: number;
  keyword?: string;
  category?: string;
}) {
  return request<PageResult<GoodsVO>>({
    url: '/market/goods',
    method: 'get',
    params
  });
}

/**
 * 获取商品详情
 */
export function getGoodsDetail(id: number | string) {
  return request<GoodsVO>({
    url: `/market/goods/${id}`,
    method: 'get'
  });
}

/**
 * 发布商品
 */
export function publishGoods(data: GoodsDTO) {
  return request<void>({
    url: '/market/goods',
    method: 'post',
    data
  });
}

/**
 * 修改商品
 */
export function updateGoods(id: number | string, data: GoodsDTO) {
  return request<void>({
    url: `/market/goods/${id}`,
    method: 'put',
    data
  });
}

/**
 * 删除商品
 */
export function deleteGoods(id: number | string) {
  return request<void>({
    url: `/market/goods/${id}`,
    method: 'delete'
  });
}

/**
 * 修改商品状态
 * @param id 商品ID
 * @param status 状态: ON_SALE, SOLD, OFF_SHELF
 */
export function changeGoodsStatus(id: number | string, status: string) {
  return request<void>({
    url: `/market/goods/${id}/status`,
    method: 'put',
    params: { status }
  });
}
