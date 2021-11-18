package lectures.part2afp

object LazyEvaluation extends App{

  // lazy delays the evaluation of values
  lazy val x: Int = {
    println("hello")
    42
  }
  println(x)
  println(x) // x keeps the same value i.e. 42

  // examples of implications
  // side effects
  def sideEffectCondition: Boolean = {
    println("boo")
    true
  }
  def simpleCondition: Boolean = false
  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no")

  // in conjunction with call by name
  def byName(n: => Int): Int = {
    // CALL BY NEED
    lazy val t = n
    t + t + t + 1
  }
  def retrieveMagicValue: Int = {
    println("waiting")
    Thread.sleep(1000)
    42
  }
  println(byName(retrieveMagicValue))
  // use lazy vals

  // filtering with lazy vals
  def lessThanThirty(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThanTwenty(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThanThirty) // List(1, 25, 5, 23)
  val gt20 = lt30.filter(greaterThanTwenty)
  println(gt20)

  val lt30lazy = numbers.withFilter(lessThanThirty) // lazy vals under the hood
  val gt20lazy = lt30lazy.withFilter(greaterThanTwenty)
  println
  gt20lazy.foreach(println)

  // for comprehensions use withFilter with guards
  for {
    a <- List(1, 2, 3) if a % 2 == 0 // use lazy vals!
  } yield a + 1
  List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int]

  /*
  Exercise: Implement a lazily evaluated singly linked STREAM of elements

  naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!)
  naturals.take(100) // lazily evaluated stream of first 100 naturals (finite stream)
  naturals.forEach(println) // will crash - infinite!
  naturals.map(_ * 2) // stream of all even numbers (potentially infinite)


  note: Streams are a special kind of collection, the head of the stream is always
  evaluated and always available. But the tail is always lazily evaluated and
  available only on demand
   */
  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] // prepend
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams

    def forEach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes the first n elements out of the stream - a finite stream
    def takeAsList(n: Int): List[A]
  }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }

}
