package ircbot.plugin.base

case class PluginLoader()

object PluginLoader extends ClassLoader {

    def getClass(name: String): Plugin = loadClass("ircbot.plugin." + name).newInstance.asInstanceOf[Plugin]

}

