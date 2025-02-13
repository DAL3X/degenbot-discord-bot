# General
My personal fun discord bot to monitor twitch channels and send custom go-live notifications to a target channel.
Can be used on multiple servers for monitoring multiple twitch channels.

It also has the capabilities to "bacon track" users. The bot will react with a bacon emoji every 24 hours, when a tracked user sends a message.
# Environment
The software is configured to use local environment files, but can be configured to use system environments. To do so, change _useLocalEnvFile_ in _DegenBot.java_ to _false_. 
The repository includes an example environment file, that needs to be placed one folder above (*../*) the executable .jar file when using local environment files.
# Build
Use gradle and the provided gradle config files to import and build the project.
To export a built jar file, use the gradle _jar_ task.
