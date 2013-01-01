package ircbot.connection

import scala.xml._
import scala.actors.Actor
import scala.actors.Actor._
import ircbot.MessageHandler
import ircbot.NetworkConfiguration

/**
 * Description: Class handles all connection related tasks. Including sending messages, joining channels and answering pings.
 * Params:  network:    NodeSeq , Network block from configuration for connection
 *          debug:      Boolean , Turns debug mode on and off
 **/
class IRCConnection(network: NetworkConfiguration, debug: Boolean)
        extends Connection(network.host, network.port, debug){

  /**
   * Description: Starts connection to irc server.
   *              Set USER and NICK.
   *              Invoke read method and print debug info if needed.
   **/
  override def run(): Unit = {
    if(connection.isConnected) {
      send(writeToSocket, "USER " + network.user + " dontcare dontcare :Scala-bot")
      send(writeToSocket, "NICK " + network.nick)
      while(true) {
        read(readLineFromSocket).foreach { line =>
            if(debug) println(line)
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
                                    Some(i)
                                }
      // If line contains ":End of /MOTD" message, it certain that bot can joint to channels.
      case i if i contains ":End of /MOTD" => { 
                                    joinChannels
                                    Some(i)
                                }
      // PRIVMSG indicated that some of channels have messages which bot must handle somehow. 
      // It can process message or reject it.
      case i if i contains "PRIVMSG" => { 
                                    handleMessage(i)
                                    Some(i)
                                }
      // This is just for debugging application.
      case i if i != null => Some(i)
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
  def joinChannels(): Unit = network.channels.foreach(c => join(c))

  /**
   * Description: Join wanted channel.
   * 
   * Params: channel: String, Channel to join.
   */
  def join(channel: String): Unit = send(writeToSocket, "JOIN " + channel)

  /**
   * Description: Send message to receiver
   *
   * Params:  receiver: String, receiver who get message. Channel or user.
   *          message:  String, Message to send
   */
  def sendTo(receiver: String, message: String): Unit = {
    val msg = ":" + network.nick + " PRIVMSG " + receiver + " :" +message
    send(writeToSocket, msg)
  }

  def handleMessage(message: String): Actor = actor {
    MessageHandler ! (message, network.nick)
    receive {
        case (Some(to: String), Some(message: String)) => sendTo(to, message)
    }
  }
}
