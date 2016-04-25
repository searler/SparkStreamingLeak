import java.net.ServerSocket
import java.util.concurrent.Executors
import java.util.concurrent.CountDownLatch
import java.net.Socket
import scala.util.Try

/**
 * Socket server on port 12345
 * that generates stream of Strings containing two numbers.
 */
object Generator extends App {

  val executors = Executors.newCachedThreadPool()

  val latch = new CountDownLatch(1);

  val server = new ServerSocket(12345)

  while (true)
    executors.execute(new Client(server.accept()))

  latch.await()

  class Client(val sock: Socket) extends Runnable {

    override def run = {
      val os = new java.io.BufferedOutputStream(sock.getOutputStream)

      var index = 0

      Try {
        while (true) {
          index += 1

          var id = 0

          while (id < 1000) {
            id += 1

            os.write(s"$id $index\n".getBytes)
          }
        }
      }
      os.close
    }
  }
}
