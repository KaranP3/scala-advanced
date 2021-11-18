package playground

object ScalaPlayground extends App {
  abstract class Student {
    def name: String
    def subject: String
  }

  case class UndergradStudent(override val name: String, override val subject: String)
    extends Student

  case class MastersStudent(override val name: String, override val subject: String)
  extends Student

  val printStudentDetails = (name: String, subject: String) => (name, subject)

  val karan = MastersStudent("Karan", "Computer Science")
  val james = UndergradStudent("James", "International Relations")

  val studentSeq: Seq[Student] = Seq(karan, james)

  val printer = for (student <- studentSeq) yield student match {
    case _: MastersStudent => (student.name, student.subject, "Masters")
    case _: UndergradStudent => (student.name, student.subject, "Undergraduate")
  }

  println(printer)
}
