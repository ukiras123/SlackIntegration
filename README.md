# SlackIntegration
Integrating different APIs to create a smart slack bot. Usage of Docker for deployment.

[![Build Status](https://travis-ci.org/ukiras123/SlackIntegration.svg?branch=master)](https://travis-ci.org/ukiras123/SlackIntegration/)


# Docker 
-  docker build -f Dockerfile -t ukiras123/slackintegration .
-  docker run -d --name='slack-bot' -v /var/log/slack:/var/log/slack -p 8080:8080 ukiras123/slackintegration

[Note: If you want to collborate: send an email to ukiras@gmail.com or fork this repo and create a PR.  
https://github.com/ukiras123/SlackIntegration/blob/master/CONTRIBUTING.md]

