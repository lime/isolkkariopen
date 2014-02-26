(ns isolkkariopen.template
  (:require [hiccup.core :refer [html]]))

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
      [:style "body { font-family: 'Helvetica'; text-align: center; margin: 50px; line-height: 50px; } h1 { font-size: 32px; color: #999; } h2 { font-size: 64px; } ul { list-style-type: none; padding: 0; }"]]
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
          [:a {:href "http://github.com/joonasrouhiainen/isolkkariopen"} "Fork on Github"]]]]))