package com.geek;

import java.util.Scanner;

import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.MsgRole;
import io.agentscope.core.model.DashScopeChatModel;
import io.agentscope.core.tool.Toolkit;
import io.agentscope.core.tool.mcp.McpClientBuilder;
import io.agentscope.core.tool.mcp.McpClientWrapper;

/**
 * Agent集成McpServer
 * 注意：在启动类中增加环境变量DASHSCOPE_API_KEY
 */
public class AgentV2
{
    public static void main( String[] args )
    {
        // 1. 配置模型
        DashScopeChatModel model = DashScopeChatModel.builder()
                .apiKey(System.getenv("DASHSCOPE_API_KEY")) // 从环境变量读取Key
                .modelName("deepseek-v4-flash")                      // 指定通义千问模型
                .build();

        // 2. 在构建Agent时注册工具
        Toolkit toolkit = new Toolkit();
        //去MCP Marketplace(https://mcp.higress.ai) 找个Mcp Server，比如黄历/假期助手 （https://mcp.higress.ai/server/server9007）
        McpClientBuilder mcpClientBuilder = McpClientBuilder.create("mcp-calendar-holiday-helper").sseTransport("{your mcp url}");
        McpClientWrapper mcpClient = mcpClientBuilder.buildAsync().block();
        toolkit.registerMcpClient(mcpClient).block();

        // 3. 构建 ReActAgent
        ReActAgent agent = ReActAgent.builder()
                .name("MyAssistant")// 智能体名字
                .toolkit(toolkit) // 注入工具包
                .sysPrompt("你是一个友好的AI助手，名叫小助手。") // 系统提示词，定义角色
                .model(model)                              // 注入模型
                .build();

        // 4. 接受用户输入
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== AI助手已启动 (输入 'quit' 或 'exit' 退出) ===");

        while (true) {
            System.out.print("\n请输入您的问题: ");
            String userInput = scanner.nextLine();

            if ("quit".equalsIgnoreCase(userInput.trim()) || "exit".equalsIgnoreCase(userInput.trim())) {
                System.out.println("感谢使用，再见！");
                break;
            }

            if (userInput.trim().isEmpty()) {
                continue;
            }

            try {
                // 创建用户消息
                Msg userMsg = Msg.builder()
                        .role(MsgRole.USER)
                        .textContent(userInput)
                        .build();

                // 调用agent处理
                System.out.println("\n思考中...");
                Msg response = agent.call(userMsg).block();

                // 输出回复
                if (response != null && response.getContent() != null) {
                    System.out.println("\n小助手: " + response.getContent());
                } else {
                    System.out.println("\n小助手: 抱歉，我没有收到回复。");
                }
            } catch (Exception e) {
                System.err.println("处理请求时出错: " + e.getMessage());
                e.printStackTrace();
            }
        }

        scanner.close();
    }
}
