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

import org.nuxeo.common.utils.i18n.I18NUtils;
import org.nuxeo.ecm.automation.context.ContextHelper;

import java.util.Locale;

public class SlackFunctions implements ContextHelper {

    public String getTranslation(String key, String locale) {
        return I18NUtils.getMessageString("messages",key,new Object[0], new Locale(locale));
    }

}
