create table T_ADDRESS
(
    ID             varchar(32)       not null comment '唯一键'
        primary key,
    USER_ID        varchar(32)       not null comment '用户id',
    RECIPIENT_NAME varchar(32)       not null comment '收件人名称',
    PHONE          varchar(12)       not null comment '收件人电话',
    ADDRESS        varchar(256)      null comment '收件人地址',
    IS_DEFAULT     tinyint default 0 null comment '是否为默认地址',
    IS_DELETED     tinyint default 0 null comment '是否删除',
    CREATED_USER   varchar(32)       null comment '创建人',
    CREATED_TIME   timestamp         null comment '创建时间',
    UPDATED_USER   varchar(32)       null comment '更新人',
    UPDATED_TIME   timestamp         null comment '更新时间'
)
    comment '用户地址表';

create table T_ORDER
(
    ID             varchar(32)    not null comment '唯一键'
        primary key,
    USER_ID        varchar(32)    not null comment '用户id',
    ADDRESS_ID     varchar(32)    not null comment '收货地址id',
    STATUS         tinyint        not null comment '订单状态',
    TOTAL_PRICE    decimal(12, 3) null comment '订单总金额',
    TOTAL_QUANTITY int            null comment '订单物品总数',
    REMARK         varchar(512)   null comment '订单备注',
    PAY_TIME       timestamp      null,
    pay_type       varchar(32)    null comment '支付方式',
    SHIP_TIME      timestamp      null,
    IS_DELETED     tinyint        null comment '是否删除',
    CREATED_USER   varchar(32)    null comment '创建人',
    CREATED_TIME   timestamp      null comment '创建时间',
    UPDATED_USER   varchar(32)    null comment '更新人',
    UPDATED_TIME   timestamp      null comment '更新时间'
)
    comment '订单表';

create table T_ORDER_PRODUCT
(
    ID                     varchar(32)    not null comment '唯一键'
        primary key,
    PRODUCT_ID             varchar(32)    not null comment '商品id',
    PRODUCT_NAME_SNAPSHOT  varchar(64)    null comment '商品标题快照',
    ORDER_ID               varchar(32)    not null comment '订单id',
    PRODUCT_QUANTITY       int default 1  null comment '商品总数',
    PRODUCT_PRICE_SNAPSHOT decimal(10, 2) null comment '下单时商品单价',
    IS_DELETED             tinyint        null comment '是否删除',
    CREATED_USER           varchar(32)    null comment '创建人',
    CREATED_TIME           timestamp      null comment '创建时间',
    UPDATED_USER           varchar(32)    null comment '更新人',
    UPDATED_TIME           timestamp      null comment '更新时间'
)
    comment '订单商品关系表';

create table T_PRODUCT
(
    ID           varchar(32)       not null comment '唯一键'
        primary key,
    TITLE        varchar(64)       not null comment '商品标题',
    DESCRIPTION  varchar(128)      null comment '商品描述',
    PRICE        decimal(10, 2)    null comment '单价',
    STOCK        int     default 0 null comment '库存数量',
    STATUS       tinyint           null comment '状态 0=已下架 1=上架',
    IS_DELETED   tinyint default 0 null comment '是否删除',
    CREATED_USER varchar(32)       null comment '创建人',
    CREATED_TIME timestamp         null comment '创建时间',
    UPDATED_USER varchar(32)       null comment '更新人',
    UPDATED_TIME timestamp         null comment '更新时间'
)
    comment '商品表';

create table T_PRODUCT_IMAGE
(
    ID            varchar(32)       not null comment '唯一键'
        primary key,
    PRODUCT_ID    varchar(32)       not null comment '商品id',
    IMAGE_URL     varchar(256)      null comment '图片连接',
    IS_DISPLAY    tinyint           null comment '是否为列表展示图片',
    DISPLAY_ORDER int               null comment '图片展示顺序',
    IS_DELETED    tinyint default 0 null comment '是否删除',
    CREATED_USER  varchar(32)       null comment '创建人',
    CREATED_TIME  timestamp         null comment '创建时间',
    UPDATED_USER  varchar(32)       null comment '更新人',
    UPDATED_TIME  timestamp         null comment '更新时间'
)
    comment '商品图片表';

create table T_REFUND
(
    ID            varchar(32)       not null comment '唯一键'
        primary key,
    ORDER_ID      varchar(32)       not null comment '订单id',
    REASON        varchar(256)      null comment '退货原因',
    STATUS        tinyint default 0 null comment '退货状态 0=申请中, 1=已通过, 2=已拒绝, 3=已退款',
    APPLY_TIME    timestamp         null comment '申请时间',
    REFUND_AMOUNT decimal(10, 2)    null comment '退货金额',
    REVIEW_TIME   timestamp         null comment '商家审核时间',
    REVIEW_REMARK varchar(256)      null comment '商家审核备注',
    IS_DELETED    tinyint default 0 null comment '是否删除',
    CREATED_USER  varchar(32)       null comment '创建人',
    CREATED_TIME  timestamp         null comment '创建时间',
    UPDATED_USER  varchar(32)       null comment '更新人',
    UPDATED_TIME  timestamp         null comment '更新时间'
)
    comment '退货信息表';

create table T_SHIPMENT
(
    ID                varchar(32)       not null comment '唯一键'
        primary key,
    ORDER_ID          varchar(32)       not null comment '订单id',
    LOGISTICS_NO      varchar(32)       null comment '快递单号',
    LOGISTICS_COMPANY varchar(32)       null comment '物流公司名称',
    SHIP_TIME         timestamp         null comment '发货时间',
    STATUS            tinyint           null comment '发货状态 0=待发货,1=已发货,2=已签收',
    DELIVERY_TIME     timestamp         null comment '签收时间',
    IS_DELETED        tinyint default 0 null comment '是否删除',
    CREATED_USER      varchar(32)       null comment '创建人',
    CREATED_TIME      timestamp         null comment '创建时间',
    UPDATED_USER      varchar(32)       null comment '更新人',
    UPDATED_TIME      timestamp         null comment '更新时间'
)
    comment '发货信息表';

create table T_USER
(
    ID           varchar(32)       not null comment '唯一键'
        primary key,
    USER_NAME    varchar(32)       null comment '用户名',
    GENDER       tinyint           null comment '性别 0=未知 1=男 2=女',
    AVATAR_URL   varchar(64)       null comment '头像地址',
    PHONE_NUMBER varchar(12)       null comment '电话号码',
    EMAIL        varchar(64)       null comment '邮箱地址',
    USER_STATUS  tinyint default 0 null comment '用户状态 0=正常 1=异常',
    IS_DELETED   tinyint default 0 null comment '是否删除',
    CREATED_USER varchar(32)       null comment '创建人',
    CREATED_TIME timestamp         null comment '创建时间',
    UPDATED_USER varchar(32)       null comment '更新人',
    UPDATED_TIME timestamp         null comment '更新时间'
)
    comment '用户信息表';


