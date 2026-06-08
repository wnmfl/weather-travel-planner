# 数据库设计

## 表结构

### users (用户表)
- id: 用户ID
- username: 用户名
- password: 密码
- email: 邮箱
- created_at: 创建时间

### attractions (景点表)
- id: 景点ID
- name: 景点名称
- city: 所在城市
- price: 门票价格
- rating: 评分
- category: 分类
- latitude/longitude: 地理位置

### routes (路线表)
- id: 路线ID
- user_id: 用户ID
- start_city: 出发城市
- end_city: 目的地
- start_date: 出发日期
- duration: 天数
- weather_score: 天气评分

### orders (订单表)
- id: 订单ID
- user_id: 用户ID
- route_id: 路线ID
- total_price: 总价
- status: 订单状态

## 关系

用户 （1） -------- （N） 条路由 | +--------（N）条命令（1）条----路线 | +--------（N）评论----景点