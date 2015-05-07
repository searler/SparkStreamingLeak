
import org.apache.spark.streaming._
import org.apache.spark._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.StreamingContext._
import java.util.Arrays
import scala.util.Try
import org.apache.spark.streaming.dstream.DStream

/**
 * Trivial test program that simply prints the stream
 * 
 */
object Trivial extends App {

  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("Example")
    .set("spark.executor.memory", "1g")
    .set("spark.cleaner.ttl", "300")

  val streamingContext = new StreamingContext(conf, Seconds(2))

  val dStream = streamingContext.socketTextStream("localhost",
    12345, StorageLevel.MEMORY_ONLY_SER)

  dStream.print

  streamingContext.start()
  streamingContext.awaitTermination()

}
