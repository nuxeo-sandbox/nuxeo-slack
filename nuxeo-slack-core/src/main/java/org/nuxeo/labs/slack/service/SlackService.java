/*
 * (C) Copyright 2021 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Michael Vachette
 */

package org.nuxeo.labs.slack.service;

import com.slack.api.Slack;

import java.util.List;

public interface SlackService {

    /**
     *
     * @return an instance of the Slack Client
     */
    Slack getSlackClient();

    /**
     * Send the message to the channel
     * @param channel
     * @param message
     */
    void sendMessageToChannel(String channel, String message);

    /**
     *
     * @param email
     * @return
     */
    String getSlackUserId(String email);

    /**
     *
     * @param emailList
     * @return
     */
    List<String> getSlackUserIds(List<String> emailList);

    /**
     *
     * @param slackUserId
     * @param message
     */
    void sendMessageToUser(String slackUserId, String message);

    /**
     *
     * @param slackUserIds
     * @param message
     */
    void sendMessageToUsers(List<String> slackUserIds, String message);
}
