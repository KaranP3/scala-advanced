package lectures.part5ts

object SelfTypes extends App {

  // requiring a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer { self: Instrumentalist => // SELF TYPE whoever implements singer also needs to implement instrumentalist
    // rest of the implementation or API
    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = println("playing")
    override def sing(): Unit = println("singing")
  }

  //  class Vocalist extends Singer {
  //
  //  } // this is illegal

  val jamesHetfield = new Singer with Instrumentalist {
    override def play(): Unit = println("playing")
    override def sing(): Unit = println("singing")
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("playing guitar")
  }

  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = println("eric clapton is singing")
  }

  // vs. Inheritance
  class A
  class B extends A // B is an A

  trait T
  trait S {self: T => } // S requires a T

  // CAKE PATTERN => "dependency injection"

  // DI
  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  // CAKE PATTERN
  trait ScalaComponent {
    def action(x: Int): String
  }
  //  trait ScalaComponentA extends ScalaComponent
  //  trait ScalaComponentB extends ScalaComponent
  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + "this is crazy!"
  }
  trait ScalaApplication{ self: ScalaDependentComponent => }

  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose components
  trait UserProfile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  // layer 3 - app
  trait AnalyticsApp extends ScalaApplication with Analytics

  // cyclical dependencies
  //  class X extends Y
  //  class Y extends X

  trait X { self: Y => }
  trait Y { self: X => } // this is possible
}
