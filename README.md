# prog3.proyecto
Proyecto para el curso de Programación III de la Universidad de Deusto realizado por [Rafael Romón](https://github.com/rafaelromon), [Saul Segura](https://github.com/luasaul) y [Mikel Solabarrieta](https://github.com/mikelsr). 

## Descripción:
Self-Hosted Server que almacena documentos y ficheros multimedia se comunica con un cliente en java que realiza operaciones sobre 3 bases de datos.

* Neo4j - Información sobre series y películas
* MongoDB - Documentos.
* DWH - Analitica (Likes, uso, etc).

<p align="center">
  <img src="https://github.com/Ninia/prog3.proyecto/blob/master/web/planteamiento.png" alt="Planteamiento"/>
</p>

## Monitoring the Server
Some stats and conditions of the host server can be stored in [InfluxDB](
https://github.com/influxdata/influxdb),
so it's possible either to monitor them in real time or query them in a
historical database. The real time monitoring can be done using [Grafana](
http://grafana.org/).
### InfluxDB
>InfluxDB is an open-source time series database developed by InfluxData.
It is written in Go and optimized for fast, high-availability storage and
retrieval of time series data in fields such as operations monitoring,
application metrics, Internet of Things sensor data, and real-time analytics. 

Official documentation can be accessed [here](
https://docs.influxdata.com/influxdb/v1.2/).
### Grafana
>Grafana is most commonly used for visualizing time series data for Internet
infrastructure and application analytics but many use it in other domains
including industrial sensors, home automation, weather, and process control.

Official documentation can be accessed [here](
http://docs.grafana.org/).
