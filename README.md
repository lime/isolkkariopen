# isolkkariopen

**IsOlkkariOpen** is a single purpose app serving data on whether Athene's guild room *Olkkari* at Aalto University's CS building is open at the moment. It infers this by polling the room's webcam feed.

The app is a single-language endeavour, with the backend, templates, scripts and stylesheets all written in Clojure + ClojureScript.

Online at http://www.isolkkariopen.com

## Running

1. Install MongoDB & Leiningen.
2. Start MongoDB with `mongod &`
3. Set environment variables (eg. `source env-local.sh` in OS X)
4. Run isolkkariopen with `lein run -m isolkkariopen.web`
5. Realtime stylesheet compilation can be optionally initiated with `lein garden auto`

## License

Built by Joonas Rouhiainen, distributed under the Eclipse Public License.
