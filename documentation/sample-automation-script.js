function run(input, params) {

    if (!Env.getProperty('nuxeo.slack.token')) {
        Console.log("Slack Token is not configured. Skipping Notification");
    }

    // get text variables

    var assignees = ctx.Event.getContext().getProperty('recipients');

    var task = ctx.Event.getContext().getProperty('taskInstance');
    var taskName = task.getName()? SlackUtils.getTranslation(task.getName(),"en-US") : '';
    var directive = task.getDirective() ? SlackUtils.getTranslation(task.getDirective(),"en-US") : '';


    var process = Repository.GetDocument(input, {
        'value': task.getProcessId()
    });
    var processName = SlackUtils.getTranslation(process['dc:title'],"en-US");


    //get action urls
    var taskUrl = Env.getProperty('nuxeo.url')+"/ui/#!/tasks/"+task.getId();
    var assetUrl = Env.getProperty('nuxeo.url')+"/ui/#!/doc/"+input.id;


    var blockMessage = [
        {
            "type": "section",
            "text": {
                "type": "mrkdwn",
                "text": "You have a new task!"
            }
        },
        {
            "type": "section",
            "text": {
                "type": "mrkdwn",
                "text": "*<"+taskUrl+ "|" + processName + " - " + taskName + ">*\n<" + assetUrl + "|" + input['dc:title'] + ">\n" + directive
            }
        }
    ];

    try {
        Notification.SendSlackNotification(input, {
            'message':'Hello, you have a new task! '+taskUrl,
            'blocks': JSON.stringify(blockMessage),
            'nuxeoUsernames': assignees
        });
    } catch (error) {
        Console.log(error);
    }

}