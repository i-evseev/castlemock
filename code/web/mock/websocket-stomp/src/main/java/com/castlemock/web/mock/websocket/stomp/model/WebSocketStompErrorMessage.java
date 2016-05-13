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

package com.castlemock.web.mock.websocket.stomp.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The WebSocketStompErrorMessage is an error response and contains the {@link WebSocketStompException}
 * exception message. The WebSocketStompErrorMessage will be converted into a WebSocket stomp message and returned to the service consumer.
 * @author Karl Dahlgren
 * @since 1.5
 * @see WebSocketStompException
 */
@XmlRootElement(name = "error-response")
public class WebSocketStompErrorMessage {

    private String message;

    /**
     * The default constructor for the SoapErrorMessage.
     */
    public WebSocketStompErrorMessage() {
    }

    /**
     * SoapErrorMessage constructor
     * @param message The error message that will be displayed to the service consumer
     */
    public WebSocketStompErrorMessage(final String message) {
        this.message = message;
    }

    /**
     * Returns the error message that will be displayed to the service consumer
     * @return The error message
     */
    @XmlElement(name = "error-message")
    public String getMessage() {
        return message;
    }

    /**
     * Sets a new value to the message variable
     * @param message The new message that will replace the old one
     */
    public void setMessage(final String message) {
        this.message = message;
    }
}
