package exercises

import lectures.part4implicits.TypeClasses.User

object EqualityPlayground extends App{
  /*
Equality
 */
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean =
      (a.name == b.name) && (a.email == b.email)
  }

  /*
Exercise: implement the type class pattern for the equality type class
 */
  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(a, b)
  }
  val karan = User("karan", 28, "karan@email.com")
  val anotherKaran = User("karan", 28, "karan@email.com")
  println(Equal(karan, anotherKaran))
  // AD-HOC polymorphism

  /*
  - Exercise: improve the Equal TC with an implicit conversion class
  - ===(anotherValue: T)
  - !==(anotherValue: T)
   */
  implicit class TypeSafeEqual[T](value: T) {
    def ===(anotherValue: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(value, anotherValue)
    def !==(anotherValue: T)(implicit equalizer: Equal[T]): Boolean =
      !equalizer.apply(value, anotherValue)
  }

  println(karan === anotherKaran)
  /*
    karan.===(anotherKaran)
    new TypeSafeEqual[User](karan).===(anotherKaran)
    new TypeSafeEqual[User](karan).===(anotherKaran)(NameEquality)
   */
  /*
    TYPE SAFE
  */
  println(karan !== anotherKaran)
}
