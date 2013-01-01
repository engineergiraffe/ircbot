This is very simple IRC bot written using scala. My first attempt to make any software with scala and using functional programming.

Compile and run:
```bash
scalac src/ircbot/{*.scala,plugin/base/*.scala,connection/*.scala} -d classes
scala -cp classes ircbot.Bot
```

Compile plugin (Example compiling Echo.scala plugin)
```bash
scalac src/ircbot/plugin/{base/Plugin.scala,Echo.scala} -d classes
```

