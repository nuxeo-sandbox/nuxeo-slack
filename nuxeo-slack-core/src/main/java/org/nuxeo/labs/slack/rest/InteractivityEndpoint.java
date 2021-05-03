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

package org.nuxeo.labs.slack.rest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.EventContextImpl;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.labs.slack.service.SlackService;
import org.nuxeo.runtime.api.Framework;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * WebEngine module to handle the Slack interactivity messages
 */
@Path("/slack")
@WebObject(type = "slack")
public class InteractivityEndpoint extends ModuleRoot {

    protected static final Log log = LogFactory.getLog(InteractivityEndpoint.class);

    @Context
    private HttpServletRequest request;

    @Path("/")
    @POST
    public Object doPost(@Context HttpServletRequest request) throws IOException {
        String signature = request.getHeader("X-Slack-Signature");
        String timestamp = request.getHeader("X-Slack-Request-Timestamp");
        String body = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8.name());
        SlackService slackService = Framework.getService(SlackService.class);
        boolean isValidRequest = slackService.isValidRequest(body,timestamp,signature);

        if (!isValidRequest) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        slackService.processSlackMessage(body);

        return Response.status(Response.Status.OK).build();
    }

}
