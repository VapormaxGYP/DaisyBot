package com.vapor.daisybot.BotService.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vapor.daisybot.BotService.AIChat;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AIChatImpl implements AIChat {

    @Override
    public String genChatMessage(String token, String words) {
        Response response;
        String responseBody;
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .readTimeout(3, TimeUnit.HOURS)
                .connectTimeout(3, TimeUnit.HOURS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"messages\": [\n    {\n      \"content\": \"" + words + "\",\n      \"role\": \"system\"\n    }\n],\n  \"model\": \"deepseek-chat\",\n  \"frequency_penalty\": 0,\n  \"max_tokens\": 2048,\n  \"presence_penalty\": 0,\n  \"response_format\": {\n    \"type\": \"text\"\n  },\n  \"stop\": null,\n  \"stream\": false,\n  \"stream_options\": null,\n  \"temperature\": 1,\n  \"top_p\": 1,\n  \"tools\": null,\n  \"tool_choice\": \"none\",\n  \"logprobs\": false,\n  \"top_logprobs\": null\n}");
        Request request = new Request.Builder()
                .url("https://api.deepseek.com/chat/completions")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", token)
                .build();

        log.info(request.body().toString());

        try {
             response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
             responseBody = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject responseBodyJson = JSONObject.parseObject(responseBody);
        JSONArray choiceArray = JSONArray.parseArray(responseBodyJson.getString("choices"));
        log.info(choiceArray.toString());

        JSONObject resultJson = JSONObject.parseObject(choiceArray.getString(0));
        log.info(resultJson.toString());

        String result = resultJson.getJSONObject("message").getString("content");

        return result;
    }
}
