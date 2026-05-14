package com.geek;

import com.geek.tools.WeatherTools;

import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.MsgRole;
import io.agentscope.core.model.DashScopeChatModel;
import io.agentscope.core.tool.Toolkit;

/**
 * Hello world!
 * 注意：在启动类中增加环境变量DASHSCOPE_API_KEY
 */
public class AgentV1
{
    public static void main( String[] args )
    {
        // 1. 配置模型
        DashScopeChatModel model = DashScopeChatModel.builder()
                .apiKey(System.getenv("DASHSCOPE_API_KEY")) // 从环境变量读取Key
                .modelName("qwen-max")                      // 指定通义千问模型
                .build();

        // 2. 在构建Agent时注册工具
        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(new WeatherTools());

        // 3. 构建 ReActAgent
        ReActAgent agent = ReActAgent.builder()
                .name("MyAssistant")// 智能体名字
                .toolkit(toolkit) // 注入工具包
                .sysPrompt("你是一个友好的AI助手，名叫小助手。") // 系统提示词，定义角色
                .model(model)                              // 注入模型
                .build();

        // 4. 发送消息并调用
        Msg userMsg = Msg.builder()
                .role(MsgRole.USER)
                .textContent("北京的天气怎么样？")
                .build();

        Msg response = agent.call(userMsg).block(); // 同步等待结果
        System.out.println(response.getTextContent());
    }
}
