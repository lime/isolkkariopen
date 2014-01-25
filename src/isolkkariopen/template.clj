(ns isolkkariopen.template
  (:require [hiccup.core :refer [html]]))

(defn index [open]
  (html
    [:head
      [:title "Is Olkkari open?"]
      [:style "body { font-family: 'Helvetica'; text-align: center; margin: 50px; line-height: 50px; } h1 { font-size: 32px; color: #999; } h2 { font-size: 64px; } ul { list-style-type: none; padding: 0; }"]]
    [:body
      [:h1
        "Is "
        [:a {:href "http://www.athene.fi/toiminta/olocam/"} "Olkkari"] 
        " open?"]
      [:h2 (if open "Yeah baby!" "Nope.")]
      [:ul
        [:li
          [:a {:href "/api/open"} "API LOL"]]
        [:li
          [:a {:href "http://github.com/joonasrouhiainen/isolkkariopen"} "Fork on Github"]]]]))