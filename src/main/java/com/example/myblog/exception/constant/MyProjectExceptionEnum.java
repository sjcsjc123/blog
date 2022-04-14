package com.example.myblog.exception.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * @author SJC
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MyProjectExceptionEnum {
    INVALID_USER_NULL(400,"用户名不能为空"),
    USER_NO_ONLY(400,"该用户名已经被注册"),
    ERROR_STAR(400,"不能关注自己"),
    REPEAT_STAR1(400,"已关注"),
    ERROR_STAR1(400,"未关注"),
    PHONE_NO_ONLY(400,"这个电话已经注册过，请换一个手机号"),
    INVALID_USERNAME_PASSWORD(400,"用户名或密码错误"),
    INVALID_PHONE_DATA_TYPE(400,"手机号格式错误"),
    INVALID_PASSWORD(400,"密码长度应该为8到20位并且同时包含数字和字母"),
    INVALID_PHONE_LENGTH(400,"手机号码长度不正确"),
    SHOP_DESCRIPTION_NULL(400,"商品描述不能为空"),
    SHOP_MONEY_NULL(400,"商品价格不能为空或负数"),
    SHOP_NULL(400,"店铺不存在"),
    ORDER_NULL(400,"订单不存在"),
    GOODS_PRICE_INVALID(400,"商品价格不正确，请确认"),
    GOODS_NUMBER_LESS(400,"商品库存不足"),
    GOODS_NUMBER_NULL(400,"商品数量错误"),
    SHIPPING_ERROR(400,"运费错误"),
    MONEY_PAID_INVALID(400,"余额支付错误，请重试"),
    COUPON_NULL(400,"优惠券不存在"),
    SHOP_NAME_NULL(400,"商品名称不能为空"),
    SMS_REPEAT(400,"请勿重复发送"),
    PASSWORD_NOT_CONFIRM(400,"密码前后不一样，请核对"),
    USERNAME_NOT_LOGIN(400,"该用户未登录，请重新登录，转到登录界面"),
    NULL_ERROR(404,"空指针错误"),
    GOODS_ENTAIL_ERROR(400,"获取商品详情失败"),
    BUSINESS_NAME_NULL(400,"店铺名称不能为空"),
    ORDER_NUMBER_IS_EXIST(400,"发生了错误，订单号已经存在，请重试"),
    BUSINESS_NAME_EQUAL_NAME(400,"商品名称不能与店铺名称一致，请更换商品名称"),
    PASSWORD_ERROR(400,"密码错误，请重新登录"),
    USERNAME_IS_ENABLE(400,"账号已禁用"),
    USERNAME_IS_EMPTY(400,"用户名不存在"),
    ROLE_IS_NULL(400,"该用户没有权限，请联系管理员"),
    TOKEN_IS_EXPIRE(400,"登录已失效，请重新登录"),
    COUPON_IS_NULL(400,"优惠券已经不存在"),
    REPEAT_THUMB(400,"请勿重复点赞"),
    NO_THUMB(400,"并未点赞"),
    REPEAT_STAR(400,"请勿重复收藏"),
    NO_STAR(400,"并未收藏"),
    GOODS_NULL(400,"商品已经不存在");
    private int code;
    private String msg;
}
