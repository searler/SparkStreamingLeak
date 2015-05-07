import org.apache.spark.streaming.dstream.DStream

/**
 * Accummulate Data instances into Data
 */
object DataOnly extends App {

  def run(data: DStream[(Int, Data)]) = {

    def updateFunction(newValues: Seq[Data], current: Option[Data]): Option[Data] =
      newValues.headOption

    data.updateStateByKey(updateFunction _)
  }

  Base(run)
}