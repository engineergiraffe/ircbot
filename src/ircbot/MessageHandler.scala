package ircbot

import scala.actors.Actor
import scala.actors.Actor._
import ircbot.plugin.base.{Plugin, PluginLoader}

case object MessageHandler extends Actor {

    val config = new Configuration("conf/config.xml")
    val plugins: List[Plugin] = config.plugins.map(PluginLoader.getClass(_))

    def act() {
        loop {
            react {
                case (message: String, nick: String) => {
                  plugins.filter(p => p.trigger(message)).foreach(sender ! _.takeAction(message, nick))
                }
            }
        }
    }

}
