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

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.labs.slack.SlackFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.nuxeo.labs.slack.SlackFeature.TEST_MESSAGE;
import static org.nuxeo.labs.slack.SlackFeature.TEST_PUBLIC_CHANNEL;
import static org.nuxeo.labs.slack.SlackFeature.TEST_USER_EMAIL;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class, SlackFeature.class })
@Deploy("org.nuxeo.labs.slack.core")
public class TestSlackService {

    @Inject
    protected SlackService slackservice;

    @Before
    public void tokenIsConfigured() {
        Assume.assumeTrue(SlackFeature.tokenIsConfigured());
    }

    @Test
    public void testService() {
        assertNotNull(slackservice);
    }

    @Test
    public void testSendMessageToPublicChannel() {
        slackservice.sendMessageToChannel(TEST_PUBLIC_CHANNEL,TEST_MESSAGE);
    }

    @Test
    public void testGetSlackUserId() {
        String userId = slackservice.getSlackUserId(TEST_USER_EMAIL);
        Assert.assertTrue(StringUtils.isNotEmpty(userId));
    }

    @Test
    public void testSendMessageToUser() {
        String userId = slackservice.getSlackUserId(TEST_USER_EMAIL);
        slackservice.sendMessageToChannel(userId,TEST_MESSAGE);
    }

}
