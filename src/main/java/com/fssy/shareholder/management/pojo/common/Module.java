/**
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-03-05	   添加物料附件上传功能
 * <p>
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-04-05	   添加计划价附件上传功能
 */
package com.fssy.shareholder.management.pojo.common;

/**
 * 模块（场景）枚举类
 *
 * @author Solomon
 */
public enum Module {

    PERFORMANCE_HANDLERS_ITEM(1, "handler_item"),
    PERFORMANCE_EVENT_LIST(2, "event_list");


    private final Integer value;

    private final String name;

    private Module(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
