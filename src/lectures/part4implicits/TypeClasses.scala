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

  // part 2
  object HtmlSerializer {
    def serialize[T](value: T)(implicit serializer: HtmlSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HtmlSerializer[T]): HtmlSerializer[T] = serializer
  }

  implicit object IntSerializer extends HtmlSerializer[Int] {
    override def serialize(value: Int): String = s"<div>$value</div>"
  }

  // part 3
  implicit class HtmlEnrichment[T](value: T) {
    def toHTML(implicit serializer: HtmlSerializer[T]): String = serializer.serialize(value)
  }

  val karan = User("karan", 28, "karan@email.com")
  println(karan.toHTML) // println(new HtmlEnrichment(karan).toHTML(userSerializer))
  /*
   - extend to new types
   - choose implementation
   - super expressive!
   */
  println(2.toHTML)
  println(karan.toHTML(PartialUserSerializer))

  /*
   - type class itself --- HTMLSerializer[T] { ... }
   - type class instances (some of which are implicit) --- UserSerializer, IntSerializer
   - conversion with implicit classes --- HtmlEnrichment
   */

  // context bounds
  def htmlBoilerplate[T](content: T)(implicit serializer: HtmlSerializer[T]) =
    s"<html><body>${content.toHTML(serializer)}</body></html>"

  def htmlSugar[T : HtmlSerializer](content: T): String = {
    var serializer = implicitly(HtmlSerializer[T])
    // use serializer if you want by using implicitly
    s"<html><body>${content.toHTML(serializer)}</body></html>"
  }

  // implicitly
  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other part of the code
  val standardPerms = implicitly[Permissions]
  println(standardPerms)
}
