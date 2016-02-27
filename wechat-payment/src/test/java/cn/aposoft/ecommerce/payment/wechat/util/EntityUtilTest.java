/**
 * 
 */
package cn.aposoft.ecommerce.payment.wechat.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import cn.aposoft.ecommerce.payment.wechat.EntityUtil;
import cn.aposoft.ecommerce.payment.wechat.RefundBill;
import cn.aposoft.ecommerce.payment.wechat.RefundQueryResponse;
import cn.aposoft.ecommerce.payment.wechat.impl.SimpleEntityUtil;

/**
 * 实体对象测试类
 * <p>
 * 1. parseRefundQueryResponseXml
 * 
 * @author Jann Liu
 *
 */
public abstract class EntityUtilTest {
	private static final String REFUND_QUERY_RESULT = "<xml>" + "<appid><![CDATA[123]]></appid>"
			+ "<cash_fee><![CDATA[10]]></cash_fee>" + "<mch_id><![CDATA[234]]></mch_id>"
			+ "<nonce_str><![CDATA[q99q6czw3XpLnD1B]]></nonce_str>"
			+ "<out_refund_no_0><![CDATA[1002240240201510251334363255]]></out_refund_no_0>" // 0-1
			+ "<out_trade_no><![CDATA[20151025_1]]></out_trade_no>"
			+ "<refund_channel_0><![CDATA[ORIGINAL]]></refund_channel_0>" // 0-2
			+ "<refund_count>1</refund_count>" + "<refund_fee>10</refund_fee>" //
			+ "<refund_fee_0>10</refund_fee_0>" // 0-3
			+ "<refund_id_0><![CDATA[2002240240201510260064983833]]></refund_id_0>" // 0-4
			+ "<refund_status_0><![CDATA[SUCCESS]]></refund_status_0>" // 0-5
			+ "<result_code><![CDATA[SUCCESS]]></result_code>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
			+ "<return_msg><![CDATA[OK]]></return_msg>" + "<sign><![CDATA[42197A9FB6102207B2F76B057861D25B]]></sign>"
			+ "<total_fee><![CDATA[10]]></total_fee>"
			+ "<transaction_id><![CDATA[1002240240201510251334363255]]></transaction_id>" //
			+ "<out_refund_no_1><![CDATA[100224024020151025133436325-1]]></out_refund_no_1>" // 1-1
			+ "<refund_channel_1><![CDATA[ORIGINAL]]></refund_channel_1>" // 1-2
			+ "<refund_fee_1>102</refund_fee_1>" // 1-3
			+ "<refund_id_1><![CDATA[2002240240201510260064983833-1]]></refund_id_1>" // 1-4
			+ "<refund_status_1><![CDATA[SUCCESS]]></refund_status_1>" // 1-5
			+ "</xml>";

	protected abstract EntityUtil getUtil();

	/**
	 * 打印测试结果
	 */
	public void printParseRefundQueryResponseXml() {
		printRefundQueryResponseResult(getParseRefundQueryResponseXml());
	}

	/**
	 * 测试退款单查询结果打印
	 * 
	 * @param response
	 *            退款单查询结果响应对象
	 */
	public static void printRefundQueryResponseResult(RefundQueryResponse response) {
		System.out.println("退款单查询：");
		// 状态信息
		System.out.println("return_code:" + response.getReturn_code());
		System.out.println("return_msg:" + response.getReturn_msg());
		//
		System.out.println("result_code:" + response.getResult_code());

		System.out.println("err_code:" + response.getErr_code());
		System.out.println("err_code_des:" + response.getErr_code_des());
		// 商户相关信息
		System.out.println("appid:" + response.getAppid());
		System.out.println("mch_id:" + response.getMch_id());

		System.out.println("device_info:" + response.getDevice_info());
		// 校验信息
		System.out.println("nonce_str:" + response.getNonce_str());
		System.out.println("sign:" + response.getSign());
		// 订单退款单信息
		System.out.println("transaction_id:" + response.getTransaction_id());
		System.out.println("out_trade_no:" + response.getOut_trade_no());
		System.out.println("total_fee:" + response.getTotal_fee());
		System.out.println("fee_type:" + response.getFee_type());

		System.out.println("cash_fee:" + response.getCash_fee());
		System.out.println("refund_count:" + response.getRefund_count());

		for (RefundBill bill : response.getRefundBillItems()) {
			print(bill);
		}
	}

	private static void print(RefundBill bill) {
		System.out.println("Bill+n:" + bill.getN());
		System.out.println("out_refund_no:" + bill.getOut_refund_no());
		System.out.println("refund_id:" + bill.getRefund_id());
		System.out.println("refund_status:" + bill.getRefund_status());
		System.out.println("refund_channel:" + bill.getRefund_channel());
		System.out.println("refund_fee:" + bill.getRefund_fee());
	}

	/**
	 * 执行退款结果解析测试 {@code parseRefundQueryResponseXml}
	 */
	@Test
	public void parseRefundQueryResponseXml() {
		assertRefundQueryResponse(getParseRefundQueryResponseXml());
	}

	/**
	 * 验证退款查询结果
	 * 
	 * @param response
	 */
	private void assertRefundQueryResponse(RefundQueryResponse response) {
		assertNotNull(response);
		assertEquals("SUCCESS", response.getReturn_code());
		assertEquals("OK", response.getReturn_msg());
		assertEquals("SUCCESS", response.getResult_code());
		assertEquals(null, response.getErr_code());
		assertEquals("123", response.getAppid());
		assertEquals("234", response.getMch_id());
		assertEquals(null, response.getDevice_info());
		assertEquals("q99q6czw3XpLnD1B", response.getNonce_str());
		assertEquals("42197A9FB6102207B2F76B057861D25B", response.getSign());
		assertEquals("1002240240201510251334363255", response.getTransaction_id());
		assertEquals("20151025_1", response.getOut_trade_no());
		assertEquals(10, response.getTotal_fee().intValue());
		assertEquals(1, response.getRefund_count().intValue());

		assertBills(response.getRefundBillItems());
	}

	// 验证退款单
	private void assertBills(List<RefundBill> refundBillItems) {
		assertNotNull(refundBillItems);
		assertEquals(2, refundBillItems.size());
		//
		RefundBill bill = refundBillItems.get(0);
		assertEquals("1002240240201510251334363255", bill.getOut_refund_no());
		assertEquals("2002240240201510260064983833", bill.getRefund_id());
		assertEquals("SUCCESS", bill.getRefund_status());
		assertEquals("ORIGINAL", bill.getRefund_channel());
		assertEquals(10, bill.getRefund_fee().intValue());
		//
		RefundBill bill1 = refundBillItems.get(1);
		assertEquals("100224024020151025133436325-1", bill1.getOut_refund_no());
		assertEquals("2002240240201510260064983833-1", bill1.getRefund_id());
		assertEquals("SUCCESS", bill1.getRefund_status());
		assertEquals("ORIGINAL", bill1.getRefund_channel());
		assertEquals(102, bill1.getRefund_fee().intValue());
	}

	/**
	 * 测试退款响应xml报文解析
	 * 
	 * @return 退款查询响应结果
	 */
	private RefundQueryResponse getParseRefundQueryResponseXml() {
		RefundQueryResponse response = getUtil().parseRefundQueryResponseXml(REFUND_QUERY_RESULT);
		return response;
	}

	public static void main(String[] args) {
		EntityUtilTest utilTester = new EntityUtilTest() {
			@Override
			protected EntityUtil getUtil() {
				return SimpleEntityUtil.getInstance();
			}
		};

		utilTester.printParseRefundQueryResponseXml();
	}
}
