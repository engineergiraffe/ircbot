package ircbot.plugin

import ircbot.plugin.base.Plugin

case object Echo extends Plugin {

    override def trigger(message: String): Boolean = true

    override def action(message: String, sender: String, channel: Option[String]): (Option[String], Option[String]) = {
      return channel match {
        case Some(ch: String) => (Some("#" + ch), Some(sender + ": " + message))
        case None => (Some(sender), Some(message))
      }
    }


}
