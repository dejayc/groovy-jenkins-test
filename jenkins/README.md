# Pull Request Pipeline
## Jenkins Setup
Set up a Jenkins Pipeline Project. There are currently two pipelines.

| Pipeline | Runs | URL |
|--|--|--|
| [PR Test](pull_request.groovy) | tests, linter, sonar (later) | [link](https://ea.cnbc.com/redesign/job/WEB.PR_Test_Pipeline/) |
| [PR Quick](pull_request_quick.groovy) | Danger | [link](https://ea.cnbc.com/redesign/job/WEB.PR_Quick_Pipeline/) |

**PR Test Setup**

* In Pipeline, set `Script Path` to `jenkins/pull_request.groovy`
* Under `Optional filter`
  * Set `Expression` to `^(opened|reopened|synchronize)*?$`
  * Set `Text` to `$PR_ACTION`

**PR Quick Setup**

* In Pipeline, set `Script Path` to `jenkins/pull_request_quick.groovy`

Set GitHub project to this repo

Under **Build Triggers**, enable Generic Webhook Trigger and set the following variables:

| Variable | JSONPath |
|--|--|
| CHANGE_URL | $.pull_request.html_url |
| SOURCE_BRANCH | $.pull_request.head.ref |
| PR_ACTION | $.action |

Add a Token, and set up corresponding webhook on GitHub.com for your repo.

Set the Cause: `Pull Request $PR_ACTION $CHANGE_URL`

Under **Optional Filter**, set Expression of `^(opened|reopened|labeled|synchronize)*?$` and Text of `$PR_ACTION`

In the **Pipeline** configuration, add GitHub credentials and set Branch Specifier to `$SOURCE_BRANCH`, set Script Path to `jenkins/pull_request.groovy` and *disable* Lightweight checkout.
