# widok-scalajs-react-wrapper
Wrapper library for using scalajs-react in a Widok application

This allows to use
  * Your own FB React componnents written in [scalajs-react](https://github.com/japgolly/scalajs-react)
  * Components written in scalas-react, e.g. [scalajs-react-components](https://github.com/chandu0101/scalajs-react-components)
  * FB React components [wrapped as scalajs-react components](https://github.com/chandu0101/scalajs-react-components/blob/master/doc/InteropWithThirdParty.md) (please share the wrapping)
  
#### How to use it

(given https://github.com/MetaStack-pl/MetaRx/issues/28 solved):

Add this library to project dependencies:

```sbt
libraryDependencies ++= Seq(
...
  "io.github.widok" %%% "widok-scalajs-react-wrapper" % "0.1.0-SNAPSHOT" withSources() withJavadoc()
)
```

Then in the Widok based code:

```scala
import org.widok.bindings.scalajsReact.ReactComponentWrapper.ReactDynamic

import chandu0101.scalajs.react.components.fascades.{LatLng, Marker}
import chandu0101.scalajs.react.components.searchboxes.ReactSearchBox.DefaultStyle
import chandu0101.scalajs.react.components.maps.GoogleMap
import chandu0101.scalajs.react.components.searchboxes.ReactSearchBox

object App extends PageApplication {
  val lat = Var("63.0")
  val lon = Var("10.0")
  val latlng = lat.flatMap(lt => lon.map ( ln => LatLng(lt.toDouble, ln.toDouble) ))

  def googleMapProps(width : String = "500px" , height : String = "500px", center: LatLng, zoom: Int = 4, markers: List[Marker] = Nil,url : String = "https://maps.googleapis.com/maps/api/js") = GoogleMap.Props(width,height,center, zoom, markers,url)
  val props = latlng.map( curPos => googleMapProps(center = curPos))
  
  val searchText = Channel[String]
  val searchProps = Var(
    ReactSearchBox.Props(onTextChange = searchText, style = DefaultStyle)
  )

  def view() = div(
   ReactDynamic(GoogleMap.component, props),
   ReactDynamic(ReactSearchBox.component, searchProps),
      "Search query: ", searchText,
 )
}
```
