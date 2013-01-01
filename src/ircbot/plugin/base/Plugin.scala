package ircbot.plugin.base

abstract class Plugin {
    // Value that defines regexp for messages sent to channel
    // sender nickname | user host | channel | receiver nickname | message
    val channelPattern = """:(.*)!(.*) PRIVMSG #(.*) :(.*):(.*)""".r

    // Value that defines regexp for messages sent to user
    // sender nickname | user host | receiver nickname | message
    val privatePattern = """:(.*)!(.*) PRIVMSG (.*) :(.*)""".r

    def takeAction(message: String, nick: String): (Option[String], Option[String]) = {
        message match {
          case channelPattern(sender_nick, sender_host, channel, receiver, msg) if nick.equals(receiver.trim) => action(msg, sender_nick, Some(channel))
          case privatePattern(sender_nick, sender_host, receiver, msg) if nick.equals(receiver.trim) => action(msg, sender_nick, None)
          case _ => (None, None)
        }
    }

    /**
     * Description: Defines when plugin triggers action.
     *
     * Params: message, String, Message from IRC stream. Using this string plugin must decide if this contains trigger.
     * Return: Boolean, boolean value that represents that is plugin triggered.
     */
    def trigger(message: String): Boolean = throw new UnsupportedOperationException("not yet implemented")

    /**
     * Description: Plugins main actions that triggered using message.
     *
     * Return: Option[String], if Some(String) returned that would be echoed back to stream. If it is None nothing will happen.
     */
    def action(message: String, sender: String, channel: Option[String]): (Option[String], Option[String]) = throw new UnsupportedOperationException("not yet implemented")

}

