import org.apache.spark.streaming.dstream.DStream

/**
 * No operation
 */
object Noop extends App {

  def run(data: DStream[(Int, Data)]) = data

  // class "leak" even if checkpointed is true
  Base(run,checkpointed=false)
}