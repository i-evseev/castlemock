/*
 * Copyright 2016 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.castlemock.core.mock.websocket.stomp.model.project.service.message.input;

import com.castlemock.core.basis.model.Input;
import com.castlemock.core.basis.model.validation.NotNull;

/**
 * @author Karl Dahlgren
 * @since 1.5
 */
public class ReadWebSocketStompProjectInput implements Input {

    @NotNull
    private String webSocketStompProjectId;

    public ReadWebSocketStompProjectInput(String webSocketStompProjectId) {
        this.webSocketStompProjectId = webSocketStompProjectId;
    }

    public String getWebSocketStompProjectId() {
        return webSocketStompProjectId;
    }

    public void setWebSocketStompProjectId(String webSocketStompProjectId) {
        this.webSocketStompProjectId = webSocketStompProjectId;
    }
}
