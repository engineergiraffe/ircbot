package ircbot

import scala.actors.Actor
import scala.actors.Actor._
//import ircbot.plugin.base.{Plugin, PluginLoader}

case object MessageHandler extends Actor {

    def act() {
        loop {
            react {
                case message: String => reply(str)
                case _ => 
            }
        }
    }

}
