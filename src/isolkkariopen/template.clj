(ns isolkkariopen.template
  (:require [hiccup.core :refer [html]]))

(defn index [nowEntry]
  (html
    [:head
      [:title "Is Olkkari open?"]
      [:style "body { font-family: 'Helvetica'; text-align: center; margin: 50px; line-height: 50px; } h1 { font-size: 32px; color: #999; } h2 { font-size: 64px; } ul { list-style-type: none; padding: 0; }"]]
    [:body
      [:h1
        "Is "
        [:a {:href "http://www.athene.fi/toiminta/olocam/"} "Olkkari"] 
        " open?"]
      [:h2 (if (:open nowEntry) "Yeah baby!" "Nope.")]
      [:p
        "Current buzz: "
        (:buzzPretty nowEntry)
        [:br]
        "Last open: "
        (if (clojure.string/blank? (:lastOpen nowEntry))
          "no data :("
          (:lastOpenAgo nowEntry))
        [:br]
        "Last closed: "
        (if (clojure.string/blank? (:lastClosed nowEntry))
          "no data :("
          (:lastClosedAgo nowEntry))]
      [:ul
        [:li
          "API: "
          [:a {:href "/api/now"} "now"]
          " / "
          [:a {:href "/api/history"} "history"]]
        [:li
          [:a {:href "http://github.com/joonasrouhiainen/isolkkariopen"} "Fork on Github"]]]]))