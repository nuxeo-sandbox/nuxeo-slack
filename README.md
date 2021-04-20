# Description

This repository contains a plugin to post messages on Slack from the Nuxeo Platform

# How to build

```
git clone https://github.com/nuxeo-sandbox/nuxeo-slack
cd nuxeo-slack
mvn clean install
```

# Slack App Configuration

* Go to https://api.slack.com/apps
* Create a new app
* Copy the `Bot User OAuth Token`
* Open the **OAuth & Permissions** menu
  * Add the following scopes 
    * channels:read 
    * chat:write 
    * chat:write.public
    * groups:write
    * im:write
    * mpim:write
    * users:read
    * users:read.email
    * files:write
  
# Nuxeo Configuration

In nuxeo.conf, set the following property

```
nuxeo.slack.token=<Bot User OAuth Token>
```

# Features

This plugin contains two automation operations.

## Post message

The [Notification.SendSlackNotification](https://github.com/nuxeo-sandbox/nuxeo-slack/blob/master/nuxeo-slack-core/src/main/java/org/nuxeo/labs/slack/automation/SendSlackNotificationOp.java) operation takes three parameters:
* `channel`: the channel name where to post. Ignored if the nuxeoUsername parameter is used
* `nuxeoUsernames`: a list of nuxeo usernames. For each user, the email address must be set in the user profile in Nuxeo and must be the same as the user's email in the target slack workspace
* `message`: the message to post
* `blocks`: json string containing a message built with the [Block Kit UI framework](https://api.slack.com/block-kit)

## Upload File

The [Slack.UploadFile](https://github.com/nuxeo-sandbox/nuxeo-slack/blob/master/nuxeo-slack-core/src/main/java/org/nuxeo/labs/slack/automation/UploadFileOp.java) takes a blob as the input and returns a String Blob containing a [slack file object](https://api.slack.com/types/file)

# Samples
* [workflow task notification](https://github.com/nuxeo-sandbox/nuxeo-slack/blob/master/documentation/sample-automation-script.js), send a slack notification on the workflowTaskAssigned event

# Known limitations
This plugin is a work in progress.

# Support

**These features are not part of the Nuxeo Production platform.**

These solutions are provided for inspiration and we encourage customers to use them as code samples and learning resources.

This is a moving project (no API maintenance, no deprecation process, etc.) If any of these solutions are found to be useful for the Nuxeo Platform in general, they will be integrated directly into platform, not maintained here.

# Nuxeo Marketplace
This plugin is published on the [marketplace](https://connect.nuxeo.com/nuxeo/site/marketplace/package/nuxeo-slack)

# License

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

# About Nuxeo

Nuxeo Platform is an open source Content Services platform, written in Java. Data can be stored in both SQL & NoSQL databases.

The development of the Nuxeo Platform is mostly done by Nuxeo employees with an open development model.

The source code, documentation, roadmap, issue tracker, testing, benchmarks are all public.

Typically, Nuxeo users build different types of information management solutions for [document management](https://www.nuxeo.com/solutions/document-management/), [case management](https://www.nuxeo.com/solutions/case-management/), and [digital asset management](https://www.nuxeo.com/solutions/dam-digital-asset-management/), use cases. It uses schema-flexible metadata & content models that allows content to be repurposed to fulfill future use cases.

More information is available at [www.nuxeo.com](https://www.nuxeo.com)
