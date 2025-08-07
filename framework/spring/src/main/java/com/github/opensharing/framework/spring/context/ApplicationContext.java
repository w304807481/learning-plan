package com.github.opensharing.framework.spring.context;

import com.github.opensharing.framework.spring.beans.factory.BeanFactory;

/**
 * 定义延迟加载Bean的功能
 *
 * @author jwen
 * Date 2023/11/10
 */
public interface ApplicationContext extends BeanFactory {

    /**
     * 配置文件加载并创建对象
     */
    void refresh();
}
