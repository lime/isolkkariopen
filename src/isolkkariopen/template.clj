(ns isolkkariopen.template
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [isolkkariopen.style :refer [style]]))

(def placeholder "no data :(")

(defn- last-closed-info [entry]
  (str
    "Last closed: "
    (if (clojure.string/blank? (:lastClosed entry))
      placeholder
      (:lastClosedAgo entry))))

(defn- last-open-info [entry]
  (str
    "Last open: "
    (if (clojure.string/blank? (:lastOpen entry))
      placeholder
      (:lastOpenAgo entry))))

(defn- last-opposite-info [entry]
  ((if (:open entry) last-closed-info last-open-info) entry))

(defn index [now-entry]
  (html
    [:head
      [:title "Is Olkkari open?"]
      (include-css "css/style.css")]
    [:body
      [:h1
        "Is "
        [:a {:href "http://www.athene.fi/toiminta/olocam/"} "Olkkari"] 
        " open?"]
      [:span.huge (if (:open now-entry) "Yeah baby!" "Nope.")]
      [:p (last-opposite-info now-entry)]
      [:hr]
      [:h2 "Activity"]
      [:p "Current buzz: " (:buzzPretty now-entry)]
      [:canvas#history {:width 600 :height 300}]
      [:p
        "API: "
        [:a {:href "/api/now"} "now"]
        " / "
        [:a {:href "/api/history"} "history"]]
      [:div#ribbon
        [:a {:href "http://github.com/joonasrouhiainen/isolkkariopen"} "Fork me on Github"]]
      (include-js "js/lib/Chart.min.js")
      (include-js "js/lib/moment.min.js")
      (include-js "js/compiled/main.js")]))