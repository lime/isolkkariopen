(defproject isolkkariopen "1.0.0-SNAPSHOT"
  :description "API for Athene guild room"
  :url "http://isolkkariopen.herokuapp.com"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [org.clojure/data.json "0.2.4"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/clojurescript "0.0-2173"]
                 [compojure "1.1.1"]
                 [ring/ring-jetty-adapter "1.1.0"]
                 [ring/ring-devel "1.1.0"]
                 [environ "0.2.1"]
                 [hiccup "1.0.4"]
                 [garden "1.1.5"]
                 [clj-time "0.6.0"]
                 [com.novemberain/monger "1.7.0"]
                 [cljs-ajax "0.2.3"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.2.1"][lein-cljsbuild "1.0.2"]]
  :hooks [environ.leiningen.hooks leiningen.cljsbuild]
  :cljsbuild {
    :builds [{
        :source-paths ["src/isolkkariopen/cljs"]
        :compiler {
          :output-to "resources/public/js/compiled/main.js"
          :optimizations :whitespace
          :pretty-print true}}]}
  :uberjar-name "isolkkariopen-standalone.jar")
