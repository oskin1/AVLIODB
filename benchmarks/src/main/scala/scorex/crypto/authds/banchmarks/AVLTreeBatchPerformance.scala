package scorex.crypto.authds.banchmarks

import java.util.concurrent.{ThreadPoolExecutor, TimeUnit}

import io.iohk.iodb.LogStore
import org.openjdk.jmh.annotations._
import org.slf4j.LoggerFactory
import scorex.crypto.authds.avltree.batch.{Operation, PersistentBatchAVLProver, VersionedIODBAVLStorage}
import scorex.crypto.hash.{Blake2b256Unsafe, Digest32}

object AVLTreeBatchPerformance extends {

  type Prover = PersistentBatchAVLProver[Digest32, Blake2b256Unsafe]

  import Helper._

  @State(Scope.Thread)
  class Basic(proverCnt: Int, opsCnt: Int) {

    val logger = LoggerFactory.getLogger("TEST")
    var prover: Prover = _
    var store: LogStore = _
    var storage: VersionedIODBAVLStorage[Digest32] = _
    var operations: Array[Operation] = _

    @Setup(Level.Iteration)
    def up: Unit = {
      val (p, s, _) = getPersistentProverWithLSMStore(1000, proverCnt)
      store = s
      prover = p
      operations = generateOps(proverCnt until (proverCnt + opsCnt))
    }

    @TearDown(Level.Iteration)
    def down: Unit = {
      store.executor.asInstanceOf[ThreadPoolExecutor].shutdownNow()
      prover = null
      operations = Array.empty
    }
  }

  class StateWith1000000 extends Basic(1000000, 100000)

  class StateWith2000000 extends Basic(2000000, 100000)

  class StateWith4000000 extends Basic(4000000, 100000)

  class StateWith8000000 extends Basic(8000000, 100000)

  class StateWith16000000 extends Basic(16000000, 100000)

  class StateWith32000000 extends Basic(32000000, 100000)

}


@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(1)
class AVLTreeBatchPerformance {

  import AVLTreeBatchPerformance._

  @Benchmark
  def apply100KinBatchesOf2KToProverWith1M(s: StateWith1000000): Unit = {
    import s._
    operations.grouped(2000).foreach { batch =>
      batch.foreach(prover.performOneOperation)
      prover.generateProofAndUpdateStorage()
    }
  }

  @Benchmark
  def apply100KinBatchesOf2KToProverWith2M(s: StateWith2000000): Unit = {
    import s._
    operations.grouped(2000).foreach { batch =>
      batch.foreach(prover.performOneOperation)
      prover.generateProofAndUpdateStorage()
    }
  }

  @Benchmark
  def apply100KinBatchesOf2KToProverWith4M(s: StateWith4000000): Unit = {
    import s._
    operations.grouped(2000).foreach { batch =>
      batch.foreach(prover.performOneOperation)
      prover.generateProofAndUpdateStorage()
    }
  }

  @Benchmark
  def apply100KinBatchesOf2KToProverWith8M(s: StateWith8000000): Unit = {
    import s._
    operations.grouped(2000).foreach { batch =>
      batch.foreach(prover.performOneOperation)
      prover.generateProofAndUpdateStorage()
    }
  }

  @Benchmark
  def apply100KinBatchesOf2KToProverWith16M(s: StateWith16000000): Unit = {
    import s._
    operations.grouped(2000).foreach { batch =>
      batch.foreach(prover.performOneOperation)
      prover.generateProofAndUpdateStorage()
    }
  }

  @Benchmark
  def apply100KinBatchesOf2KToProverWith32M(s: StateWith32000000): Unit = {
    import s._
    operations.grouped(2000).foreach { batch =>
      batch.foreach(prover.performOneOperation)
      prover.generateProofAndUpdateStorage()
    }
  }
}
