package ircbot

import scala.xml._

import ircbot.connection._

object Bot {

    def main(args: Array[String]) {
        val config = new Configuration("conf/config.xml")
        config.networks.map(network => (new Thread(new IRCConnection(new NetworkConfiguration(network), config.debug)).start))
    }

}
