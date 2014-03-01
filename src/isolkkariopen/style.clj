(ns isolkkariopen.style
  (:require [garden.def :refer [defstyles]]
            [garden.units :refer [px em]]))

(defstyles style
  (def ribbon-shadow "0 0 10px rgba(0,0,0,0.5)")
  (def ribbon-text-shadow "0 0 10px rgba(0,0,0,0.31)")
  (def ribbon-rotate "rotate(45deg)")

  [:body :html {
    :overflow-x  "hidden"}]
  [:body {
    :font-family "'Open Sans', Arial, sans-serif"
    :text-align  "center"
    :line-height (px 50)
    :margin      (px 50)}]
  [:h1 {
    :font-size   (px 32)
    :font-family "'Helvetica Neue', Helvetica, Arial, sans-serif"
    :font-weight "normal"
    :color       "#999"}]
  [:.huge {
    :font-size   (px 64)
    :line-height (em 2)}]
  [:a {
    :text-decoration "none"}]
  [:#ribbon {
    :position    "fixed"
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
  ])