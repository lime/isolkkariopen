(ns isolkkariopen.style
  (:require [garden.def :refer [defstyles]]
            [garden.units :refer [px em]]))

(defstyles style
  :vendors ["webkit" "moz" "o"]
  [:body :html {
    :background  "#eee"
    :overflow-x  "hidden"}]
    :font-size   (px 18)
  [:body {
    :font-family "'Open Sans', Arial, sans-serif"
    :text-align  "center"
    :line-height (px 50)
    :margin      (px 50)}]
  [:h1 {
    :font {
      :family "'Helvetica Neue', Helvetica, Arial, sans-serif"
      :size   (px 32)
      :weight "normal"}
    :color       "#999"}]
  [:.huge {
    :font-size   (px 64)
    :line-height (em 2)}]
  [:a {
    :text-decoration "none"}]
  [:hr {
    :border "1px dashed #ccc"}]
  [:#ribbon {
    :top         (px 42)
    :right       (px -42)
    :width       (px 200)
    :padding-top (px 1)
    :position           "fixed"
    :background-color   "#c00"}
    [:a {
      :color           "#eee"
      :display         "block"
      :font-weight     500
      :padding-top     (px 1)
      :height          (px 24)
      :line-height     (px 24)
      :text-align      "center"
      :text-decoration "none"
      :font-size       (px 16)
      :border          "1px solid rgba(255,255,255,0.3)"
      :text-shadow     "0 0 10px rgba(0,0,0,0.31)"}]]
  [:#ribbon ^{:prefix true :vendors [:webkit :moz :o]} {
    :box-shadow       "0 0 10px rgba(0,0,0,0.5)"
    :transform        "rotate(45deg)"
  }])