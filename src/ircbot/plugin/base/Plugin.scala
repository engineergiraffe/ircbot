package ircbot.plugin.base

trait Plugin {

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
    def action(): Option[String] = throw new UnsupportedOperationException("not yet implemented")

}

