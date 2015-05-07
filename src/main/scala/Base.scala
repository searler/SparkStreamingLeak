
import org.apache.spark.streaming._
import org.apache.spark._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.StreamingContext._
import java.util.Arrays
import scala.util.Try
import org.apache.spark.streaming.dstream.DStream

/**
 * Common implementation across all test scenarios
 */
object Base {

  object DataParser {
    def parse(str: String): Tuple2[Int, Data] = {
      val parts: Array[String] = str.split(" ")
      parts match {
        case Array(idS, indexS) => {
          Try {
            val id = idS.toInt
            (id, Data(id, indexS.toInt))
          }.getOrElse(0, Data(0, 0))
        }
        case _ => println("unexpected ", parts.toList); (0, Data(0, 0))
      }
    }
  }

  /**
   * Run the Spark streaming with specified scenario specific functionality
   */
  def apply[T](run: DStream[(Int, Data)] => DStream[(Int, T)], checkpointed: Boolean = true) = {

    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("Example")
      .set("spark.executor.memory", "1g")
      .set("spark.cleaner.ttl", "300")
      .set("spark.streaming.receiver.maxRate","100000")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    conf.registerKryoClasses(Array(classOf[Data], classOf[Summation]))

    val streamingContext = new StreamingContext(conf, Seconds(2))

    if (checkpointed)
      streamingContext.checkpoint("/tmp/spark")

    val dStream = streamingContext.socketTextStream("localhost",
      12345, StorageLevel.MEMORY_AND_DISK_SER)

    val data = dStream.map { DataParser.parse _ }

    run(data).print

    streamingContext.start()
    streamingContext.awaitTermination()
  }

}

case class Data(id: Int, index: Int)

case class Summation(id: Int, sum: Long)
