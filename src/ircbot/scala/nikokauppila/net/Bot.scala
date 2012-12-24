package ircbot.scala.nikokauppila.net

object Bot {

    def main(args: Array[String]) {
        val conn: IRCConnection = new IRCConnection("irc.quakenet.org", 6667, true)
        conn.start(conn.readSocket)
    }

}
