package lectures.part1as

import scala.util.Try

object DarkSugars extends App{
  // syntax sugar #1 : methods with single params
  def singleArgMethod(x: Int): String = s"$x little ducks"

  //  val description = singleArgMethod {
  //    // write some complex code
  //    10
  //  }
  //
  //  val aTryInstance = Try { // java's try {...}
  //    throw new RuntimeException
  //  }
  //
  //  List(1, 2, 3).map{ x =>
  //    x + 1
  //  }
  //
  //  // syntax sugar #2: instances of traits with a single method can be reduced
  // to lambdas
  // name - single abstract method pattenr
  trait Action {
    def act(x: Int): Int
  }

  val anInstance = new Action {
    override def act(x: Int): Int = x + 1
  }

  val aFuncInstance: Action = (x: Int) => x + 1 // magic

  // example - Runnables
  val aThread: Thread = new Thread {
    override def run(): Unit = println("hello scala")
  }

  val aSweeterThread = new Thread(() => println("sweet scala"))

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }

  val anAbstractInstance: AnAbstractType = (a: Int) => println(a)

  // syntax sugar #3 - :: and #:: methods are special
  val prependedList = 2 :: List(3, 4)
  //2.::List(3, 4)
  // List(3, 4).::2

  // scala spec - last character decides the associativity of the method
  1 :: 2 :: 3 :: List(4, 5)
  List(4, 5).::(3).::(2).::(1)

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this // actual implementation here
  }
  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  // syntax sugar #4 - multi-word method naming
  class SomeGirl(name: String) {
    def `and then said` (gossip: String): Unit = println(s"$name said $gossip")
  }

  val lily = new SomeGirl("lily")
  println(lily `and then said` "scala is so sweet")

  // syntax sugar #5 - infix types
  class Composite[A, B]

  val composite: Int Composite String = ???

  class -->[A, B]
  val towards: Int --> String = ???

  // syntax sugar #6 - update method (also very special, like apply)
  val anArray = Array(1, 2, 3)
  anArray(2) = 7 // rewritten to an Array.update(2, 7)
  // used in mutable collections
  // remember apply and update

  // syntax sugar #7: setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0 // private for OO encapsulation
    def member: Int = internalMember // "getter
    def member_=(value: Int): Unit = internalMember = value // setter
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // rewritten as aMutableContainer.member_=(42)


}
