package lectures.part5ts

object PathDependentTypes extends App{

  class Outer {
    class Inner {

    }
    object InnerObject
    type InnerType

    def print(i: Inner): Unit = println(i)
    def printGeneral(i: Outer#Inner): Unit = println(i)
    def aMethod: Int = {
      class HelperClass
      // type HelperType // only accepts type aliases here
      2
    }
  }

  // per-instance
  val outer = new Outer
  val inner = new outer.Inner

  val otherOuter = new Outer
  //  val otherInner: otherOuter.Inner = new outer.Inner // this will not work

  // path dependent types

  // Outer#Inner
  outer.printGeneral(inner)
  otherOuter.printGeneral(inner)

  /*
    Exercise
      DB keyed by Int or String, but maybe others
   */
  /*
    Use path dependent types
    abstract type members and/or type aliases
   */
  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    type Key = K
  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  //  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  //  get[IntItem](42) // ok
  //  get[StringItem]("key") // ok

  //  get[IntItem]("2") // not okay
}
