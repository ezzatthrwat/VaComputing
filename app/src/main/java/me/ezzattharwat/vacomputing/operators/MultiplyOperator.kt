
package me.ezzattharwat.vacomputing.operators

object MultiplyOperator : Calculate {
  override fun apply(leftOperand: Float, rightOperand: Float): Float = leftOperand.times(rightOperand)
}