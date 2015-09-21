package org.widok.bindings.scalajsReact

import japgolly.scalajs.react.React
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.ReactComponentC
import japgolly.scalajs.react.ReactElement
import japgolly.scalajs.react._
import org.scalajs.dom
import org.scalajs.dom.html._
import org.widok.{ReadChannel, DOM, Widget}

/**
 * Created by marius on 9/21/15.
 */

object ReactComponentWrapper {

  case class ReactStatic(component: Unit => ReactElement) extends Widget[ReactStatic] {
    override val rendered = DOM.createElement("span")

    override def render(parent: dom.Node, offset: dom.Node): Unit = {
      super.render(parent, offset)
      React.render(
        ReactComponentB.static("Static wrapper for Widok", component()).build(Unit),
        rendered)
    }
  }

  private def wrapperComponentDynamic[WP, WS, WB, WN <: TopNode](component: ReactComponentC.ReqProps[WP, WS, WB, WN]) =
    ReactComponentB[ReadChannel[WP]]("Dynamic wrapper for Widok")
      .initialStateP(_.cache.get)
      .noBackend
      .render(scope => {
        component(scope.state)
      })
      .componentWillMount ( scope =>
        scope.props.distinct.attach { currProps =>
          scope.setState(currProps)
        })
      .build

  case class ReactDynamic[P, S, B, N <: TopNode](component: ReactComponentC.ReqProps[P, S, B, N], props: ReadChannel[P]) extends Widget[ReactDynamic[P, S, B, N]] {
    override val rendered: Element = DOM.createElement("span")

    React.render(
      wrapperComponentDynamic(component)(props),
      rendered
    )
  }
}
