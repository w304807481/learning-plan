package com.geek.tools;

import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;

public class WeatherTools {
    @Tool(description = "获取指定城市的天气信息")
    public String getWeather(
            @ToolParam(name = "city", description = "城市名称") String city) {
        // 这里可以调用真实的天气API，现在模拟一个返回
        return String.format("%s今天的天气是晴朗，温度25°C。", city);
    }
}
