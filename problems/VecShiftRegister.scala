package TutorialProblems

import Chisel._

class VecShiftRegister extends Module {
  val io = new Bundle {
    val ins   = Vec(4, UInt(INPUT, 4))
    val load  = Bool(INPUT)
    val shift = Bool(INPUT)
    val out   = UInt(OUTPUT, 4)
  }
  // FILL IN LOADABLE SHIFT REGISTER USING VEC 
  val shift_reg = Vec.fill(4){ Reg(init = UInt(0, 4)) }
  when (io.load) {
    shift_reg(0) := io.ins(0)
    shift_reg(1) := io.ins(1)
    shift_reg(2) := io.ins(2)
    shift_reg(3) := io.ins(3)
  } .elsewhen (io.shift) {
    shift_reg(0) := io.ins(0)
    shift_reg(1) := shift_reg(0)
    shift_reg(2) := shift_reg(1)
    shift_reg(3) := shift_reg(2)
  }

  io.out := shift_reg(3)
}

class VecShiftRegisterTests(c: VecShiftRegister) extends Tester(c) { 
  val reg     = Array.fill(4){ 0 }
  val ins     = Array.fill(4){ 0 }
  // Initialize the delays.
  for (i <- 0 until 4)
    poke(c.io.ins(i), 0)
  poke(c.io.load, 1)
  step(1)

  for (t <- 0 until 16) {
    for (i <- 0 until 4)
      ins(i) = rnd.nextInt(16)
    val shift = rnd.nextInt(2)
    val load  = rnd.nextInt(2)
    for (i <- 0 until 4)
      poke(c.io.ins(i), ins(i))
    poke(c.io.load,  load)
    poke(c.io.shift, shift)
    step(1)
    if (load == 1) {
      for (i <- 0 until 4) 
        reg(i) = ins(i)
    } else if (shift == 1) {
      for (i <- 3 to 1 by -1)
        reg(i) = reg(i-1)
      reg(0) = ins(0)
    }
    expect(c.io.out, reg(3))
  }
}
