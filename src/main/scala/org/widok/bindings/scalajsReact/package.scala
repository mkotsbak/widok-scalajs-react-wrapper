package org.widok.bindings

import japgolly.scalajs.react.ReactComponentU_
import org.scalajs.dom
import org.scalajs.dom.html._
import org.widok.bindings.scalajsReact.ReactComponentWrapper.ReactWrapper
import org.widok.{View, DOM, Widget}
import pl.metastack.metarx.ReadChannel

/**
 * Created by marius on 9/23/15.
 */
package object scalajsReact {

  implicit class ReactWidget[T <: ReactComponentU_](wrapper: ReactWrapper[T]) extends Widget[ReactWidget[T]] {
    override val rendered: Element = DOM.createElement("span")

    override def render(parent: dom.Node, offset: dom.Node): Unit = {
      wrapper.render(rendered)
      super.render(parent, offset)
    }
  }

  implicit class ReactPlaceholderWidget[U <: ReactComponentU_, T <: ReactWrapper[U]](value: ReadChannel[T]) extends View {
    val node = DOM.createElement("span")

    def render(parent: dom.Node, offset: dom.Node) {
      DOM.insertAfter(parent, offset, node)

      value.attach { cur: ReactWrapper[U] => cur.render(node) }
    }
  }
}
