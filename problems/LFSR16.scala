package TutorialProblems

import Chisel._

class LFSR16 extends Module {
  val io = new Bundle {
    val inc = Bool(INPUT)
    val out = UInt(OUTPUT, 16)
  }
  // COMPUTE LFSR16 HERE
  val lfsr = Reg(init = UInt(1, 16))
  when (io.inc) {
    val shift_in = (lfsr(0) ^ lfsr(2) ^ lfsr(3) ^ lfsr(5))
    lfsr := Cat(shift_in, lfsr(15, 1))
  }

  io.out := lfsr
}

class LFSR16Tests(c: LFSR16) extends Tester(c) {
  var res = 1
  for (t <- 0 until 16) {
    val inc = rnd.nextInt(2)
    poke(c.io.inc, inc)
    step(1)
    if (inc == 1) {
      val bit = ((res >> 0) ^ (res >> 2) ^ (res >> 3) ^ (res >> 5) ) & 1;
      res = (res >> 1) | (bit << 15);
    }
    expect(c.io.out, res)
  }
}
