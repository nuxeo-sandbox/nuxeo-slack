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
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsOpenResponse;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.runtime.model.Extension;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SlackServiceImpl extends DefaultComponent implements SlackService {

    public static final String NUXEO_SLACK_TOKEN_PROPERTY = "nuxeo.slack.token";

    /**
     * volatile on purpose to allow for the double-checked locking idiom
     */
    protected volatile Slack slack;

    @Override
    public Slack getSlackClient() {
        Slack result = slack;
        if (result == null) {
            synchronized (this) {
                result = slack;
                if (result == null) {
                    result = slack = Slack.getInstance();
                }
            }
        }
        return result;
    }

    protected String getToken() {
        return Framework.getProperty(NUXEO_SLACK_TOKEN_PROPERTY);
    }

    /**
     * Component activated notification.
     * Called when the component is activated. All component dependencies are resolved at that moment.
     * Use this method to initialize the component.
     *
     * @param context the component context.
     */
    @Override
    public void activate(ComponentContext context) {
        super.activate(context);
    }

    /**
     * Component deactivated notification.
     * Called before a component is unregistered.
     * Use this method to do cleanup if any and free any resources held by the component.
     *
     * @param context the component context.
     */
    @Override
    public void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    /**
     * Registers the given extension.
     *
     * @param extension the extension to register
     */
    @Override
    public void registerExtension(Extension extension) {
        super.registerExtension(extension);
    }

    /**
     * Unregisters the given extension.
     *
     * @param extension the extension to unregister
     */
    @Override
    public void unregisterExtension(Extension extension) {
        super.unregisterExtension(extension);
    }

    /**
     * Start the component. This method is called after all the components were resolved and activated
     *
     * @param context the component context. Use it to get the current bundle context
     */
    @Override
    public void start(ComponentContext context) {
        // do nothing by default. You can remove this method if not used.
    }

    /**
     * Stop the component.
     *
     * @param context the component context. Use it to get the current bundle context
     */
    @Override
    public void stop(ComponentContext context) {
        // do nothing by default. You can remove this method if not used.
    }

    @Override
    public void sendMessageToChannel(String channel, String message) {
        Slack slack = getSlackClient();
        try {
            ChatPostMessageResponse response = slack.methods(getToken()).chatPostMessage(req -> req
                    .channel(channel)
                    .text(message));
            if (!response.isOk()) {
                throw new NuxeoException(response.getError());
            }
        } catch (IOException | SlackApiException e) {
            throw new NuxeoException(e);
        }
    }

    @Override
    public String getSlackUserId(String email) {
        return getSlackUserIds(List.of(email)).get(0);
    }

    @Override
    public List<String> getSlackUserIds(List<String> emailList) {
        Slack slack = getSlackClient();
        return emailList.stream().map(email -> {
            try {
                UsersLookupByEmailResponse response = slack.methods(getToken()).usersLookupByEmail(req -> req.email(email));
                if (response.isOk()) {
                    return response.getUser().getId();
                } else {
                    throw new NuxeoException("Couldn't get user for "+email+", "+response.getError());
                }
            } catch (IOException | SlackApiException e) {
                throw new NuxeoException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public void sendMessageToUser(String slackUserId, String message) {
        sendMessageToChannel(slackUserId, message);
    }

    @Override
    public void sendMessageToUsers(List<String> slackUserIds, String message) {
        Slack slack = getSlackClient();
        try {
            ConversationsOpenResponse conversation = slack.methods(getToken()).conversationsOpen(req -> req.users(slackUserIds));
            if (conversation.isOk()) {
                sendMessageToChannel(conversation.getChannel().getId(), message);
            } else {
                throw new NuxeoException("Couldn't open conversation "+conversation.getError());
            }
        } catch (IOException | SlackApiException e) {
            throw new NuxeoException(e);
        }
    }
}
