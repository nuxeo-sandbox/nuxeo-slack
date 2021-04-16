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

package org.nuxeo.labs.slack;

import org.apache.commons.lang3.StringUtils;
import org.junit.runners.model.FrameworkMethod;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.usermanager.NuxeoPrincipalImpl;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.RunnerFeature;

import static org.nuxeo.labs.slack.service.SlackServiceImpl.NUXEO_SLACK_TOKEN_PROPERTY;

public class SlackFeature implements RunnerFeature {

    public static final String TEST_USER_NAME = "testUser";
    public static final String TEST_USER_EMAIL = "nuxeo.slack.test@gmail.com";

    public static final String TEST_PUBLIC_CHANNEL = "general";
    public static final String TEST_MESSAGE = "Hello! This is Nuxeo Automation";
    public static final String TEST_BLOCKS = "" +
            "   [{\n" +
            "      \"type\":\"section\",\n" +
            "      \"text\":{\n" +
            "         \"type\":\"mrkdwn\",\n" +
            "         \"text\":\"Hello, Assistant to the Regional Manager Dwight! *Michael Scott* wants to know where you'd like to take the Paper Company investors to dinner tonight.\\n\\n *Please select a restaurant:*\"\n" +
            "      }\n" +
            "   }\n" +
            "]";

    @Override
    public void beforeSetup(FeaturesRunner runner, FrameworkMethod method, Object test) {
        UserManager um = Framework.getService(UserManager.class);
        NuxeoPrincipal nuxeoPrincipal = new NuxeoPrincipalImpl(TEST_USER_NAME);
        nuxeoPrincipal.setEmail(TEST_USER_EMAIL);
        um.createUser(nuxeoPrincipal.getModel());
    }

    @Override
    public void beforeRun(FeaturesRunner runner) {
        Framework.getProperties().setProperty(NUXEO_SLACK_TOKEN_PROPERTY,System.getProperty("nuxeo.slack.token"));
    }

    public static boolean tokenIsConfigured() {
        return StringUtils.isNotEmpty(Framework.getProperty(NUXEO_SLACK_TOKEN_PROPERTY));
    }
}
