package ircbot.scala.nikokauppila.net

/**
 * Description: Class handles all connection related tasks. Including sending messages, joining channels and answering pings.
 * Params:  host:   String  , Hostname to connect
 *          port:   Int     , Port to connect
 *          debug:  Boolean , Turns debug mode on and off
 **/
class IRCConnection(host: String, port: Int, debug: Boolean) extends Connection(host, port, debug){

  // TODO: Channel list must come from botconfic.xml or something like that.
  private val channels: List[String] = List("#testaillaanskaalabottia")

  /**
   * Description: Starts connection to irc server.
   *              Set USER and NICK.
   *              Invoke read method and print debug info if needed.
   **/
  def readSocket(): Unit = {
    if(connection.isConnected) {
      send(writeToSocket, "USER scalabot12345 skaalailevabotti eiole :Scala-bot")
      send(writeToSocket, "NICK scalabot12345")
      while(true) {
        read(readLineFromSocket) match { 
            case Some(line: String) => println(line)
            case None =>
        }
      }
    }
  }

  /**
   * Description: Decide what to do to line that we got from socket.
   *
   * Return: Some(String) if debug is set on, otherwise None.
   **/
  def readLineFromSocket(line: String): Option[String] =
    return line match {
      // If irc server send "PING :n", it must be answered with "PONG n".
      case i if (i split ":")(0).trim == "PING" => { 
                                    pong((i split ":")(1))
                                    if(debug) Some(i) else None
                                }
      // If line contains ":End of /MOTD" message, it certain that bot can joint to channels.
      case i if i contains ":End of /MOTD" => { 
                                    joinChannels
                                    if(debug) Some(i) else None
                                }
      // PRIVMSG indicated that some of channels have messages which bot must handle somehow. 
      // It can process message or reject it.
      case i if i contains "PRIVMSG" => { 
                                    handleMessage(i)
                                    if(debug) Some(i) else None
                                }
      // This is just for debugging application.
      case i if (debug && i != null)  => Some(i)
      case _ => None
    }

  /**
   * Description: Add \n at the end of message.
   *
   * Params: message: String, Message to send.
   **/
  def writeToSocket(message: String): String = { println(message + "\n"); return message + "\n" }

  /**
   * Description: Answer to ping messages.
   * 
   * Params: n: String, Number that ping reclaim.
   */
  def pong(n: String): Unit = send(writeToSocket, "PONG " + n)

  /**
   * Description: Join multiple channels at once. Get channel list from
   * botconfig.xml.
   **/
  def joinChannels(): Unit = channels.map(c => join(c))

  /**
   * Description: Join wanted channel.
   * 
   * Params: channel: String, Channel to join.
   */
  def join(channel: String): Unit = send(writeToSocket, "JOIN " + channel)

  /**
   * Description: Send message to channel
   *
   * Params:  channel: String, Channel where send message
   *          message: String, Message to send
   */
  def sendToChannel(channel: String, message: String): Unit =
    send(writeToSocket, "PRIVMSG " + channel + " " + message)

  /**
   * Description: Handles message. If message havent assing to bot, reject it. 
   * If message doesnt have any keywords just echo it back, else trigger 
   * function corresponding keyword.
   *
   * Params: message: String, Message that containt PRIVMSG
   **/
  def handleMessage(message: String): Unit = {
    val pattern = """:(.*)!(.*) PRIVMSG #(.*) :scalabot12345:(.*)""".r
    message match {
        case pattern(nick, host, ch, msg)  => sendToChannel("#"+ch.trim, ":" + nick.trim + ":" + msg)
        case _ =>
    }
  }

}
