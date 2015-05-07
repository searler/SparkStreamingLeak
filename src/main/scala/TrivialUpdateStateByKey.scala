
import org.apache.spark.streaming._
import org.apache.spark._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.StreamingContext._
import java.util.Arrays
import scala.util.Try
import org.apache.spark.streaming.dstream.DStream

/**
 * Trivial test program that performs updateStateByKey using only
 * standard Scala classes.
 */
object TrivialUpdateStateByKey extends App {

  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("Example")
    .set("spark.executor.memory", "1g")
    .set("spark.cleaner.ttl", "300")
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

  val streamingContext = new StreamingContext(conf, Seconds(2))

  streamingContext.checkpoint("/tmp/spark")

  val dStream = streamingContext.socketTextStream("localhost",
    12345, StorageLevel.MEMORY_AND_DISK_SER)

  val string = dStream.map { s =>
    val p = s.split(" ")
    val id = p(0).toInt
    (id, (id, p(2)))
  }

  def updateFunction(newValues: Seq[(Int, String)], current: Option[(Int, String)]): Option[(Int, String)] =
    newValues.headOption

  string.updateStateByKey(updateFunction _)

  dStream.print

  streamingContext.start()
  streamingContext.awaitTermination()

}
