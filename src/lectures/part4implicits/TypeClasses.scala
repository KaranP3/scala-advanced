package lectures.part4implicits
import java.util.Date

object TypeClasses extends App {

  trait HtmlWriteable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HtmlWriteable {
    override def toHtml: String =
      s"<div>$name ($age yo) <a href='$email'/></div>"
  }

  User("Karan", 28, "karan@email.com").toHtml
  /*
  disadvantages
  1 - only for the types WE write
  2 - ONE implementation out of quite a number
   */

  // option 2 - pattern matching
  //  object HtmlSerializer {
  //    def serializeToHtml(value: Any): Any = value match {
  //      case User(n, a, e) =>  s"<div>$n ($a yo) <a href='$e'/></div>"
  //      case java.util.Date =>
  //      case _ =>
  //    }
  //  }

  /*
  disadvantages
  1 - lost type safety
  2 - need to modify the code every time to add something
  3 - still ONE implementation for each given type
   */

  // option 3
  trait HtmlSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HtmlSerializer[User] {
    override def serialize(user: User): String =
      s"<div>${user.name} (${user.age} yo) <a href='${user.email}'/></div>"
  }

  println(UserSerializer.serialize(User("Karan", 28, "karan@email.com")))

  // 1 - we can define serializers for other types
  // java.util.Date
  object DateSerializer extends HtmlSerializer[java.util.Date] {
    override def serialize(date: Date): String = s"<div>${date.toString}</div>"
  }

  // 2 - we can define multiple serializers
  object PartialUserSerializer extends HtmlSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  // TYPE CLASS
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]): MyTypeClassTemplate[T] = instance
  }

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

  // part 2
  object HtmlSerializer {
    def serialize[T](value: T)(implicit serializer: HtmlSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HtmlSerializer[T]): HtmlSerializer[T] = serializer
  }

  implicit object IntSerializer extends HtmlSerializer[Int] {
    override def serialize(value: Int): String = s"<div>$value</div>"
  }

  val karan = User("karan", 28, "karan@email.com")

  println(HtmlSerializer.serialize(2))
  println(HtmlSerializer.serialize(karan))

  // access to the entire type class interface
  println(HtmlSerializer[User].serialize(karan))

  /*
  Exercise: implement the type class pattern for the equality type class
   */
  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(a, b)
  }

  val anotherKaran = User("karan", 28, "karan@email.com")
  println(Equal(karan, anotherKaran))
  // AD-HOC polymorphism
}
