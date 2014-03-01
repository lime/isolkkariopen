(ns isolkkariopen.template
  (:require [hiccup.core :refer [html]]
            [garden.core :refer [css]]
            [garden.units :refer [px em]]
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
    (def ribbon-shadow "0 0 10px rgba(0,0,0,0.5)")
    (def ribbon-text-shadow "0 0 10px rgba(0,0,0,0.31)")
    (def ribbon-rotate "rotate(45deg)")

    [:body {
      :font-family "Helvetica"
      :text-align  "center"
      :line-height (px 50)
      :margin      (px 50)
      :overflow-x  "hidden"}]
    [:h1 {
      :font-size   (px 32)
      :font-weight "normal"
      :color       "#999" }]
    [:.huge {
      :font-size   (px 64)
      :line-height (em 2)}]
    [:a {
      :text-decoration "none" }]
    [:#ribbon {
      :position    "absolute"
      :top         (px 42)
      :right       (px -42)
      :width       (px 200)
      :padding-top (px 1)
      :background-color "#c00"
      :background-image   "-webkit-linear-gradient(linear, left top, left bottom, from(rgba(0, 0, 0, 0)), to(rgba(0, 0, 0, 0.15)))"
      :box-shadow         ribbon-shadow
      :-moz-box-shadow    ribbon-shadow
      :-webkit-box-shadow ribbon-shadow
      :-moz-transform     ribbon-rotate
      :-webkit-transform  ribbon-rotate
      :-o-transform       ribbon-rotate
      :-ms-transform      ribbon-rotate
      :transform          ribbon-rotate
      }
      [:a {
        :color           "#eee"
        :display         "block"
        :font-family     "700 13px 'Helvetica Neue', Helvetica, Arial, sans-serif"
        :font-weight     500
        :padding-top     (px 1)
        :height          (px 24)
        :line-height     (px 24)
        :text-align      "center"
        :text-decoration "none"
        :font-size       (px 16)
        :border          "1px solid rgba(255,255,255,0.3)"
        :text-shadow     ribbon-text-shadow
      }]
    ]))

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