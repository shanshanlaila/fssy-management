/**
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-03-05	   添加物料附件上传功能
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-04-05	   添加计划价附件上传功能
 */
package com.fssy.shareholder.management.pojo.common;

/**
 * 模块（场景）枚举类
 * 
 * @author Solomon
 */
public enum Module
{

	TECHNOLOGY(1, "tech"), FINANCE(2, "finance"), SALE(3, "sale"), MANUFACTURE_PLAN(4, "manufacture_plan"),
	MANUFACTURE(5, "manu"),
	BULK_PURCHASE(6, "bulk_purchase"), // 正常采购计划
	MINOR_PURCHASE(7,"minor_purchase"), // 零星采购计划
	TRIAL_PURCHASE(8,"trial_purchase"), // 试制采购计划
	DELIVERY_PLAN_BUSINESS(9,"delivery_plan_business"), // 商用车交付计划
	DELIVERY_PLAN_PASSENGER(10,"delivery_plan_passenger"), // 乘用车交付计划

	DELIVERY_BILL_DETAIL_CV(11,"delivery_bill_cv_customer"), // 客户商用车交货单
	DELIVERY_BILL_DETAIL_PV(12,"delivery_bill_pv_customer"),//客户乘用车交货单
	CHECKINF_ACCOUNT_CV(13, "checking_account_customer"),//客户商用车对账单
	INVOICED_CUSTOMER(14, "invoiced_list_customer"),//开票通知单台账
	INVOICED_CUSTOMER_DETAIL(15, "invoiced_detail_customer"),//开票通知单明细
	CHECKING_ACCOUNT_SUPPLIER(16,"checking_account_supplier"),//供应商提供的交货单
	MONTHLY_PLAN_CUSTOMER(17,"monthly_plan_customer"),//客户月度预示计划
	DELIVERY_WEEKLY_PLAN(18, "delivery_weekly_plan"),// 客户周计划
	WAREHOUSE_INVENTORY_CHECKING(19, "warehouse_inventory_checking"),// 库存盘点表
	DELIVERY_PLAN_NEED_RESOLVE(20, "delivery_plan_need_resolve"),// 系统分解的客户计划
	MATERIAL_SUPPLY_RELATION(21,"material_supply_Relation"),//供应关系
	PURCHASE_PRICE(22,"purchase_price"),//采购合同价格
	SALE_CONTRACT(23,"sale_contract"), // 销售合同
	PURCHASE_CONTRACT(24,"purchase_contract"), // 采购合同
	SALE_PRICE(25,"sale_price"),
	SCM_WAREHOUSE_INVENTORY(26,"scm_warehouse_inventory"),// 实时库存
	DELIVERY_BILL_DETAIL_MINOR(27,"delivery_bill_minor_customer"),// 零星客户交货单
	DELIVERY_BILL_DETAIL_CV_REPEAT(28,"delivery_bill_cv_customer_repeat"), // 二次导入客户商用车交货单
	DELIVERY_BILL_DETAIL_PV_REPEAT(29,"delivery_bill_pv_customer_repeat"),//二次导入客户乘用车交货单
	SHOPPING_RETURN_NOTICE(30,"shopping_return_notice"), // 发出商品退货通知单
	MATERIAL_DATA(31,"material_data"), // 发出商品退货通知单
	BOM_WAIT_DATA(40,"bom_wait_data"), // bom编制文件
	TECH_STEP_DATA(50,"tech_step_data"), // 技措单
	BOM_DEVELOPMENT_AGREEMENT(51,"bom_development_agreement"), // 开发协议
	BOM_WAIT_DATA_TECH(60,"bom_wait_data_tech"), // 技术bom维护文件
	BOM_WAIT_DATA_CRAFT(61,"bom_wait_data_craft"), // 工艺bom维护文件
	BOM_WAIT_DATA_PURCHASE(62,"bom_wait_data_purchase"), // 采购bom维护文件
	BOM_WAIT_DATA_INVENTORY(63,"bom_wait_data_inventory"), // 物控bom维护文件
	BOM_WAIT_DATA_COLOR(64,"bom_wait_data_color"), // 涂装bom维护文件
	BOM_WAIT_DATA_CRAFT_CHANGE(65,"bom_wait_data_craft_change"), // 工艺变更bom维护文件
	BOM_TECH_4M_DATA(66,"bom_tech_4m_data"), // 4M变更文件
	DELIVERY_PLAN_WARRANTY(67,"delivery_plan_warranty"), // 三包交付计划
	DELIVERY_BILL_DETAIL_WARRANTY(68,"delivery_bill_detail_warranty"),//三包交货单
	FINANCE_PLAN_PRICE(69,"plan_price"),// 计划价附件
	DELIVERY_WEEK_PLAN_PRO(70,"delivery_week_plan_pro"),// 客户周计划附件，提前导入
	PURCHASE_MINOR_ATTACHMENT(71,"purchase_minor_attachment"), // 请购单附件
	CHECKING_ACCOUNT_ATTACHMENT(72,"checking_account_attachment"), // 客户对账单附件
	CHECKING_ACCOUNT_SUPPLIER_SYS_ATTACHMENT(73,"checking_account_supplier_sys_attachment"), // 供应商对账单附件
	INVOICED_SUPPLIER_DFLQ(74,"invoiced_supplier_dflq"), // 柳汽发来的供应商开票单
	CHECKING_ACCOUNT_SUPPLIER_SYS_WAIT_INVOICE_NUMBER(75,"checking_account_supplier_sys_wait_invoice_number"), // 柳汽的供应商台账修改开票数
	TRANSFER_SLIP_DETAIL_ATTACHMENT(76,"transfer_slip_detail_attachment"),// 调拨单导入附件
	DELIVERY_BILL_DETAIL_IMPORT(77,"delivery_bill_detail_import");// 客户交货单导入附件




	private final Integer value;

	private final String name;

	private Module(Integer value, String name)
	{
		this.value = value;
		this.name = name;
	}

	public Integer getValue()
	{
		return value;
	}

	public String getName()
	{
		return name;
	}
}
