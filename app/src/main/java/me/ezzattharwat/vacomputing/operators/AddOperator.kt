
package me.ezzattharwat.vacomputing.operators


object AddOperator : Calculate {
  override fun apply(leftOperand: Float, rightOperand: Float): Float = leftOperand.plus(rightOperand)
}