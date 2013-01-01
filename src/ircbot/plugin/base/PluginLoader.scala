package ircbot.plugin.base

case object PluginLoader extends ClassLoader {

    def getClass(name: String): Plugin = { 
        val cons = Class.forName("ircbot.plugin." + name + "$").getDeclaredConstructors
        cons(0).setAccessible(true)
        cons(0).newInstance().asInstanceOf[Plugin]
    }
}

