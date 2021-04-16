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

package org.nuxeo.labs.slack.automation;

import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.StringList;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.labs.slack.service.SlackService;

import java.util.List;
import java.util.stream.Collectors;

@Operation(
        id = SendSlackNotificationOp.ID, category = Constants.CAT_NOTIFICATION,
        label = "Send Slack Notification", description = "Send a Slack Notification to a list of users or a channel"
)
public class SendSlackNotificationOp {

    public static final String ID = "Notification.SendSlackNotification";

    @Context
    protected CoreSession session;

    @Context
    protected SlackService slackService;

    @Context
    protected UserManager userManager;

    @Param(name = "channel", required = false)
    protected String channel;

    @Param(name = "nuxeoUsernames", required = false)
    protected StringList nuxeoUsernames = new StringList();

    @Param(name = "message")
    protected String message;

    @Param(name = "blocks", required = false)
    protected String blocks;

    @OperationMethod
    public void run() {
        if (!nuxeoUsernames.isEmpty()) {
            List<String> emails = nuxeoUsernames.stream().map(
                    username -> userManager.getPrincipal(username).getEmail()
            ).collect(Collectors.toList());
            List<String> slackIds = slackService.getSlackUserIds(emails);
            slackService.sendMessageToUsers(slackIds, message, blocks);
        } else if (StringUtils.isNotEmpty(channel)) {
            slackService.sendMessageToChannel(channel, message, blocks);
        }
    }
}
