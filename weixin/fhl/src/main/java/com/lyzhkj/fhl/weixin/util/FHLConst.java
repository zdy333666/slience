/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.weixin.util;

/**
 *
 * @author breeze
 */
public class FHLConst {

    //极光推送
    protected static final String JG_APP_KEY = "e5a14403f858c7170c80dcec";
    protected static final String JG_MASTER_SECRET = "a53a31178614158323e907c7";

    /**
     * 居民
     */
    public static final String MESSAGE_ROLE_BD = "jm";
    /**
     * 商家
     */
    public static final String MESSAGE_ROLE_SJ = "sj";
    /**
     * 巡检
     */
    public static final String MESSAGE_ROLE_PK = "pk";

    /*
	 * -----------------用户提示--------------------------
     */
    public static final String SUBSCRIBE
            = "您好，感谢您关注\"分好啦\"公众服务号，在这里，您可以进行积分查询，积分兑换，出示二维码等服务！垃圾有分类，生活有品质！【联运知慧】\n"
            + "服务热线：400 012 3366";

    public static final String INFO_BIND_USER
            = "业主绑定：\n"
            + "请输入jm+您的账户对应的手机号码来获取绑定验证码。\n"
            + "如jm13512345678\n"
            + "商户负责人或者收银员绑定：\n"
            + "请输入sj+您的账户对应的手机号码来获取绑定验证码。\n"
            + "如sj13512345678\n"
            + "巡检员绑定：\n"
            + "请输入pk+您的账户对应的手机号码来获取绑定验证码。\n"
            + "如pk13512345678\n"
            + "输入的时候请注意大小写，不要加空格";

    public static final String INFO_HELP_USER
            = "商户和业主可以通过两种方式完成交易。\n";

    public static final String INFO_USER_INTEGRAL
            = "{0}：您好，您目前的\n"
            + "投放奖励积分为：{1}\n"
            + "可回收积分为：{2}\n"
            + "巡检积分为：{3}\n"
            + "账户总积分为：{4}\n"
            + "可兑换积分为：{5}。";

    public static final String INFO_BAGPICK_1
            = "{0}：您好，没有查询到您的垃圾巡检记录。";

    public static final String INFO_EXCHANGE_1
            = "最近的兑换记录明细：无";

    public static final String INFO_EXCHANGE_2
            = "最近的兑换记录明细：";

    public static final String INFO_EXCHANGE_3
            = "{0}：您好，下面是({1})的信息。\n"
            + "累计兑换积分：{2} \n"
            + "本月已兑换积分：{3} \n"
            + "累计提现积分：{4} \n"
            + "剩余可提现积分：{5} \n";

    public static final String INFO_EXCHANGE_4
            = "{0}：您好，没有查询到您的门店信息。";

    public static final String INFO_EXCHANGE_5
            = "{0}：您好，所属门店：{1} 收银端账号：{2}\n"
            + "累计兑换积分：{3}。\n"
            + "本月已兑换积分：{4}。\n"
            + "{5}。\n";

    public static final String INFO_BAGPICK_2
            = "{0}：您好，下面是您最近的垃圾巡检记录。";

    public static final String INFO_BAGPICK_ITEM
            = "{0}\n{1}给您的{2}({3})打了{4}分";

    public static final String INFO_EXCHANGE_ITEM
            = "{0}\n{1}向您支付了{2}积分";

    public static final String INFO_HELP_XX
            = "开发ing";

    public static final String INFO_KITCHENMID
            = "厨余二维码,点图放大";

    public static final String INFO_OTHERMID
            = "其他二维码,点图放大";

    public static final String INFO_WEIXINQR
            = "我的二维码,点图放大";

    public static final String INFO_WEIXINQR_PAY_SJ
            = "时间{0} \n"
            + "门店({1}) \n"
            + "业主：{2} 向您支付了{3}积分 \n"
            + "本月({4})累计收取{5}积分";

    public static final String INFO_WEIXIN_PAY_JM
            = "时间{0} 您向商户({1}) 支付了积分{2}，您的目前可用积分为{3}";

    public static final String INFO_UN_KNOW
            = "???";

    public static final String INFO_TERMINAL_SUCESS
            = "垃圾袋领取成功,请及时拿走你的垃圾袋";
    public static final String INFO_TERMINAL_FAILURE
            = "垃圾袋领取失败,请确认本月是否已经领取";
    public static final String INFO_TERMINAL_SYSTEMERR
            = "系统异常，垃圾袋领取失败";
    public static final String INFO_VOID_QR
            = "二维码无效";

    public static final String SUCCESS_SCAN_QRCARD
            = "业主：{0} \n领卡成功";
    public static final String FAIL_SCAN_QRCARD
            = "业主：{0} \n领卡失败，请重试";

    /*
	 * -----------------短信信息推送--------------------------
     */
    public static final String INFO_SENDSMS0
            = "{0}(微信垃圾分类智能平台验证码，五分钟内有效)";
    public static final String INFO_SENDSMS2
            = "短信验证码发送失败，请通知管理员。";
    public static final String INFO_SENDSMS3
            = "短信验证码发送成功，请输入收到的短信验证码。";
    public static final String INFO_SENDSMS4
            = "短信验证码已发送，请五分钟后再请求。";

    /*
	 * ------------------输入提示-------------------------
     */
    public static final String SUCCESS_SCAN_GARBAGE
            = "巡检员（{0}）：该{1}属于业主（{2}），请打分，输入0-5的数值。";
    public static final String SUCCESS_SCAN_GARBAGE_INPUT
            = "巡检员（{0}）：本次您给业主（{1}）的{2}垃圾袋，打了{3}分。";
    public static final String SUCCESS_SCAN_RECEIPT
            = "商户（{0}）：该业主{1}可被扣的积分为{2}，请输入本次需要扣除的积分。";

    public static final String WARN_OWNER_DELETE
            = "该业主被手动删除";

    /*
	 * -----------------警告--------------------------
     */
    public static final String WARN_NO_ROLE_1
            = "垃圾巡检功能，只有巡检员才能使用。";

    public static final String WARN_NO_ROLE_2
            = "您还未绑定{0}角色，不能进行此项操作。\n";
//            + "如果您需要绑定，请点击\"系统设置\"中的\"账户绑定\"菜单,并根据提示操作。";

    public static final String WARN_NO_ROLE_3
            = "您已绑定商户负责人角色，无法进行扫码收款操作";

    public static final String WARN_SCAN_GARBAGE
            = "未识别的垃圾袋二维码。";

    public static final String WARN_SCAN_RECEIPT
            = "无法从该二维码获得业主信息。";

    public static final String WARN_SCAN_UNBUND
            = "无法获得该用户信息。";

    public static final String WARN_SCAN_QRCARD
            = "此卡未绑定有效业主。";

    public static final String WARN_SCAN_UNKNOW_QRCARD
            = "请扫描有效的厨余或其他二维码。";

    public static final String WARN_SCAN_FINISHED
            = "该垃圾袋今天已经被巡检过，分值为：";

    /*
	 * -----------------异常--------------------------
     */
    public static final String ERR_BIND
            = "系统错误，绑定失败，稍后再试。";
    public static final String ERR_UNBIND
            = "系统错误，解绑失败，稍后再试。";
    public static final String ERR_QR
            = "系统错误，二维码推送失败，稍后再试。";
    public static final String ERR_PAY
            = "系统错误，兑换失败，稍后再试。";
    public static final String ERR_PICK
            = "系统错误，巡检打分失败，稍后再试。";
    public static final String ERR_SHOWVIEW
            = "系统错误，数据查询失败，稍后再试。";
    public static final String ERR_SYSTEM
            = "系统异常，请稍后再试。";
    public static final String ERR_TERMINAL
            = "系统错误，领袋失败，稍后再试。";

    /*
	 * -----------------解绑相关--------------------------
     */
    public static final String WARN_UNBIND_BD
            = "您还未绑定业主角色，不能进行此项操作。\n"
            + "如果您需要绑定：请输入jm+您的账号对应的手机号码来获取绑定验证码。"
            + "如：jm13512345678\n"
            + "请注意大小写，不要加空格";

    public static final String WARN_UNBIND_SJ
            = "您还未绑定{0}或者{1}角色，不能进行此项操作。\n"
            + "如果您需要绑定，请点击\"系统设置\"中的\"账户绑定\"菜单,并根据提示操作。";

    public static final String WARN_UNBIND_PK
            = "您还未绑定巡检员角色，不能进行此项操作。";

    public static final String WARN_UNBIND_All
            = "您还未绑定任何角色，不需要进行解绑操作。";

    public static final String SUCCESS_UNBIND_BD
            = "您已成功解绑业主角色。";

    public static final String SUCCESS_UNBIND_SJ
            = "您已成功解绑{0}角色。";

    public static final String SUCCESS_UNBIND_PK
            = "您已成功解绑巡检员角色。";

    public static final String CASHIER_USERTYPE_2 = "商户负责人";

    public static final String CASHIER_USERTYPE_3 = "收银员";

    /*
	 * -----------------绑定相关--------------------------
     */
    public static final String SUCCESS_BIND_BD
            = "{0}：您好，您已经成功绑定业主角色，您的当前可用积分为：{1}。";

    public static final String SUCCESS_BIND_PK
            = "{0}：您好，您已经成功绑定巡检员角色。";

    public static final String SUCCESS_BIND_SJ
            = "{0}：您好，您已经成功绑定{1}角色。";

    public static final String WARN_HAD_BIND_BD
            = "当前号码已绑定业主角色，请先解绑";

    public static final String WARN_HAD_BIND_SJ
            = "当前号码已绑定{0}角色，请先解绑。";

    public static final String WARN_HAD_BIND_PK
            = "当前号码已绑定巡检员角色，请先解绑。";

    public static final String WARN_NOT_REGEDIT
            = "绑定失败，当前号码没有登记。请联系管理员进行账户注册";

    public static final String WARN_NOT_REGEDIT2
            = "绑定失败，当前号码没有绑定智能垃圾分类卡";

    /*
	 * -----------------用户输入相关--------------------------
     */
    public static final String WARN_404
            = "您输入了：{0}";

    public static final String WARN_ERR_CAPTCHA
            = "验证码错误，请确认后重新输入。";

    public static final String WARN_SCAN_INPUT_1
            = "请输入有效数值， 0 ~ 5";

    public static final String WARN_INPUT
            = "请输入有效数值";

    public static final String WARN_PAY_INPUT
            = "输入积分不能大于业主的可扣积分。";

    public static final String INFO_REPORT_SUCCESS
            = "感谢您的意见反馈\n"
            + "{0}\n\n"
            + "您反馈的关于“{1}”问题已经由系统记录并反映至相关部门，有关部门核实后会在5个工作日内给予反馈，"
            + "反馈内容会推送至您的公众号。";

    //或者登陆智慧垃圾分类云平台官网www.lajifenlei.com意见反馈专栏，搜索相应反馈内容查询
    
    public static final String INFO_REPORT_FEEDBACK
            = "感谢您的意见反馈\n"
            + "{0}\n\n"
            + "您反馈的关于“{1}”问题,有关部门已给出如下回复：\n{2}";
}
