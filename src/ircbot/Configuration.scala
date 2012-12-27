package ircbot

import scala.xml._

class Configuration(path: String) {

    val xml: Elem = XML.loadFile(path)

    // Value that defines is debug mode on or off.
    val debug: Boolean = (xml \ "debug" \ "@on").text.toBoolean

    // Value that contain connection block.
    val connection: NodeSeq = (xml \ "connection")

    // Value that contains list of all networks.
    val networks: List[Node] = (connection \ "network").toList
}

class NetworkConfiguration(val network: Node) {
    /**
     * Description: Gives a NodeSeq that contains all channels that belongs network given parameter.
     **/
    val channels: List[String] = (network \ "channels" \ "channel").map(ch => (ch \ "@name").text).toList

    /**
     * Description: Gives nick that belongs to given network. If nick is longer than 9 chars then take only first 9 characters.
     **/
    val nick: String = {
        if((network \ "nick").text.length > 9) (network \ "nick").text.substring(0,9) else (network \ "nick").text
    }

    /**
     * Description: Gives user that belong to given network.
     **/
    val user: String = (network \ "user").text

    /**
     * Description: Gives a Tuple that contain nick and user that belong to given network.
     **/
    val nickAndUser: (String, String) = (nick, user)

    /**
     * Description: Gives hostname that belong to given network
     **/
    val host: String = (network \ "@name").text

    /**
     * Description: Gives port that belong to given network
     **/
    val port: Int = (network \ "@port").text.toInt
    
    /**
     * Description: Gives a Tuple that contain hostname and user that belong to given network.
     **/
    val hostAndPort: (String, Int) = (host, port)
}
