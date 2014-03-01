(ns isolkkariopen.template
  (:require [hiccup.core :refer [html]]
            [garden.core :refer [css]]
            [hiccup.page :refer [include-js]]))

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

(def style
  (css
    [:body {
      :font-family "Helvetica"
      :text-align  "center"
      :line-height "50px"
      :margin      "50px" }]
    [:h1 {
      :font-size   "32px"
      :color       "#999" }]
    [:h2 {
      :font-size "64px" }]
    [:ul {
      :list-style-type "none"
      :padding 0 }]))

(defn index [now-entry]
  (html
    [:head
      [:title "Is Olkkari open?"]
      [:style style]]
    [:body
      [:h1
        "Is "
        [:a {:href "http://www.athene.fi/toiminta/olocam/"} "Olkkari"] 
        " open?"]
      [:h2 (if (:open now-entry) "Yeah baby!" "Nope.")]
      [:p
        "Current buzz: "
        (:buzzPretty now-entry)
        [:br]
        (last-opposite-info now-entry)]
      [:ul
        [:li
          "API: "
          [:a {:href "/api/now"} "now"]
          " / "
          [:a {:href "/api/history"} "history"]]
        [:li
          [:a {:href "http://github.com/joonasrouhiainen/isolkkariopen"} "Fork on Github"]]]
      [:canvas {:id "history" :width 600 :height 300}]
      (include-js "js/lib/Chart.min.js")
      (include-js "js/compiled/main.js")]))