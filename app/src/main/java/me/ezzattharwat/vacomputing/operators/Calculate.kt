
package me.ezzattharwat.vacomputing.operators


interface Calculate {
  /**
   * @return the result of calculation
   * @throws [RuntimeException] like when divided by zero
   */
  @Throws(RuntimeException::class)
  fun apply(leftOperand: Float, rightOperand: Float): Float


}
