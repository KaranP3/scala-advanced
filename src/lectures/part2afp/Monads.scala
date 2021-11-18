package lectures.part2afp

object Monads extends App{
  // essentially Try is a computation that might throw exceptions
  // but it will not blow up in your face, it will contain the exception
  // within if it does happen

  // our own Try monad
  trait Attempt[+A] {
    // takes another type parameter and a function which takes something
    // of type A and transforms it into another Attempt of type B
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    // creates a monad out of an expression which might throw an expression
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
  left-identity

  unit.flatMap(f) = f(x)
  Attempt(x).flatMap(f) = f(x) // Success case!
  Success(x).flatMap(f) = f(x) // proved

  right-identity

  Attempt.flatMap(unit) = attempt
  Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
  Fail(_).flatMap(...) = Fail(e)

  associativity

  attempt.flatMap(f).flatMap(g) = attempt.flatMap(x => f(x).flatMap(g))
  Fail(e).flatMap(f).flatMap(g) = Fail(e)
  Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)

  Success(v).flatMap(f).flatMap(g) =
    f(v).flatMap(g) OR Fail(e)

  Success(v).flatMap(x => f(x).flatMap(g)) =
    f(v).flatMap(g) OR Fail(e)
   */

  val attempt = Attempt {
    throw new RuntimeException("My own monad, yes")
  }
  println(attempt)

  /*
  Exercise:
  1) implement a Lazy[T] monad = computation which will only be executed
     when it's needed
     unit/apply
     flatMap

  2) Monads = unit + flatMap
     Monads = unit + map + flatten
   */

  // 1 - lazy monad
  class Lazy[+A](value: => A) {
    // call by need
    private lazy val internalValue = value
    def use: A = internalValue
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything")
    42
  }

//  println(lazyInstance.use)

  val flatMappedInstance = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  val flatMappedInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  flatMappedInstance.use
  flatMappedInstance2.use

  /*
  left identity
  unit.flatMap(f) = f(v)
  Lazy(v).flatMap(f) = f(v)

  right identity
  1.flatMap(unit) = 1
  Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)

  associativity: 1.flatMap(f).flatMap(g) = 1.flatMap(x => f(x).flatMap(g))
  Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
  Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
   */

  // 2. map and flatten in terms of flatMap
  /*
  Monad[T] { // List
    def flatMap[B](f: (=> A) => Monad[B]): Monad[B] = ...(implemented)

    def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x)))
    def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap(x: Monad[T] => x)
  }

  List(1, 2, 3).map(_ * 2) = List(1, 2, 3).flatMap(x => List(x * 2))
  List(List(1, 2), List(3, 4)).flatten = List(List(1, 2), List(3, 4)).flatMap(x => x) = List(1, 2, 3, 4)
   */

}
