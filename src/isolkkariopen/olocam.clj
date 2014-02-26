(ns isolkkariopen.olocam
  (:require [clojure.math.numeric-tower :refer [abs]]
            [environ.core :refer [env]]
            [isolkkariopen.settings :refer [settings]])
  (import   [javax.imageio ImageIO]
            [java.awt Color]
            [java.net URL]))

(def url (new URL (settings :cam-url)))

(defn fetch-pic! [] (ImageIO/read url))

(defn pixel [img x y]
  (new Color (. img (getRGB x y)) true))

(defn databuffer [bufferedImg]
  (. (. bufferedImg (getRaster)) getDataBuffer))

(defn equality-buffer [buf1 buf2]
  "Boolean buffer through element equality comparison"
  (map
    #(< (abs (-(. buf1 (getElem %)) (. buf2 (getElem %)))) (settings :noise-tolerance))
    (range 0 (. buf1 (getSize)))))  

(defn normed-inequality [buf1 buf2]
  "Fractional amount of inequal elements between 0 and 1"

  (def eqbf (equality-buffer buf1 buf2))
  
  (if (zero? (count eqbf)) 0
    (/
      (count (filter false? eqbf))
      (count eqbf))))

(defn buzz [pic1 pic2]
  (min 1
    (* (settings :buzz-multiplier)
      (float
        (normed-inequality (databuffer pic1) (databuffer pic2))))))

(defn olkkari-open? [pic]
  (not= Color/black
    (pixel pic (:olkkari-open-x settings) (:olkkari-open-y settings))))