package lectures.part3concurrency

import scala.concurrent.Future
import scala.util.{Failure, Success}

// important for futures
import scala.concurrent.ExecutionContext.Implicits.global

object FuturesPromises extends App{

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture: Future[Int] = Future {
    calculateMeaningOfLife // calculates meaning of life on another thread
  } // (global) which is passed by the compiler

  println(aFuture.value) // Option[Try[Int]]

  println("waiting on the future")
  aFuture.onComplete {
    case Success(value) => println(s"the meaning of life is $value")
    case Failure(exception) => println(s"I have failed with $exception")
  } // SOME thread

  Thread.sleep(3000)
}
