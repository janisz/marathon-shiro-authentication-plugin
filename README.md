# [Shiro](http://shiro.apache.org/) authentication plugin for [Marathon](https://mesosphere.github.io/marathon/) [![Coverage Status](https://coveralls.io/repos/janisz/marathon-shiro-authentication-plugin/badge.svg?branch=shiro_authenticator&service=github)](https://coveralls.io/github/janisz/marathon-shiro-authentication-plugin?branch=master) [![Build Status](https://travis-ci.org/janisz/marathon-shiro-authentication-plugin.svg?branch=master)](https://travis-ci.org/janisz/marathon-shiro-authentication-plugin) [![Codacy Badge](https://api.codacy.com/project/badge/3ca99746b9c142ef83e8ffcdbe693ca3)](https://www.codacy.com/app/janiszt/marathon-shiro-authentication-plugin) 



Plugin is based on [HTTP Basic access authentication](https://en.wikipedia.org/wiki/Basic_access_authentication).
Currently it supports only authentication, so any authenticated user ca do anything. I'm working on
providing fine grained authorization based on paths, actions and Shiro roles.

_Always set up HTTPS for your Marathon instance before using this plugin._

## Configuration

Read Marathon docs about Plugins interface and configuration flags.
To configure this plugin you need to create [Shiro configuration file](http://shiro.apache.org/configuration.html)
and place it somewhere in the system (e.g., `/etc/marathon/shiro.ini`).
 
 - Put `marathon-shiro-authentication-plugin.jar` in your plugins directory and
 provide path to this directory using `--plugin_dir` flag
 - Create configuration JSON for Marathon (e.g., in `/etc/marathon/plugin_conf.json`)
 
 ```json
 {
   "plugins": [
     {
       "plugin": "mesosphere.marathon.plugin.auth.Authorizer",
       "implementation": "tech.allegro.marathon.plugin.auth.ShiroAuthorizer"
     },
     {
       "plugin": "mesosphere.marathon.plugin.auth.Authenticator",
       "implementation": "tech.allegro.marathon.plugin.auth.ShiroAuthenticator",
       "configuration": {
         "path": "/etc/marathon/shiro.ini"
       }
     }
   ]
 }
 ```
 
- Set `--plugin_conf` to pathe where you put configuration JSON (e.g., `/etc/marathon/shiro.ini`)