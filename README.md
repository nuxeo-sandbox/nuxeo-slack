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
  
# Nuxeo Configuration

In nuxeo.conf, set the following property

```
nuxeo.slack.token=<Bot User OAuth Token>
```

# Features

This plugin contains an [automation operation](https://github.com/nuxeo-sandbox/nuxeo-slack/blob/master/nuxeo-slack-core/src/main/java/org/nuxeo/labs/slack/automation/SendSlackNotificationOp.java) to post messages on Slack

## Nuxeo Marketplace
This plugin is published on the [marketplace](https://connect.nuxeo.com/nuxeo/site/marketplace/package/nuxeo-slack)

# Known limitations

This plugin is a work in progress.

# About Nuxeo

[Nuxeo](www.nuxeo.com), developer of the leading Content Services Platform, is reinventing enterprise content management (ECM) and digital asset management (DAM). Nuxeo is fundamentally changing how people work with data and content to realize new value from digital information. Its cloud-native platform has been deployed by large enterprises, mid-sized businesses and government agencies worldwide. Customers like Verizon, Electronic Arts, ABN Amro, and the Department of Defense have used Nuxeo's technology to transform the way they do business. Founded in 2008, the company is based in New York with offices across the United States, Europe, and Asia.

Learn more at www.nuxeo.com.
