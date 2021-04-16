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

import com.google.gson.Gson;
import com.slack.api.model.File;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;
import org.nuxeo.labs.slack.service.SlackService;

@Operation(
        id = UploadFileOp.ID, category = Constants.CAT_NOTIFICATION,
        label = "Send Slack Notification", description = "Upload a file to slack. Returns a json object wrapped in a string blob"
)
public class UploadFileOp {

    public static final String ID = "Slack.UploadFile";

    @Context
    protected CoreSession session;

    @Context
    protected SlackService slackService;

    @OperationMethod
    public Blob run(Blob blob) {
        File slackFile = slackService.uploadFile(blob);
        String jsonStr =  new Gson().toJson(slackFile);
        return new StringBlob(jsonStr,"application/json");
    }
}
