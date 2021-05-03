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

import com.slack.api.app_backend.SlackSignature;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.restapi.test.RestServerFeature;
import org.nuxeo.labs.slack.SlackFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.ServletContainerFeature;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RunWith(FeaturesRunner.class)
@Features({SlackFeature.class, RestServerFeature.class})
@RepositoryConfig(cleanup = Granularity.METHOD)
public class TestInteractivityEndpoint {

    public static final String BASE_URL = "http://localhost";

    @Inject
    protected ServletContainerFeature servletContainerFeature;

    @Test
    public void testCorrectSignature() throws IOException {
        String nowTimeStamp = ""+System.currentTimeMillis();
        String secret = SlackFeature.getSigningSecret();
        String body = IOUtils.toString(getClass().getResourceAsStream("/files/command_message.txt"),
                StandardCharsets.UTF_8.name());

        SlackSignature.Generator generator = new SlackSignature.Generator(secret);
        String signature = generator.generate(nowTimeStamp,body);

        int port = servletContainerFeature.getPort();
        String url = BASE_URL + ":"+port+"/slack";
        HttpClient client = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(body));
        post.setHeader(SlackSignature.HeaderNames.X_SLACK_REQUEST_TIMESTAMP,nowTimeStamp);
        post.setHeader(SlackSignature.HeaderNames.X_SLACK_SIGNATURE,signature);

        HttpResponse response = client.execute(post);
        Assert.assertEquals(200,response.getStatusLine().getStatusCode());
    }

    @Test
    public void testIncorrectSignature() throws IOException {
        String nowTimeStamp = ""+System.currentTimeMillis();
        String signature = "abdc";

        int port = servletContainerFeature.getPort();
        String url = BASE_URL + ":"+port+"/slack";
        HttpClient client = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity("hahahaha"));
        post.setHeader(SlackSignature.HeaderNames.X_SLACK_REQUEST_TIMESTAMP,nowTimeStamp);
        post.setHeader(SlackSignature.HeaderNames.X_SLACK_SIGNATURE,signature);

        HttpResponse response = client.execute(post);
        Assert.assertEquals(401,response.getStatusLine().getStatusCode());
    }


}
