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

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.core.util.StringList;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.labs.slack.SlackFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.nuxeo.labs.slack.SlackFeature.TEST_BLOCKS;
import static org.nuxeo.labs.slack.SlackFeature.TEST_MESSAGE;
import static org.nuxeo.labs.slack.SlackFeature.TEST_PUBLIC_CHANNEL;
import static org.nuxeo.labs.slack.SlackFeature.TEST_USER_NAME;

@RunWith(FeaturesRunner.class)
@Features({AutomationFeature.class, SlackFeature.class})
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy("org.nuxeo.labs.slack.core")
public class TestSendSlackNotificationOp {

    @Inject
    protected CoreSession session;

    @Inject
    protected AutomationService automationService;

    @Before
    public void tokenIsConfigured() {
        Assume.assumeTrue(SlackFeature.tokenIsConfigured());
    }

    @Test
    public void sendSimpleMessageToChannel() throws OperationException {
        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        params.put("channel", TEST_PUBLIC_CHANNEL);
        params.put("message",TEST_MESSAGE);
        automationService.run(ctx, SendSlackNotificationOp.ID, params);
    }

    @Test
    public void sendBlocksToChannel() throws OperationException {
        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        params.put("channel", TEST_PUBLIC_CHANNEL);
        params.put("message",TEST_MESSAGE);
        params.put("blocks",TEST_BLOCKS);
        automationService.run(ctx, SendSlackNotificationOp.ID, params);
    }

    @Test
    public void sendNotificationToUser() throws OperationException {
        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        params.put("nuxeoUsernames", new StringList(List.of(TEST_USER_NAME)));
        params.put("message",TEST_MESSAGE);
        automationService.run(ctx, SendSlackNotificationOp.ID, params);
    }

}
