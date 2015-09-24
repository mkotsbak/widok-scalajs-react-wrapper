# widok-scalajs-react-wrapper
Wrapper library for using scalajs-react in a Widok application

This allows to use
  * Your own FB React componnents written in [scalajs-react](https://github.com/japgolly/scalajs-react)
  * Components written in scalas-react, e.g. [scalajs-react-components](https://github.com/chandu0101/scalajs-react-components)
  * FB React components [wrapped as scalajs-react components](https://github.com/chandu0101/scalajs-react-components/blob/master/doc/InteropWithThirdParty.md) (please share the wrapping)
  
#### How to use it

Add this library to project dependencies (until it is published somewhere, you need to download this project and run "sbt publish-local" first):

```sbt
libraryDependencies += "io.github.widok" %%% "widok-scalajs-react-wrapper" % "0.1.0-SNAPSHOT" withSources() withJavadoc()

jsDependencies += "org.webjars" % "react" % "0.12.2" / "react-with-addons.js" commonJSName "React"

```
Then in the Widok based code:

```scala
import org.widok.bindings.scalajsReact.ReactComponentWrapper._

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
    ReactSearchBox.Props(onTextChange = searchText.produce, style = DefaultStyle)
  )

  def view() = div(
   p(
     span("Lat: "), number().bind(lat),
     span("Lon: "), number().bind(lon),
     span("Zoom: "), number().bind(zoom)
   ),
   p(
    ReactDynamic(GoogleMap.component, props),
    ReactDynamic(ReactSearchBox.component, searchProps),
   "Search query: ", searchText,
   )
 )
}
```
