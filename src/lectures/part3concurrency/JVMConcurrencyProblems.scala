package lectures.part3concurrency

object JVMConcurrencyProblems {

  def runInParallel(): Unit = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    println(x) // race condition
  }

  case class BankAccount(var amount: Int)

  def buy(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    /*
    involves three steps
    1. read old value
    2.compute result
    3. write new value
     */
    bankAccount.amount -= price
  }

  def buySafe(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    bankAccount
      .synchronized { // does not allow multiple threads to run the critical section
        bankAccount.amount -= price // critical section
      }
  }

  def demoBankingProblem(): Unit = {
    (1 to 10000).foreach(_ => {
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buy(account, "shoes", 3000))
      val thread2 = new Thread(() => buy(account, "iphone", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if (account.amount != 43000) {
        println(s"Something went wrong: ${account.amount}")
      }
    })
  }

  /*
  Exercise
  1. create "interception threads"
    thread1
      -> thread2
        -> thread3
          ....
    each thread prints "hello from thread $i"
    print all of them in reverse order

  2. what is the min/max value of x
  3. "sleep fallacy" - what's the value of message?
   */

  // inception threads
  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = {
    new Thread(() => {
      if (i < maxThreads) {
        val newThread = inceptionThreads(maxThreads, i + 1)
        newThread.start()
        newThread.join()
      }
      println(s"Hello from thread $i")
    })
  }

  /*
  max value = 100 - each thread increases x by 1
  min value = 1 -
    all threads read x = 0 at the same time
    all threads (in parallel) compute 0 + 1 = 1
    all threads try to write x = 1
   */
  def minMaxX(): Unit = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
  }

  /*
  almost always message = Scala is awesome
  is it guaranteed? NO
  obnoxious situation:

  main thread:
    message = "Scala sucks"
    awesomeThread.start()
    sleep(1001) - yields execution

  awesome thread starts:
    sleep(1001) - yields execution
  OS gives CPU to some important thread, takes > 2s
  OS gives the CPU back to the main thread
  main thread:
    println(message) // "Scala sucks"
  awesomeThread
    message = "Scala is awesome"
   */
  def demoSleepFallacy(): Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })

    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)
    // solution
    awesomeThread.join()
    println(message)
  }

  def main(args: Array[String]): Unit = {
    inceptionThreads(50).start()
  }
}
