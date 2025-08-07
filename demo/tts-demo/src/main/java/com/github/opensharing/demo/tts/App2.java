package com.github.opensharing.demo.tts;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

import com.alibaba.dashscope.audio.tts.SpeechSynthesisResult;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisAudioFormat;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer;
import com.alibaba.dashscope.common.ResultCallback;

class TimeUtils2 {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static String getTimestamp() {
        return LocalDateTime.now().format(formatter);
    }
}


public class App2 {
    private static final String[] textArray = {"流式文本语音合成SDK，",
            "可以将输入的文本", "合成为语音二进制数据，", "相比于非流式语音合成，",
            "流式合成的优势在于实时性", "更强。用户在输入文本的同时",
            "可以听到接近同步的语音输出，", "极大地提升了交互体验，",
            "减少了用户等待时间。", "适用于调用大规模", "语言模型（LLM），以",
            "流式输入文本的方式", "进行语音合成的场景。"};
    private static final String model = "cosyvoice-v2"; // 模型
    private static final String voice = "longxiaochun_v2"; // 音色

    public static void streamAudioDataToSpeaker() {
        CountDownLatch latch = new CountDownLatch(1);

        // 配置回调函数
        ResultCallback<SpeechSynthesisResult> callback = new ResultCallback<SpeechSynthesisResult>() {
            @Override
            public void onEvent(SpeechSynthesisResult result) {
                // System.out.println("收到消息: " + result);
                if (result.getAudioFrame() != null) {
                    // 此处实现处理音频数据的逻辑
                    System.out.println(TimeUtils2.getTimestamp() + " 收到音频");
                }
            }

            @Override
            public void onComplete() {
                System.out.println(TimeUtils2.getTimestamp() + " 收到Complete，语音合成结束");
                latch.countDown();
            }

            @Override
            public void onError(Exception e) {
                System.out.println("出现异常：" + e.toString());
                latch.countDown();
            }
        };

        // 请求参数
        SpeechSynthesisParam param =
                SpeechSynthesisParam.builder()
                        // 若没有将API Key配置到环境变量中，需将下面这行代码注释放开，并将your-api-key替换为自己的API Key
                        .apiKey("sk-XXXX")
                        .model(model)
                        .voice(voice)
                        .format(SpeechSynthesisAudioFormat
                                .PCM_22050HZ_MONO_16BIT) // 流式合成使用PCM或者MP3
                        .build();
        SpeechSynthesizer synthesizer = new SpeechSynthesizer(param, callback);
        // 带Callback的call方法将不会阻塞当前线程
        for (String text : textArray) {
            // 发送文本片段，在回调接口的onEvent方法中实时获取二进制音频
            synthesizer.streamingCall(text);
        }
        // 结束流式语音合成
        synthesizer.streamingComplete();
        System.out.println(
                "[Metric] requestId为："
                        + synthesizer.getLastRequestId()
                        + "，首包延迟（毫秒）为："
                        + synthesizer.getFirstPackageDelay());
        // 等待合成完成
        try {
            latch.await();
            // 等待播放线程全部播放完
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        streamAudioDataToSpeaker();
        System.exit(0);
    }
}
