package org.widok.bindings.scalajsReact

import japgolly.scalajs.react.{React, ReactComponentB, ReactComponentC, ReactElement, _}
import org.scalajs.dom
import org.scalajs.dom.html.Element
import org.widok.{View, DOM, Widget}
import pl.metastack.metarx.ReadChannel

/**
 * Created by marius on 9/21/15.
 */

object ReactComponentWrapper {

  trait ReactWrapper[T <: ReactComponentU_] {
    val reactComponent: T

    def render(target: dom.Node) = React.render(reactComponent, target)
  }

  implicit class ReactWidget[T <: ReactComponentU_](wrapper: ReactWrapper[T]) extends Widget[ReactWidget[T]] {
    override val rendered: Element = DOM.createElement("span")

    override def render(parent: dom.Node, offset: dom.Node): Unit = {
      wrapper.render(rendered)
      super.render(parent, offset)
    }
  }

  case class ReactStatic(component: ReactElement) extends ReactWrapper[ReactComponentU_] {
    override val reactComponent = ReactComponentB.static("Static wrapper for Widok", component).build(Unit)
  }

  private def wrapperComponentDynamic[WP, WS, WB, WN <: TopNode](component: ReactComponentC.ReqProps[WP, WS, WB, WN]) =
    ReactComponentB[ReadChannel[WP]]("Dynamic wrapper for Widok")
      .initialStateP(_.cache.get.get)
      .noBackend
      .render(scope => {
        component(scope.state)
      })
      .componentWillMount(scope =>
        scope.props.distinct.attach { currProps =>
          scope.setState(currProps)
        })
      .build

  case class ReactDynamic[P, S, B, N <: TopNode](component: ReactComponentC.ReqProps[P, S, B, N], props: ReadChannel[P]) extends ReactWrapper[ReactComponentU_] {
    override val reactComponent = wrapperComponentDynamic(component)(props)
  }

  implicit class ReactPlaceholderWidget[U <: ReactComponentU_, T <: ReactWrapper[U]](value: ReadChannel[T]) extends View {
    val node = DOM.createElement("span")

    def render(parent: dom.Node, offset: dom.Node) {
      DOM.insertAfter(parent, offset, node)

      value.attach { cur: ReactWrapper[U] => cur.render(node) }
    }
  }
}
