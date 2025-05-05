## **文档作者**

主要编写者：李祥郡

其他编写者：戚俊涵，张铭恺，易飞翔

## **文档修改历史**

| 修改人员 | 日期 | 修改原因         | 版本号 |
| -------- | ---- | ---------------- | ------ |
| 李祥郡   | 4.20 | 初步创建体系架构 | 1.0.0  |

## **目录**

## 1.**引言**

### **1.1 编制目的**

本报告详细完成对 **SBEAM系统** 的概要设计，达到指导详细设计和开发的目的，同时实现和测试人员及用户的沟通。

本报告面向开发人员、测试人员及最终用户而编写，是了解系统的导航。

### **1.2 词汇表**

| 词汇名称 | 词汇含义                           | 备注         |
| -------- | ---------------------------------- | ------------ |
| PO       | 持久化对象（Persistent Object）    | 数据层对象名 |
| VO       | 可视化对象（View Object）          | 数据层对象名 |
| DAO      | 数据访问对象（Data Access Object） |              |

### **1.3 参考资料**

[软件架构的 “4+1” 视图（附视图案例）_4+1视图模型案例-CSDN博客](https://blog.csdn.net/qq_29068607/article/details/143194772)

[什么是Dao层、Entity层、Service层、Servlet层、Utils层？-CSDN博客](https://blog.csdn.net/Restarting2019/article/details/122296373#:~:text=本文介绍了Java开发中常用的五层架构，分别是Dao层、Entity层、Service层、Servlet层、Utils层，以及它们的作用和特点。Dao层是数据访问层，Entity层是实体层，Service层是业务逻辑层，Servlet层是服务器端程序，Utils层是工具类。)

[计算机系统: 进程图解析，全面详细！-CSDN博客](https://blog.csdn.net/m0_72410588/article/details/132768839#:~:text=本文将对进程图进行详细解析，帮助读者深入理解计算机系统中的进程管理。 什么是进程图？ 进程图是一种图形化表示进程之间关系的工具。,通过进程图，我们可以清晰地看到进程的创建、执行和终止过程，以及不同进程之间的交互与通信。 进程图通常由节点和边组成，节点代表进程，边代表进程之间的依赖关系或通信方式。 进程节点：表示一个进程，通常由一个矩形或圆形图形表示。 每个进程节点都有一个唯一的标识符，用于区分不同的进程。)

[16. 面向对象——UML包图（Package Diagram） - 知乎](https://zhuanlan.zhihu.com/p/149275134)

[UML部署图详解-CSDN博客](https://blog.csdn.net/litianxiang_kaola/article/details/53969021)

## 2.产品概述

SBEAM 游戏商城
├─ 一、项目背景与目标
│   ├─ 1. 市场背景：
│   │   ├─ 游戏行业用户规模持续扩大，数字化发行平台需求旺盛
│   │   └─ 社交化、互动化的游戏购买体验成为差异化竞争点
│   └─ 2. 项目目标：
│       ├─ 打造集游戏浏览、购买、评论、抽奖、社交为一体的一站式平台
│       └─ 提升用户活跃度与粘性，实现平台与厂商多方共赢
│
├─ 二、系统定位与价值
│   ├─ 定位：面向广大电竞和休闲玩家的综合数字游戏商城
│   └─ 核心价值：
│       ├─ 丰富的游戏内容与详细的评测资料
│       ├─ 安全便捷的支付与购买流程
│       ├─ 多样化的互动机制（评论、抽奖、社交）
│       └─ 实时聊天增强用户社区氛围
│
├─ 三、核心功能概览
│   ├─ 1. 游戏浏览与详情
│   │   ├─ 分类检索、关键词搜索
│   │   ├─ 游戏封面、截图、视频预览
│   │   └─ 评分与用户评论展示
│   ├─ 2. 评论系统
│   │   ├─ 发布、删除与回复评论
│   │   └─ 评论点赞与举报
│   ├─ 3. 抽奖活动
│   │   ├─ 多种抽奖机制（抽取余额）
│   │   ├─ 活动页面与奖品池管理
│   │   └─ 抽奖结果通知与领奖流程
│   ├─ 4. 购买与支付
│   │   ├─ 购物车与订单管理
│   │   ├─ 多渠道支付（支付宝）
│   │   └─ 订单状态跟踪与售后支持
│   └─ 5. 好友社交
│       ├─ 好友申请与管理
│       ├─ 私信与群聊
│       └─ 在线状态与消息推送
│
└─ 四、主要用户角色
    ├─ 游客：浏览游戏、查看信息
    ├─ 注册用户：评论、抽奖、购买、添加好友、聊天
    └─ 管理员：活动及奖品管理、用户与内容审核

## 3.逻辑视角

体系结构的逻辑视角图

![体系结构的逻辑视角图.drawio](https://s2.loli.net/2025/04/27/RedyUxSt349uTzV.png)

体系结构的逻辑方案图

![体系结构的逻辑设计方案图.drawio](https://s2.loli.net/2025/04/27/Ta9JYVilSN24UHo.png)

## 4.组合视角

### **4.1 开发包图**

体系结构的开发包图

![体系结构的进程图.drawio](https://s2.loli.net/2025/04/27/dZ75V3Qer6PNB8w.png)

### **4.2 运行时进程**

体系结构的进程图

![体系结构的进程图.drawio](https://s2.loli.net/2025/04/27/dZ75V3Qer6PNB8w.png)

### **4.3 物理部署**

体系结构的部署图

![体系结构的部署图.drawio](https://s2.loli.net/2025/04/27/c2OVhNnsxLuzIat.png)

## 5.**接口视角**

### **5.1 模块的职责**

体系结构的接口图

![体系结构的接口视角图.drawio](https://s2.loli.net/2025/04/27/hLsp2D5Jg7KAPIy.png)

### **5.2** **用户界面**层的分解

#### **5.2.1 职责**

1. 提供前端组件用于展示商品，订单，支付，抽奖，好友等等。

2. 发起http请求，与后端进行交互
3. 输入数据校验
4. 提供操作反馈信息

#### **5.2.2 接口规范**

| 接口名称                       | 方法   | 路径                                            | 请求参数         | 请求体                                                       | 响应体                                                       |
| ------------------------------ | ------ | ----------------------------------------------- | ---------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 用户注册                       | POST   | /api/users/register                             | 无               | {id:Integer,name:String,phone:String,password:String,storeId:Integer,address:String,role:RoleEnum,createTime:Date,balance:Double,avatar:String} | {"code":000,"message":"null","data":{id:Integer,name:String,phone:String,password:String,storeId:Integer,address:String,role:RoleEnum,createTime:Date,balance:Double,avatar:String} |
| 用户登录                       | POST   | /api/users/login                                | phone?,password? | 无                                                           | {"code":000,"massage":null,"data":{token:String}}            |
| 获取用户信息                   | GET    | /api/users                                      | 无               | 无                                                           | {"code":000,"message":"null","data":{id:Integer,name:String,phone:String,password:String,storeId:Integer,address:String,role:RoleEnum,createTime:Date,balance:Double,avatar:String} |
| 更新用户信息                   | POST   | /api/users/{userId}                             | 无               | 无                                                           | {"code":000,"message":"null","data":"true"}                  |
| 创建商品                       | POST   | /api/product/createProduct                      | 无               | {productId:Integer,storeId:Integer,productName:String,productAmount:Integer,productPrice:Double,productDescription:String,productImages:List<String>,productLogo:String,productDiscount:Double} | {"code":000,"message":null,"data":"true"}                    |
| 删除商品                       | DELETE | /api/product/deleteProduct/{productId}          | 无               | 无                                                           | {"code":000,"message":null,"data":"true"}                    |
| 获取所有商品                   | GET    | /api/product                                    | 无               | 无                                                           | {"code":000,"message":null,"data":{productId:Integer,storeId:Integer,productName:String,productAmount:Integer,productPrice:Double,productDescription:String,productImages:List<String>,productLogo:String,productDiscount:Double}} |
| 搜索商品                       | GET    | /api/product/search/{productId}                 | 无               | 无                                                           | {"code":000,"message":null,"data":{productId:Integer,storeId:Integer,productName:String,productAmount:Integer,productPrice:Double,productDescription:String,productImages:List<String>,productLogo:String,productDiscount:Double}} |
| 更新商品信息                   | PUT    | /api/product/productDetail/{productId}          | 无               | {productId:Integer,storeId:Integer,productName:String,productAmount:Integer,productPrice:Double,productDescription:String,productImages:List<String>,productLogo:String,productDiscount:Double} | {"code":000,"message":null,"data":"true"}                    |
| 补充商品库存                   | PATCH  | /api/product/productDetail/{productId}          | productAmount?   | 无                                                           | {"code":000,"message":null,"data":"true"}                    |
| 判断购物车里是否有某件商品     | POST   | /api/product/productDetail/{productId}/{cartId} | 无               | 无                                                           | {"code":000,"message":null,"data":"true"}                    |
| 更新购物车里的商品（包括增删） | PUT    | /api/product/productDetail/{productId}/cart     | productAmount?   | {cartId:Integer,userId:Integer,cartItems:List<CartItem>}     | {"code":000,"message":null,"data":"true"}                    |
| 获取购物车里的所有商品         | GET    | /api/user/{userId}/cart                         | 无               | 无                                                           | {"code":000,"message":null,"data":{cartItemId:Integer,productId:Integer,productQuantity:Integer,productPrice:Double}} |
| 生成订单信息                   | PUT    | /api/user/{userId}/cart                         | 无               | {cartId:Integer,userId:Integer,cartItems:List<CartItem>}     | {"code":000,"message":null,"data":{orderId:Integer,userId:Integer,totalPrice:Double,payMethod:String,orderStatus:OrderStatus,createTime:Date,outTradeNo,String}} |
| 支付                           | GET    | /api/alipay/pay                                 | orderId?         | 无                                                           | void                                                         |
| 支付回调                       | POST   | /api/alipay/return                              | 无               | 无                                                           | String                                                       |
| 获取订单信息                   | GET    | /api/orders/{ordersId}                          | 无               | 无                                                           | {"code":000,"message":null,"data":{orderStatus:orderStatus}} |
| 上传图片                       | POST   | /api/tools/images                               | file?            | 无                                                           | {"code":000,"message":null,"data":{String}}                  |
| 创建广告                       | POST   | /api/advertisement                              | 无               | {advertisementId:Integer,advertisementTitle:String,advertisementContent:String,advertisementImageUrl:String,productId:Integer} | {"code":000,"message":null,"data":"true"}                    |
| 删除广告                       | DELETE | /api/advertisement                              | advertisementId? | 无                                                           | {"code":000,"message":null,"data":"true"}                    |
| 获取所有广告                   | GET    | /api/advertisement                              | 无               | 无                                                           | {"code":000,"message":null,"data":{advertisementId:Integer,advertisementTitle:String,advertisementContent:String,advertisementImageUrl:String,productId:Integer}} |
| 更新广告                       | PUT    | /api/advertisement/{advertisementId}            | 无               | {advertisementId:Integer,advertisementTitle:String,advertisementContent:String,advertisementImageUrl:String,productId:Integer} | {"code":000,"message":null,"data":"true"}                    |
|                                |        |                                                 |                  |                                                              |                                                              |
|                                |        |                                                 |                  |                                                              |                                                              |
|                                |        |                                                 |                  |                                                              |                                                              |
|                                |        |                                                 |                  |                                                              |                                                              |
|                                |        |                                                 |                  |                                                              |                                                              |
|                                |        |                                                 |                  |                                                              |                                                              |
|                                |        |                                                 |                  |                                                              |                                                              |
|                                |        |                                                 |                  |                                                              |                                                              |

### **5.3** **业务逻辑层**的分解

#### **5.3.1 职责**

业务逻辑层负责处理核心业务流程、验证、权限校验、事务管理、调用数据层接口并返回结果。

- 实现应用核心流程，如注册、登录、下单、支付。
- 用户权限校验（基于 JWT 或会话）。
- 事务管理（下单与扣库存需保证原子性）。
- 调用数据层接口并组合业务结果。
- 异常处理与日志记录。

#### **5.3.2 接口规范**

```java
public interface UserService {
    Boolean register(UserVO userVO);
    String login(String phone,String password);
    UserVO getInformation();
    Boolean updateInformation(int userId,UserVO userVO);
    void depositBalance(Integer userId, double amount);
}
public interface ProductService {
    Boolean createProduct(ProductVO productVO);
    List<ProductVO> getAllProductsByStoreId(Integer storeId);
    List<ProductVO> getAllProducts();
    ProductVO getProductByProductId(Integer productId);
    Boolean deleteProduct(Integer productId);
    Boolean updateProduct(Integer productId,ProductVO productVO);
    Boolean updateProductAmount(Integer productId,Integer amount);
    Boolean updateProductDiscount(Integer productId,Double productDiscount);
}
public interface CartService {
    Boolean createCart(CartVO cartVO);
    CartVO getCart(int userId);
    Boolean judgeCartItemExist(Integer productId,Integer cartId);
    Boolean updateCart(int productId,int numberOfProduct,CartVO cartVO);
    OrdersVO settleAccount(int userId, CartVO cartVO);
    List<CartItemVO> showAllCartItems(int userId);
    int getCartItemCount(int cartId);
}
public interface OrdersService {
    Boolean createOrders(OrdersVO ordersVO);
    List<OrdersVO> getAllOrders();
    OrdersVO getOrdersById(int orderId);
    Boolean deleteOrdersByOrderId(int orderId);
    OrderStatus getOrderStatus(int ordersId);
}
public interface ImageService {
    String upload(MultipartFile file);
}
public interface AdvertisementService {
    public Boolean createAdvertisement(AdvertisementVO advertisementVO);
    public Boolean deleteAdvertisement(Integer advertisementId);
    public Boolean updateAdvertisement(int advertisementId,AdvertisementVO advertisementVO);
    public List<AdvertisementVO> showAllAdvertisements();
    public AdvertisementVO getAdvertisementByAdvertisementId(Integer advertisementId);
}

```

### **5.4 数据层的分解**

#### **5.4.1 职责**

与数据库交互，实现 CRUD 操作并提供数据持久化服务.

- 定义实体模型（使用 TypeORM 或 Sequelize）。
- 提供 CRUD 接口与数据库交互。
- 支持分页查询与条件筛选。
- 保证数据一致性与性能优化。

#### **5.4.2 接口规范**

```java
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByPhone(String phone);
    User findByPhoneAndPassword(String phone, String password);
    Optional<User> findById(Integer Id);
}
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByProductId(Integer productId);
    List<Product> findAllByStoreId(Integer storeId);
    Product findByProductName(String productName);
}
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByCartId(Integer cartId);
    Cart findByUserId(Integer userId);
}
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findAll();
    Orders findByOrdersId(Integer OrderId);
    Orders findByOutTradeNo(String outTradeNo);
}
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
    Advertisement findByAdvertisementId(int id);
}
```

## 6.**信息视角**

### **6.1 描述数据持久化对象(**PO)

```java
@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "create_time")
    private Date createTime;

    @Basic
    @Column(name = "store_id")
    private Integer storeId;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "balance")
    private Double balance= (double) 0;

    @Basic
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Basic
    @Column(name="avatar")
    private String avatar;
}
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_id")
    private Integer productId;

    @Basic
    @Column(name = "store_id")
    private Integer storeId;

    @Basic
    @Column(name = "product_name")
    private String productName;

    @Basic
    @Column(name = "product_amount")
    private Integer productAmount;

    @Basic
    @Column(name = "product_price")
    private Double productPrice;

    @Basic
    @Column(name = "product_discount")
    private Double productDiscount;

    @Basic
    @Column(name = "product_description", columnDefinition = "TEXT")
    private String productDescription;

    @Basic
    @Column(name = "product_logo")
    private String productLogo;

    @ElementCollection
    @Column(name = "product_images")
    private List<String> productImages;
}
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    @Column(nullable = false)
    private Integer userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name="cart_item")
    private List<CartItem> cartItems;
}
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonIgnore
    private Cart cart;

    @Column(name = "product_id",nullable = false)
    private Integer productId;

    @Column(name = "product_quantity",nullable = false)
    private Integer productQuantity;

    @Column(name = "product_price",nullable = false)
    private Double productPrice;
}
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "orders_id")
    private Integer ordersId;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "total_price")
    private Double totalPrice;

    @Basic
    @Column(name = "pay_method")
    private String payMethod;

    @Basic
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Basic
    @Column(name = "create_time")
    private Date createTime;

    @Basic
    @Column(name = "out_trade_no")
    private String outTradeNo;
}
@Entity
@Table(name = "advertisements")
@Setter
@Getter
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisement_id", nullable = false, columnDefinition = "INT COMMENT '广告id'")
    private Integer advertisementId;

    @Column(name = "advertisement_title", nullable = false, length = 50, columnDefinition = "VARCHAR(50) COMMENT '广告标题，不允许为空'")
    private String advertisementTitle;

    @Column(name = "advertisement_content", nullable = false, length = 500, columnDefinition = "VARCHAR(500) COMMENT '广告内容'")
    private String advertisementContent;

    @Column(name = "advertisement_image_url", nullable = false, length = 500, columnDefinition = "VARCHAR(500) COMMENT '广告图片url'")
    private String advertisementImageUrl;

    @Column(name="product_id", nullable = false)
    private Integer productId;
}
@Data
@Getter
@Setter
public class AliPay {
    private String traceNo;
    private double totalAmount;
    private String subject;
    private String alipayTraceNo;
}
```

### **6.2 数据库表**

| 表名 | 字段名      | 类型         | 是否为空 | 键   | 默认值 | 额外信息       |
| ---- | ----------- | ------------ | -------- | ---- | ------ | -------------- |
| user | id          | int          | NO       | PRI  | NULL   | auto_increment |
| user | address     | varchar(255) | YES      |      | NULL   |                |
| user | avatar      | varchar(255) | YES      |      | NULL   |                |
| user | balance     | double       | YES      |      | NULL   |                |
| user | create_time | datetime(6)  | YES      |      | NULL   |                |
| user | name        | varchar(255) | YES      |      | NULL   |                |
| user | password    | varchar(255) | YES      |      | NULL   |                |
| user | phone       | varchar(255) | YES      |      | NULL   |                |
| user | role        | varchar(255) | YES      |      | NULL   |                |
| user | store_id    | int          | YES      |      | NULL   |                |

| 表名    | 字段名              | 类型         | 是否为空 | 键   | 默认值 | 额外信息       |
| ------- | ------------------- | ------------ | -------- | ---- | ------ | -------------- |
| product | product_id          | int          | NO       | PRI  | NULL   | auto_increment |
| product | product_amount      | int          | YES      |      | NULL   |                |
| product | product_description | text         | YES      |      | NULL   |                |
| product | product_discount    | double       | YES      |      | NULL   |                |
| product | product_logo        | varchar(255) | YES      |      | NULL   |                |
| product | product_name        | varchar(255) | YES      |      | NULL   |                |
| product | product_price       | double       | YES      |      | NULL   |                |
| product | store_id            | int          | YES      |      | NULL   |                |

| 表名                   | 字段名             | 类型         | 是否为空 | 键   | 默认值 | 额外信息 |
| ---------------------- | ------------------ | ------------ | -------- | ---- | ------ | -------- |
| product_product_images | product_product_id | int          | NO       | MUL  | NULL   |          |
| product_product_images | product_images     | varchar(255) | YES      |      | NULL   |          |

| 表名 | 字段名  | 类型 | 是否为空 | 键   | 默认值 | 额外信息       |
| ---- | ------- | ---- | -------- | ---- | ------ | -------------- |
| cart | cart_id | int  | NO       | PRI  | NULL   | auto_increment |
| cart | user_id | int  | NO       |      | NULL   |                |

| 表名      | 字段名           | 类型   | 是否为空 | 键   | 默认值 | 额外信息       |
| --------- | ---------------- | ------ | -------- | ---- | ------ | -------------- |
| cart_item | cart_item_id     | int    | NO       | PRI  | NULL   | auto_increment |
| cart_item | product_id       | int    | NO       |      | NULL   |                |
| cart_item | product_price    | double | NO       |      | NULL   |                |
| cart_item | product_quantity | int    | NO       |      | NULL   |                |
| cart_item | cart_id          | int    | NO       | MUL  | NULL   |                |

| 表名   | 字段名       | 类型         | 是否为空 | 键   | 默认值 | 额外信息       |
| ------ | ------------ | ------------ | -------- | ---- | ------ | -------------- |
| orders | orders_id    | int          | NO       | PRI  | NULL   | auto_increment |
| orders | create_time  | datetime(6)  | YES      |      | NULL   |                |
| orders | order_status | varchar(255) | YES      |      | NULL   |                |
| orders | out_trade_no | varchar(255) | YES      |      | NULL   |                |
| orders | pay_method   | varchar(255) | YES      |      | NULL   |                |
| orders | total_price  | double       | YES      |      | NULL   |                |
| orders | user_id      | int          | YES      |      | NULL   |                |

| 表名           | 字段名                  | 类型         | 是否为空 | 键   | 默认值 | 额外信息       |
| -------------- | ----------------------- | ------------ | -------- | ---- | ------ | -------------- |
| advertisements | advertisement_id        | int          | NO       | PRI  | NULL   | auto_increment |
| advertisements | advertisement_content   | varchar(500) | NO       |      | NULL   |                |
| advertisements | advertisement_image_url | varchar(500) | NO       |      | NULL   |                |
| advertisements | advertisement_title     | varchar(50)  | NO       |      | NULL   |                |
| advertisements | product_id              | int          | NO       | MUL  | NULL   |                |