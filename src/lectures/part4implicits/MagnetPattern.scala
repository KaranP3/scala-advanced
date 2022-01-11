package lectures.part4implicits

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MagnetPattern extends App{

  // method overloading

  class P2PRequest
  class P2PResponse
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    def receive[T : Serializer](message: T): Int
    def receive[T : Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
    // lots of overloads
  }

  /*
  problems:
  1 - type erasure
  2 - lifting doesn't work for all overloads
    val receiveFV = receive _ // ?!
  3 - code duplication
  4 - type inference and default args
    actor.receive(?!)
   */

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling P2P request
      println("handling P2P request")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    def apply(): Int = {
      // logic for handling P2P response
      println("handling P2P response")
      42
    }
  }

  receive(new P2PResponse)
  receive(new P2PRequest)

  // benefits
  // 1 - no more type erasure problems
  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    def apply(): Int = 2
  }

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    def apply(): Int = 3
  }

  println(receive(Future(new P2PResponse)))
  println(receive(Future(new P2PRequest)))

  // 2 - lifting works
  trait MathLib {
    def add1(x: Int): Int = x + 1
    def add1(x: String): Int = x.toInt + 1
    // a bunch of add1 overloads
  }

  // "magnetize"
  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(value: Int) extends AddMagnet {
    def apply(): Int = value + 1
  }

  implicit class AddString(value: String) extends AddMagnet {
    def apply(): Int = value.toInt + 1
  }

  val addFV = add1 _
  println(addFV(1))
  println(addFV("3"))

  /*
  Drawbacks
  1 - super verbose
  2 - harder to read
  3 - you can't name or place default arguments
  4 - callByName doesn't work correctly (exercise: prove it!) (hint: side effects)
   */

  class Handler {
    def handle(value: => String): Unit = {
      println(value)
      println(value)
    }
    // other overloads
  }

  trait HandleMagnet {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet): Unit = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("hello scala")
    "haha"
  }

  handle(sideEffectMethod())
  handle{
    println("hello scala")
    "haha"
  }
}
