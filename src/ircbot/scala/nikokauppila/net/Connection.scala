package ircbot.scala.nikokauppila.net

import java.net.{Socket, ServerSocket}
import java.util.concurrent.{Executors, ExecutorService}
import java.io.{InputStreamReader, BufferedReader, BufferedOutputStream}

/**
 * Description: Simple abstract class to define interface for Connection classes.
 * Params:  host:   String  , Hostname to connect
 *          port:   Int     , Port to connect
 *          debug:  Boolean , Turns debug mode on and off
 **/
abstract class Connection(val host: String, val port: Int, val debug: Boolean) {

  protected val connection: Socket = new Socket(host, port)
  protected val inputStream: BufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream)) 
  protected val outputStream: BufferedOutputStream = new BufferedOutputStream(connection.getOutputStream)

  /**
   * Description: Starts handling socket stream.
   **/
  def start(f: () => Unit) = { f() }

  /**
   * Description: Read line from socket input stream
   * Params: f: function (String => Option[String]) , defines what we do to line from stream.
   **/
  def read(f: (String) => Option[String]): Option[String] = { return f(inputStream.readLine) }

  /**
   * Description: Handles message sending.
   *
   * Params: f:         function (String => String) , function that used to format message.
   *         message:   String                      , Message to send
   **/
  def send(f: (String) => String, message: String) = {
    outputStream.write((f(message)).getBytes)
    outputStream.flush()
  }

}
