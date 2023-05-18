/*
 * Copyright 2023 webtau maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demo.springboot.app.data;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class PriceWebSocketHandler extends TextWebSocketHandler {
    private boolean displayExtra;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        List<String> extras = session.getHandshakeHeaders().get("x-extra");
        displayExtra = extras != null && !extras.isEmpty();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, ?> request = JsonUtils.deserializeAsMap(message.getPayload());

        Object symbol = request.get("symbol");
        if (symbol.equals("IBM")) {
            sendIbm(session, request);
        } else if (symbol.equals("DUMMY")) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("symbol", request.get("symbol"));
            response.put("price", 0);

            session.sendMessage(new TextMessage(JsonUtils.serialize(response)));
        } else if (symbol.equals("LIST")) {
            Map<String, Object> priceLow = new LinkedHashMap<>();
            priceLow.put("symbol", "IBM");
            priceLow.put("price", 20);

            Map<String, Object> priceHigh = new LinkedHashMap<>();
            priceHigh.put("symbol", "IBM");
            priceHigh.put("price", 30);

            Map<String, Object> priceLive = new LinkedHashMap<>();
            priceLive.put("symbol", "IBM");
            priceLive.put("price", 33);

            String json = JsonUtils.serialize(Arrays.asList(priceLow, priceHigh, priceLive));
            session.sendMessage(new TextMessage(json));
        } else if (symbol.equals("GROOVY_DSL")) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("label", "hello label");
            response.put("destination", "hello destination");

            session.sendMessage(new TextMessage(JsonUtils.serialize(response)));
        }
    }

    private void sendIbm(WebSocketSession session, Map<String, ?> request) throws IOException, InterruptedException {
        for (int price = 77; price < 130; price++) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("symbol", request.get("symbol"));
            response.put("price", price);

            if (displayExtra) {
                response.put("extraData", 200);
            }

            session.sendMessage(new TextMessage(JsonUtils.serialize(response)));
            if (price < 100) {
                Thread.sleep(5);
            }
        }
    }
}
