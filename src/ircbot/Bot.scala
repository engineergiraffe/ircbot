package ircbot

import scala.xml._

import ircbot.connection._

object Bot {

    def main(args: Array[String]) {
        val xml = XML.loadFile("conf/config.xml")
        val debug = (xml \ "debug" \ "@on").text.toBoolean
        val port = (xml \ "connection" \ "network").map(network => createConnection(network, debug))
    }

    def createConnection(network: Node, debug: Boolean): Unit = {
        val name = (network \ "@name").text
        val port = (network \ "@port").text.toInt
        val nick = (network \ "nick").text match {
            case i if i.length > 9 => i.substring(0,9)
            case i if i.length <= 9 => i
        }
        val user = (network \ "user").text
        val channels = (network \ "channels")
        (new Thread(new IRCConnection(name, port, user, nick.toString, channels, debug))).start()
    }

}
