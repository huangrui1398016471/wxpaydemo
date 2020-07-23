
# 微信小程序支付
小程序中不支持h5支付，这个太坑了，原先在调试模式的时候可以支付，等到上线了发现支付调不通，说多了都是泪。然后又急急忙忙重新研究小程序支付！
![image.png](https://img.hacpai.com/file/2020/04/image-6f4a1fff.png)
# 小程序支付需要两了解个步骤
1. 统一下单
[统一下单接口](https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=9_1&index=1)

2. 调起支付
[调起支付](https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5)
# demo参考
https://github.com/huangrui1398016471/wxpaydemo

1. 拉取项目修改controller中的appid和openid改为你自己的。
2. 在Constant类中修改商户号和key等信息
3. 启动后台项目，在小程序中调用接口进行测试(我使用的uniapp)


```
<template>
	<view class="content">
		<button @click="payHandler" class="pay-btn" type="primary">
			<view class="iconfont iconweixin"></view>
			<text class="title">微信支付</text>
		</button>
	</view>
</template>

<script>
export default {
	data() {
		return {};
	},
	onLoad() {},
	methods: {
		// 支付按钮点击方法
		payHandler() {
			uni.request({
				url: 'http://localhost:8080/wx/wxpay/weixinlogin', //仅为示例，并非真实接口地址。
				data: {
					text: 'uni.request'
				},
				header: {
					'custom-header': 'hello' //自定义请求头信息
				},
				success: res => {
					console.log(res.data);
					uni.req;
					var that = this;
					var time1 = new Date().getTime();
					var signData = {
						timeStamp: '1588063843', // 时间戳从1970年1月1日至今的秒数，即当前的时间
						nonceStr: 'chengdusiruiqixingxichanyegongsi', // 随机字符串，长度为32个字符以下
						package: 'prepay_id=' + res.data.prepay_id, // 统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=xx
						signType: 'MD5' //签名算法，暂支持 MD5
					};

					uni.request({
						url: 'http://localhost:8080/wx/wxpay/secondSign',
						data: signData,
						method: 'POST',
						success: re => {
							uni.requestPayment({
								provider: 'wxpay',
								// orderInfo: '同意冰红茶', // 订单数据
								timeStamp: '1588063843', // 时间戳从1970年1月1日至今的秒数，即当前的时间
								nonceStr: 'chengdusiruiqixingxichanyegongsi', // 随机字符串，长度为32个字符以下
								package: 'prepay_id=' + res.data.prepay_id, // 统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=xx
								signType: 'MD5', //签名算法，暂支持 MD5
								paySign: re.data, // 签名
								success: function(res) {
									console.log(res);
									// 支付成功的回调中 创建绘本馆成功
									uni.showToast({
										title: '微信支付成功',
										icon: 'success',
										duration: 1500
									});
								},
								fail: function(err) {
									console.log(err);
									// 支付失败的回调中 用户未付款
									uni.showToast({
										title: '支付取消',
										duration: 1500,
										image: '/static/png/error_icon.png'
									});
								}
							});
						}
					});
				}
			});
		}
	}
};
</script>

<style lang="scss">
.content {
	position: absolute;
	top: 0upx;
	left: 0upx;
	bottom: 0upx;
	right: 0upx;
	display: flex;
	align-items: center;
	justify-content: center;

	.pay-btn {
		/*  #ifdef  MP-WEIXIN  */
		background-color: #86db48;
		/*  #endif  */
		/*  #ifdef  MP-ALIPAY  */
		background-color: #3296fa;
		/*  #endif  */
		width: 600upx;
		height: 90upx;
		border-radius: 45upx;
		display: flex;
		justify-content: center;
		align-items: center;

		.title {
			font-size: 18px;
			color: #fff;
			margin-left: 20upx;
		}

		.iconfont {
			color: #fff;
		}
	}
}
.bottom {
	/* border-radius: 80rpx; */
	margin: 70rpx 50rpx;
	font-size: 35rpx;
}
</style>

```
