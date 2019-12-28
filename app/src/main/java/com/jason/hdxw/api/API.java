package com.jason.hdxw.api;

/**
 * 接口列表
 * created by wang on 2018/11/17
 */
public class API {
    //本地测试
//        public static final String BASE = "http://192.168.1.119:7008/index.php?m=";
    //线下测试
//    public static final String BASE = "http://dianduoduo.jiesenkeji.com/index.php?m=";
    //正式地址
//    public static final String BASE = "http://47.97.109.131/index.php?m=";

    //public static final String BASE = "http://121.40.11.219/index.php?m=";
    public static final String BASE = "http://x1.zxzyw.net/index.php?m=";
    //TODO 基础模块
    //手机短信验证码
    public static final String BASIC_OTP=BASE+"jason&c=public&a=sendCode";
    //图形验证码 网页链接
    public static final String BASICS_IMG_VERIFY = BASE + "user&c=public&a=verify";
    //登录
    public static final String BASICS_LOGIN = BASE + "jason&c=public&a=login";
    //注册 账号不能是纯中文或者纯数字
    public static final String BASICS_REGISTER = BASE + "jason&c=public&a=user_reg";
    //首页收益
    public static final String BASICS_HOME_EARNINGS = BASE + "jason&c=user&a=index";
    //公告列表
    public static final String BASICS_NOTICE_LIST = BASE + "jason&c=user&a=help_lists";
    //公告详情
    public static final String BASICS_NOTICE_DETAILS = BASE + "jason&c=user&a=help_show";
    //新手教程
    public static final String BASICS_NOVICE_COURSE = BASE + "jason&c=user&a=course";
    //关于我们
    public static final String BASICS_ABOUT = BASE + "jason&c=user&a=about_find";
    //推荐二维码
    public static final String BASICS_QRCODE = BASE + "jason&c=user&a=wx_code";
    //版本信息
    public static final String BASICS_VERSION_MSG = BASE + "jason&c=user&a=about";
    //新闻模块
    public static final String BASICS_JOURNALISM = BASE + "jason&c=user&a=money_list";
    //意见反馈接口
    public static final String BASICS_IDEA_RETRUN = BASE + "jason&c=user&a=fakui";
    //注册协议
    public static final String BASICS_REGISTER_PROTOCOL = BASE + "jason&c=public&a=xieyi";
    //版本升级
    public static final String BASICS_UPDATE = BASE + "jason&c=user&a=sversion";

    //TODO 会员中心
    //修改支付密码
    public static final String MEMBER_CHANGE_PAY = BASE + "jason&c=user&a=passzhi";
    //修改登录密码
    public static final String MEMBER_CHANGE_LOGIN = BASE + "jason&c=user&a=repass";
    //提现时间说明
    public static final String MEMBER_KITING_TIME = BASE + "jason&c=user&a=tixian_log";
    //获取个人信息
    public static final String MEMBER_USERINFO = BASE + "jason&c=user&a=userinfo";
    //获取银行卡信息
    public static final String MEMBER_BANK_MSG = BASE + "jason&c=user&a=bank_find";
    //添加银行卡
    public static final String MEMBER_BANK_ADD = BASE + "jason&c=user&a=up_bank";
    //更换头像
    public static final String MEMBER_CHANGE_HEADIMG = BASE + "jason&c=user&a=ico_image";
    //立即提现
    public static final String MEMBER_KITING = BASE + "jason&c=user&a=tixian";
    //退出程序
    public static final String MEMBER_EXIT_APP = BASE + "jason&c=public&a=loginout";
    //提现明细
    public static final String MEMBER_KITING_DETAILS = BASE + "jason&c=user&a=user_withdrawlog";
    //收益明细
    public static final String MEMBER_EARNINGS_DETAILS = BASE + "jason&c=user&a=profit_details";
    //我的团队
    public static final String MEMBER_MY_TEAM = BASE + "jason&c=user&a=my_team";
    //获取提现类型
    public static final String MEMBER_KITING_TYPE = BASE + "jason&c=user&a=tixian_fang";
    //获取支付宝信息
    public static final String MEMBER_ZFB_MSG = BASE + "jason&c=user&a=zhifubao";
    //获取微信信息
    public static final String MEMBER_WX_MSG = BASE + "jason&c=user&a=wx";
    //上传支付宝二维码
    public static final String MEMBER_ZFB_SEND = BASE + "jason&c=user&a=zhifubao_img";
    //上传微信二维码
    public static final String MEMBER_WX_SEND = BASE + "jason&c=user&a=weixin_img";
    //更新支付宝信息
    public static final String MEMBER_ZFB_UPDATA = BASE + "jason&c=user&a=zhifubao_save";
    //更新微信信息
    public static final String MEMBER_WX_UPDATA = BASE + "jason&c=user&a=weixin_save";
    //检查开始赚钱是否可用
    public static final String MEMBER_EXAMINE_MONEY = BASE + "jason&c=ddd&a=testing_money";
    //查看当前声音状态
    public static final String MEMBER_AD_MUSIC = BASE + "jason&c=user&a=zt";
    //留言反馈列表接口
    public static final String MEMBER_MSG_RETURN = BASE + "jason&c=user&a=fankui";
}
