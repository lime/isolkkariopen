(ns isolkkariopen.olocam
  (import [javax.imageio ImageIO]
          [java.awt Color]
          [java.net URL]))

(def url
  (new URL "http://www.athene.fi/olocam/latest.jpg"))

(defn latest-pic []
  (ImageIO/read url))

(defn pixel [x y]
  (new Color
    (. (latest-pic) (getRGB x y)) true))

(defn olkkari-open? []
  (not
    (= Color/black
      (pixel 500 100))))